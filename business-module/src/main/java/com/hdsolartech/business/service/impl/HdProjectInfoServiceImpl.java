package com.hdsolartech.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdOrderInfo;
import com.hdsolartech.business.domain.HdProjectInfo;
import com.hdsolartech.business.domain.bo.HdOrderAuthorityBo;
import com.hdsolartech.business.domain.bo.HdProjectAuthorityBo;
import com.hdsolartech.business.domain.bo.HdProjectInfoBo;
import com.hdsolartech.business.domain.vo.HdOrderAuthorityVo;
import com.hdsolartech.business.domain.vo.HdProjectAuthorityVo;
import com.hdsolartech.business.domain.vo.HdProjectInfoVo;
import com.hdsolartech.business.mapper.HdProjectInfoMapper;
import com.hdsolartech.business.service.IHdProjectAuthorityService;
import com.hdsolartech.business.service.IHdProjectFileInfoService;
import com.hdsolartech.business.service.IHdProjectInfoService;
import com.hdsolartech.business.service.IHdProjectOperLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.domain.dto.RoleDTO;
import org.dromara.common.core.service.ProjectService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 项目信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HdProjectInfoServiceImpl implements IHdProjectInfoService, ProjectService {

    private final HdProjectInfoMapper baseMapper;


    /**
     * 项目文件service
     */
    private final IHdProjectFileInfoService projectFileInfoService;


    /**
     * 项目日志service
     */
    private final IHdProjectOperLogService projectOperLogService;

    /**
     * 项目权限 service
     */
    private final IHdProjectAuthorityService projectAuthorityService;


    /**
     * 查询项目信息
     */
    @Override
    public HdProjectInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目信息列表
     */
    @Override
    public TableDataInfo<HdProjectInfoVo> queryPageList(HdProjectInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectInfo> lqw = buildQueryWrapperByPermission(bo);
        if(lqw == null){
            //直接返回空数据
            return TableDataInfo.build(pageQuery.build());
        }
        Page<HdProjectInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目信息列表
     */
    @Override
    public List<HdProjectInfoVo> queryList(HdProjectInfoBo bo) {
        LambdaQueryWrapper<HdProjectInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectInfo> buildQueryWrapperByPermission(HdProjectInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getProjectNo()), HdProjectInfo::getProjectNo, bo.getProjectNo());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), HdProjectInfo::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdProjectInfo::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getPurchaseUnit()), HdProjectInfo::getPurchaseUnit, bo.getPurchaseUnit());
        lqw.like(StringUtils.isNotBlank(bo.getOwnerUnit()), HdProjectInfo::getOwnerUnit, bo.getOwnerUnit());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            HdProjectInfo::getCreateTime ,params.get("beginCreateTime"), params.get("endCreateTime"));
        if(ArrayUtil.isNotEmpty(bo.getStatusArr())){
            lqw.in( HdProjectInfo::getStatus, Arrays.asList(bo.getStatusArr()));
        }
        lqw.eq(bo.getStatus() != null, HdProjectInfo::getStatus,bo.getStatus());
        lqw.orderByDesc(HdProjectInfo::getId);
        //admin  数据全部返回
        if( LoginHelper.isSuperAdmin()){
            return lqw;
        }
        //具有某个角色的用户 数据全部返回
        if(CollUtil.isNotEmpty(LoginHelper.getLoginUser().getRoles())){
            List<String>  roleKeys = LoginHelper.getLoginUser().getRoles().stream().map(o->o.getRoleKey()).collect(Collectors.toList());
            if(roleKeys.contains(UserConstants.PROJECT_ORDER_DATA_ROLE_KEY)){
                return lqw;
            }
        }
        HdProjectAuthorityBo authorityBo  = new HdProjectAuthorityBo();
        authorityBo.setUserId(LoginHelper.getUserId());
        //数据需要通过项目权限信息进行过滤
        List<HdProjectAuthorityVo>
            projectAuthorityVos = projectAuthorityService.queryList(authorityBo);
        if(CollUtil.isEmpty(projectAuthorityVos)){
            //没有项目查询权限
            return null;
        }
        lqw.in(HdProjectInfo::getId, projectAuthorityVos.stream().map(HdProjectAuthorityVo::getProjectId).collect(Collectors.toList()));
        return lqw;
    }

    private LambdaQueryWrapper<HdProjectInfo> buildQueryWrapper(HdProjectInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getProjectNo()), HdProjectInfo::getProjectNo, bo.getProjectNo());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), HdProjectInfo::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdProjectInfo::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getPurchaseUnit()), HdProjectInfo::getPurchaseUnit, bo.getPurchaseUnit());
        lqw.like(StringUtils.isNotBlank(bo.getOwnerUnit()), HdProjectInfo::getOwnerUnit, bo.getOwnerUnit());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            HdProjectInfo::getCreateTime ,params.get("beginCreateTime"), params.get("endCreateTime"));
        if(ArrayUtil.isNotEmpty(bo.getStatusArr())){
            lqw.in( HdProjectInfo::getStatus, Arrays.asList(bo.getStatusArr()));
        }
        lqw.orderByDesc(HdProjectInfo::getId);

        return lqw;
    }

    /**
     * 新增项目信息
     */
    @Override
    public Boolean insertByBo(HdProjectInfoBo bo) {
        HdProjectInfo add = MapstructUtils.convert(bo, HdProjectInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            //保存文件信息
            projectFileInfoService.updateFileInfo(add.getRelaFileInfo() ,add.getId() ,null,null);
            //判断是否存在文件信息 保存项目和文件的信息
            projectOperLogService.recordLog(UserConstants.PROJECT_OPER_LOG_TYPE_1, add.getId(), null, UserConstants.PROJECT_OPERATE_TYPE_01,UserConstants.PROJECT_OPERATE_RESULT_SUCCESS,"");
            //创建项目权限service
            HdProjectAuthorityBo authBo = new HdProjectAuthorityBo();
            authBo.setProjectId(add.getId());
            //设置登录用户
            authBo.setUserId( LoginHelper.getUserId());
            //创建项目权限bo
            projectAuthorityService.insertByBo(authBo);
            //如果负责人 不为 登录用户id
            if( LoginHelper.getUserId().longValue()!=bo.getManager().longValue()){
                HdProjectAuthorityBo authBo1 = new HdProjectAuthorityBo();
                authBo1.setProjectId(add.getId());
                //设置登录用户
                authBo1.setUserId(bo.getManager());
                //创建项目权限bo
                projectAuthorityService.insertByBo(authBo1);
            }
        }
        return flag;
    }

    /**
     * 修改项目信息
     */
    @Override
    public Boolean updateByBo(HdProjectInfoBo bo) {
        HdProjectInfo update = MapstructUtils.convert(bo, HdProjectInfo.class);
        validEntityBeforeSave(update);
        Boolean flag =  baseMapper.updateById(update) > 0;
        //保存文件信息
        projectFileInfoService.updateFileInfo(update.getRelaFileInfo() ,update.getId() ,null,null);
        //判断是否存在文件信息 保存项目和文件的信息
        projectOperLogService.recordLog(UserConstants.PROJECT_OPER_LOG_TYPE_1, update.getId(), null, UserConstants.PROJECT_OPERATE_TYPE_02,UserConstants.PROJECT_OPERATE_RESULT_SUCCESS,"");
        //创建 负责人权限信息
        HdProjectAuthorityBo queryBo = new HdProjectAuthorityBo();
        queryBo.setProjectId(bo.getId());
        queryBo.setUserId(bo.getManager());
        HdProjectAuthorityVo authorityVo = projectAuthorityService.queryByBo(queryBo);
        if(authorityVo == null){
            //插入负责人权限信息
            HdProjectAuthorityBo projectAuthorityBo = new HdProjectAuthorityBo();
            projectAuthorityBo.setProjectId(bo.getId());
            //设置登录用户
            projectAuthorityBo.setUserId(bo.getManager());
            //创建项目权限bo
            projectAuthorityService.insertByBo(projectAuthorityBo);
        }
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    @Override
    public String selectNameByProjectId(Long projectId) {
        HdProjectInfoVo projectInfoVo =  baseMapper.selectVoById(projectId);
        if( projectInfoVo !=null){
            return projectInfoVo.getName();
        } else {
            return "";
        }

    }

    /**
     * 获取未完成的项目
     * @return
     */
    @Override
    public List<Long>  undoneList(){
        //非完成的项目信息
        List<HdProjectInfo>  proList = baseMapper.selectList(Wrappers.<HdProjectInfo>query().lambda().select(HdProjectInfo::getId).ne(HdProjectInfo::getStatus,UserConstants.PROJECT_STATUS_COMPLETED));
        if(CollUtil.isEmpty(proList)){
            return null;
        }
        return proList.stream().map(pro->pro.getId()).collect(Collectors.toList());
    }

    /**
     * 查询项目信息列表
     */
    @Override
    public List<HdProjectInfoVo> queryMyList(HdProjectInfoBo bo){
        LambdaQueryWrapper<HdProjectInfo> lqw = buildQueryWrapperByPermission(bo);
        return baseMapper.selectVoList(lqw);
    }
}
