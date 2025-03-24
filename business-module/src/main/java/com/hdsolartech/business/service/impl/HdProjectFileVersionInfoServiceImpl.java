package com.hdsolartech.business.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hdsolartech.business.domain.bo.HdProjectFileVersionInfoBo;
import com.hdsolartech.business.domain.vo.HdProjectFileVersionInfoVo;
import com.hdsolartech.business.domain.HdProjectFileVersionInfo;
import com.hdsolartech.business.mapper.HdProjectFileVersionInfoMapper;
import com.hdsolartech.business.service.IHdProjectFileVersionInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 项目文件版本信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@RequiredArgsConstructor
@Service
public class HdProjectFileVersionInfoServiceImpl implements IHdProjectFileVersionInfoService {

    private final HdProjectFileVersionInfoMapper baseMapper;

    /**
     * 查询项目文件版本信息
     */
    @Override
    public HdProjectFileVersionInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目文件版本信息列表
     */
    @Override
    public TableDataInfo<HdProjectFileVersionInfoVo> queryPageList(HdProjectFileVersionInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectFileVersionInfo> lqw = buildQueryWrapper(bo);
        Page<HdProjectFileVersionInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目文件版本信息列表
     */
    @Override
    public List<HdProjectFileVersionInfoVo> queryList(HdProjectFileVersionInfoBo bo) {
        LambdaQueryWrapper<HdProjectFileVersionInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectFileVersionInfo> buildQueryWrapper(HdProjectFileVersionInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectFileVersionInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectFileId() != null, HdProjectFileVersionInfo::getProjectFileId, bo.getProjectFileId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdProjectFileVersionInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getDownloadUrl()), HdProjectFileVersionInfo::getDownloadUrl, bo.getDownloadUrl());
        lqw.eq(bo.getIsOnline() != null, HdProjectFileVersionInfo::getIsOnline, bo.getIsOnline());
        lqw.eq(bo.getResult() != null, HdProjectFileVersionInfo::getResult, bo.getResult());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), HdProjectFileVersionInfo::getDescription, bo.getDescription());
        lqw.orderByDesc(HdProjectFileVersionInfo::getCreateTime);
        return lqw;
    }

    /**
     * 新增项目文件版本信息
     */
    @Override
    public Boolean insertByBo(HdProjectFileVersionInfoBo bo) {
        HdProjectFileVersionInfo add = MapstructUtils.convert(bo, HdProjectFileVersionInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目文件版本信息
     */
    @Override
    public Boolean updateByBo(HdProjectFileVersionInfoBo bo) {
        HdProjectFileVersionInfo update = MapstructUtils.convert(bo, HdProjectFileVersionInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectFileVersionInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目文件版本信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
