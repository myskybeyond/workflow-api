package com.hdsolartech.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdOrderInfo;
import com.hdsolartech.business.domain.HdProjectFileInfo;
import com.hdsolartech.business.domain.bo.HdProjectFileInfoBo;
import com.hdsolartech.business.domain.bo.HdProjectFileVersionInfoBo;
import com.hdsolartech.business.domain.vo.HdProjectFileInfoVo;
import com.hdsolartech.business.mapper.HdProjectFileInfoMapper;
import com.hdsolartech.business.service.IHdProjectFileInfoService;
import com.hdsolartech.business.service.IHdProjectFileVersionInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.domain.vo.OssFileVo;
import org.dromara.common.core.service.ConfigService;
import org.dromara.common.core.service.OssService;
import org.dromara.common.core.service.ProcessService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 项目文件信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HdProjectFileInfoServiceImpl implements IHdProjectFileInfoService {

    private final HdProjectFileInfoMapper baseMapper;

    /**
     * 项目文件版本service
     */
    private final IHdProjectFileVersionInfoService projectFileVersionInfoService;


    /**
     * 查询项目文件信息
     */
    @Override
    public HdProjectFileInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目文件信息列表
     */
    @Override
    public TableDataInfo<HdProjectFileInfoVo> queryPageList(HdProjectFileInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectFileInfo> lqw = buildQueryWrapper(bo);
        Page<HdProjectFileInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目文件信息列表
     */
    @Override
    public List<HdProjectFileInfoVo> queryList(HdProjectFileInfoBo bo) {
        LambdaQueryWrapper<HdProjectFileInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectFileInfo> buildQueryWrapper(HdProjectFileInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectFileInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdProjectFileInfo::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdProjectFileInfo::getOrderId, bo.getOrderId());
        lqw.eq(bo.getCategoryId() != null, HdProjectFileInfo::getCategoryId, bo.getCategoryId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdProjectFileInfo::getName, bo.getName());
        lqw.eq(bo.getIsOnline() != null, HdProjectFileInfo::getIsOnline, bo.getIsOnline());
        lqw.eq(bo.getUpdateBy() != null, HdProjectFileInfo::getUpdateBy, bo.getUpdateBy());
        lqw.between(params.get("beginUpdateTime") != null && params.get("endUpdateTime") != null,
            HdProjectFileInfo::getUpdateTime ,params.get("beginUpdateTime"), params.get("endUpdateTime"));
        lqw.orderByDesc(HdProjectFileInfo::getUpdateTime);
        return lqw;
    }

    /**
     * 新增项目文件信息
     */
    @Override
    public Long  insertByBo(HdProjectFileInfoBo bo) {
        HdProjectFileInfo add = MapstructUtils.convert(bo, HdProjectFileInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if( bo.getIsOnline() == UserConstants.PROJECT_FILE_ONLINE_1 ){
            bo.setId(add.getId());
            //创建在线文件版本信息
            createOnlineFileVersion(bo);
        }
        return add.getId();
    }

    /**
     * 修改项目文件信息
     */
    @Override
    public Boolean updateByBo(HdProjectFileInfoBo bo) {
        HdProjectFileInfo update = MapstructUtils.convert(bo, HdProjectFileInfo.class);
        validEntityBeforeSave(update);
        HdProjectFileInfoVo files = this.queryById(bo.getId());
        if(files.getVersion()== null||files.getVersion() < bo.getVersion() ){
            //创建新版本
            //创建在线文件版本信息
            createOnlineFileVersion(bo);
        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectFileInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目文件信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void updateFileInfo(String relaFileInfo ,Long projectId ,Long orderId,Long userId){
        //文件分类
        String category = StrUtil.EMPTY;
        if(StrUtil.isNotEmpty(relaFileInfo)){
            String[] split = relaFileInfo.split(StringUtils.SEPARATOR);
            List<String> resList = new ArrayList<>();
            for(int i = 0 ;i < split.length ; i++){
                if(split[i].contains(UserConstants.FILE_CATEGORY_PREFIX)){
                    category = split[i].replace(UserConstants.FILE_CATEGORY_PREFIX,StrUtil.EMPTY);
                }else {
                    resList.add(split[i]);
                }

            }
            relaFileInfo = String.join(StringUtils.SEPARATOR, resList);
        }
        //更新文件Ids
        List<OssFileVo>  files = SpringUtils.getBean(OssService.class).selectFilesByIds(relaFileInfo);
        if(CollUtil.isNotEmpty(files)){
            //文件不为空
            for(int i = 0 ;i < files.size() ; i++){
                LambdaQueryWrapper<HdProjectFileInfo> lqw = Wrappers.lambdaQuery();
                lqw.eq(projectId != null, HdProjectFileInfo::getProjectId, projectId);
                if(orderId != null){
                    lqw.eq( HdProjectFileInfo::getOrderId, orderId);
                }else {
                    lqw.isNull(HdProjectFileInfo::getOrderId);
                }
                lqw.eq(HdProjectFileInfo::getName,files.get(i).getOriginalName());
                HdProjectFileInfo  proFile = baseMapper.selectOne(lqw);
                if(proFile != null){
                    //项目文件存在 //判断ossID是否一致
                    if( proFile.getOssFileId() != files.get(i).getOssId()){
                       //当前文件存储id 需要升级
                        HdProjectFileVersionInfoBo projectFileVersionInfo = new HdProjectFileVersionInfoBo();
                        projectFileVersionInfo.setProjectFileId(proFile.getId());
                        projectFileVersionInfo.setName(files.get(i).getOriginalName());
                        projectFileVersionInfo.setVersion(proFile.getVersion()+1);
                        projectFileVersionInfo.setDownloadUrl(files.get(i).getUrl());
                        projectFileVersionInfo.setIsOnline(UserConstants.PROJECT_FILE_ONLINE_0);
                        projectFileVersionInfo.setResult(0L);
                        if(userId != null){
                            projectFileVersionInfo.setCreateBy(userId);
                            projectFileVersionInfo.setUpdateBy(userId);
                        }
                        projectFileVersionInfoService.insertByBo(projectFileVersionInfo);
                       //升级文件
                        proFile.setVersion(proFile.getVersion()+1);
                        proFile.setOssFileId(files.get(i).getOssId());
                        proFile.setDownloadUrl(files.get(i).getUrl());
                        if(userId != null){
                            proFile.setUpdateBy(userId);
                        }
                        baseMapper.updateById(proFile);
                    }else {
                      //文件id一致 不需要进行升级。
                    }
                }else {
                    //创建新文件
                    proFile = new HdProjectFileInfo();
                    //项目id
                    proFile.setProjectId(projectId);
                    proFile.setOrderId(orderId );
                    proFile.setCategoryId(null);
                    proFile.setName(files.get(i).getOriginalName());
                    proFile.setVersion(1L);
                    proFile.setDownloadUrl(files.get(i).getUrl());
                    proFile.setIsOnline(UserConstants.PROJECT_FILE_ONLINE_0);
                    proFile.setOssFileId(files.get(i).getOssId());
                    //增加文件分类的处理
                    if(StrUtil.isNotEmpty(category)){
                        proFile.setCategoryId(Long.parseLong(category));
                    }
                    if(userId != null){
                        proFile.setCreateBy(userId);
                        proFile.setUpdateBy(userId);
                    }
                    //保存文件
                    baseMapper.insert(proFile);
                    //创建版本信息
                    HdProjectFileVersionInfoBo projectFileVersionInfo = new HdProjectFileVersionInfoBo();
                    projectFileVersionInfo.setProjectFileId(proFile.getId());
                    projectFileVersionInfo.setName(files.get(i).getOriginalName());
                    projectFileVersionInfo.setVersion(1L);
                    projectFileVersionInfo.setDownloadUrl(files.get(i).getUrl());
                    projectFileVersionInfo.setIsOnline(UserConstants.PROJECT_FILE_ONLINE_0);
                    projectFileVersionInfo.setResult(0L);
                    projectFileVersionInfoService.insertByBo(projectFileVersionInfo);

                }
            }
        }
    }

    @Override
    public void handleOnlineFiles(List<String> onlineFiles,Long projectId ,Long orderId ,Long userId ){
        try{
            //解析在线文件信息
            if(CollUtil.isEmpty(onlineFiles)){
                log.error("在线文件为空");
                return;
            }
            //在线文件处理
            for(int i = 0 ;i < onlineFiles.size() ; i++){
                String file = onlineFiles.get(i);
                //解析在线文件格式
                //onlineFiles = 在线文件id、在线文件名称、在线文件版本、在线文件备注、在线文件分类
                String[] fileAttributes =  file.split(StringUtils.SEPARATOR);
                //文件id
                Long fileId = Long.parseLong( fileAttributes[0]);
                String fileName = fileAttributes[1];
                Long version = null;
                if(fileAttributes.length >= 3){
                    version = StrUtil.isEmpty(fileAttributes[2]) ? null:Long.parseLong(fileAttributes[2]);
                }
                String description = "";
                if(fileAttributes.length >= 4){
                    description = fileAttributes[3];
                }
                String category = "";
                if(fileAttributes.length >= 5){
                    category = fileAttributes[4];
                }
                //查询项目 订单是否存在当前文件
                LambdaQueryWrapper<HdProjectFileInfo> lqw = Wrappers.lambdaQuery();
                lqw.eq(projectId != null, HdProjectFileInfo::getProjectId, projectId);
                if(orderId != null){
                    lqw.eq( HdProjectFileInfo::getOrderId, orderId);
                }else {
                    lqw.isNull(HdProjectFileInfo::getOrderId);
                }
                lqw.eq(HdProjectFileInfo::getOnlineFileId, fileId);
                HdProjectFileInfo  proFile = baseMapper.selectOne(lqw);
                if(proFile == null){
                    //创建新文件
                    proFile = new HdProjectFileInfo();
                    //项目id
                    proFile.setProjectId(projectId);
                    proFile.setOrderId(orderId );
                    proFile.setCategoryId(null);
                    proFile.setName(fileName);
                    proFile.setVersion(version);
                    proFile.setIsOnline(UserConstants.PROJECT_FILE_ONLINE_1);
                    proFile.setOnlineFileId(fileId);
                    proFile.setCategoryId(StrUtil.isEmpty(category)?null:Long.parseLong(category));
                    proFile.setDescription(description);
                    if(userId != null){
                        proFile.setCreateBy(userId);
                        proFile.setUpdateBy(userId);
                    }
                    //保存文件
                    baseMapper.insert(proFile);
                    //创建版本信息
                    HdProjectFileVersionInfoBo projectFileVersionInfo = new HdProjectFileVersionInfoBo();
                    projectFileVersionInfo.setProjectFileId(proFile.getId());
                    projectFileVersionInfo.setName(fileName);
                    projectFileVersionInfo.setVersion(version);
                    projectFileVersionInfo.setDownloadUrl(null);
                    projectFileVersionInfo.setIsOnline(UserConstants.PROJECT_FILE_ONLINE_1);
                    projectFileVersionInfo.setDescription(description);
                    projectFileVersionInfo.setResult(0L);
                    if(userId != null){
                        projectFileVersionInfo.setCreateBy(userId);
                        projectFileVersionInfo.setUpdateBy(userId);
                    }
                    projectFileVersionInfoService.insertByBo(projectFileVersionInfo);
                }else {
                    long fileVersion  = ObjectUtil.isNotNull(proFile.getVersion())?proFile.getVersion():0L;
                    if(ObjectUtil.isNotNull(version)  && version.longValue() > fileVersion ){
                        //增加版本信息
                        HdProjectFileVersionInfoBo projectFileVersionInfo = new HdProjectFileVersionInfoBo();
                        projectFileVersionInfo.setProjectFileId(proFile.getId());
                        projectFileVersionInfo.setName(fileName);
                        projectFileVersionInfo.setVersion(version);
                        projectFileVersionInfo.setDownloadUrl(null);
                        projectFileVersionInfo.setIsOnline(UserConstants.PROJECT_FILE_ONLINE_1);
                        projectFileVersionInfo.setDescription(description);
                        projectFileVersionInfo.setResult(0L);
                        if(userId != null){
                            projectFileVersionInfo.setCreateBy(userId);
                            projectFileVersionInfo.setUpdateBy(userId);
                        }
                        projectFileVersionInfoService.insertByBo(projectFileVersionInfo);
                        //项目id
                        proFile.setName(fileName);
                        proFile.setVersion(version);
                        proFile.setDescription(description);
                        if(userId != null){
                            proFile.setCreateBy(userId);
                            proFile.setUpdateBy(userId);
                        }
                        //保存文件
                        baseMapper.updateById(proFile);
                    }

                }
            }
        }catch (Exception e){
            log.error("处理在线文件信息",e);
        }

    }


    /**
     * 查询项目文件信息列表
     */
    @Override
    public TableDataInfo<HdProjectFileInfoVo> myList(HdProjectFileInfoBo bo, PageQuery pageQuery){
        LambdaQueryWrapper<HdProjectFileInfo> lqw = buildMyQueryWrapper(bo);
        Page<HdProjectFileInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private LambdaQueryWrapper<HdProjectFileInfo> buildMyQueryWrapper(HdProjectFileInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectFileInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdProjectFileInfo::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdProjectFileInfo::getOrderId, bo.getOrderId());
        lqw.eq(bo.getCategoryId() != null, HdProjectFileInfo::getCategoryId, bo.getCategoryId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdProjectFileInfo::getName, bo.getName());
        lqw.eq(bo.getIsOnline() != null, HdProjectFileInfo::getIsOnline, bo.getIsOnline());
        lqw.eq( HdProjectFileInfo::getUpdateBy,  LoginHelper.getUserId() );
        lqw.between(params.get("beginUpdateTime") != null && params.get("endUpdateTime") != null,
            HdProjectFileInfo::getUpdateTime ,params.get("beginUpdateTime"), params.get("endUpdateTime"));
        lqw.orderByDesc(HdProjectFileInfo::getUpdateTime);
        return lqw;
    }


    public void createOnlineFileVersion(HdProjectFileInfoBo bo ){
        try{
            //创建版本信息
            HdProjectFileVersionInfoBo projectFileVersionInfo = new HdProjectFileVersionInfoBo();
            projectFileVersionInfo.setProjectFileId(bo.getId());
            projectFileVersionInfo.setName(bo.getName());
            projectFileVersionInfo.setVersion(bo.getVersion());
            projectFileVersionInfo.setDownloadUrl(null);
            projectFileVersionInfo.setIsOnline(UserConstants.PROJECT_FILE_ONLINE_1);
            projectFileVersionInfo.setDescription(bo.getDescription());
            projectFileVersionInfo.setResult(0L);
            projectFileVersionInfoService.insertByBo(projectFileVersionInfo);
        }catch (Exception e){
            log.error("处理在线文件信息",e);
        }

    }

    /**
     * 查询项目文件信息列表
     */
    @Override
    public TableDataInfo<HdProjectFileInfoVo> getListByTaskId(HdProjectFileInfoBo bo, PageQuery pageQuery){
        if(StrUtil.isEmpty(bo.getTaskId())){
            //增加特殊处理 查询我自身创建的文件
            LambdaQueryWrapper<HdProjectFileInfo> lqw = buildMyQueryWrapper(bo);
            Page<HdProjectFileInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
            return TableDataInfo.build(result);
        }
        //根据task 查询订单id
        //项目id
        String projectId = SpringUtils.getBean(ProcessService.class).getProjectIdByTaskId(bo.getTaskId());
        //订单id
        String orderId = SpringUtils.getBean(ProcessService.class).getOrderIdByTaskId(bo.getTaskId());
        if(StrUtil.isEmpty(projectId) && StrUtil.isEmpty(orderId)){
            return TableDataInfo.build(pageQuery.build());
        }
        bo.setProjectId(StrUtil.isNotEmpty(projectId)?Long.parseLong(projectId):null);
        bo.setOrderId(StrUtil.isNotEmpty(orderId)?Long.parseLong(orderId):null);
        LambdaQueryWrapper<HdProjectFileInfo> lqw = buildQueryWrapperForTask(bo);
        Page<HdProjectFileInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }


    private LambdaQueryWrapper<HdProjectFileInfo> buildQueryWrapperForTask(HdProjectFileInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectFileInfo> lqw = Wrappers.lambdaQuery();
        if(bo.getOrderId() != null){
            lqw.eq( HdProjectFileInfo::getOrderId, bo.getOrderId());
        }else{
            //使用项目id 查询数据
            lqw.eq(bo.getProjectId() != null, HdProjectFileInfo::getProjectId, bo.getProjectId());
        }
        lqw.eq(bo.getCategoryId() != null, HdProjectFileInfo::getCategoryId, bo.getCategoryId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdProjectFileInfo::getName, bo.getName());
        lqw.eq(bo.getIsOnline() != null, HdProjectFileInfo::getIsOnline, bo.getIsOnline());
        lqw.eq(bo.getUpdateBy() != null, HdProjectFileInfo::getUpdateBy, bo.getUpdateBy());
        lqw.between(params.get("beginUpdateTime") != null && params.get("endUpdateTime") != null,
            HdProjectFileInfo::getUpdateTime ,params.get("beginUpdateTime"), params.get("endUpdateTime"));
        lqw.orderByDesc(HdProjectFileInfo::getUpdateTime);
        return lqw;
    }
}
