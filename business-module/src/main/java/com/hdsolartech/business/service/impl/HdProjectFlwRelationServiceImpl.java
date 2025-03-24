package com.hdsolartech.business.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;
import com.hdsolartech.business.domain.bo.HdProjectFlwRelationBo;
import com.hdsolartech.business.domain.vo.HdProjectFlwRelationVo;
import com.hdsolartech.business.domain.HdProjectFlwRelation;
import com.hdsolartech.business.mapper.HdProjectFlwRelationMapper;
import com.hdsolartech.business.service.IHdProjectFlwRelationService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 项目流程关系Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-24
 */
@RequiredArgsConstructor
@Service
public class HdProjectFlwRelationServiceImpl implements IHdProjectFlwRelationService {

    private final HdProjectFlwRelationMapper baseMapper;

    @Override
    public Page<Map> queryTrackingPageList(HdProjectFlwRelationBo bo, PageQuery pageQuery) {
        Page<Map> mapPage = baseMapper.queryTracking(bo, pageQuery.build(), LoginHelper.getUserId());
        return mapPage;
    }

    /**
     * 查询项目流程关系
     */
    @Override
    public HdProjectFlwRelationVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目流程关系列表
     */
    @Override
    public TableDataInfo<HdProjectFlwRelationVo> queryPageList(HdProjectFlwRelationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectFlwRelation> lqw = buildQueryWrapper(bo);
        Page<HdProjectFlwRelationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目流程关系列表
     */
    @Override
    public List<HdProjectFlwRelationVo> queryList(HdProjectFlwRelationBo bo) {
        LambdaQueryWrapper<HdProjectFlwRelation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectFlwRelation> buildQueryWrapper(HdProjectFlwRelationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectFlwRelation> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getType() != null, HdProjectFlwRelation::getType, bo.getType());
        lqw.eq(bo.getProjectId() != null, HdProjectFlwRelation::getProjectId, bo.getProjectId());
        lqw.eq(StringUtils.isNotBlank(bo.getActHiTaskinstId()), HdProjectFlwRelation::getActHiTaskinstId, bo.getActHiTaskinstId());
        lqw.eq(StringUtils.isNotBlank(bo.getProcInstId()), HdProjectFlwRelation::getProcInstId, bo.getProcInstId());
        lqw.eq(StringUtils.isNotBlank(bo.getProcDefId()), HdProjectFlwRelation::getProcDefId, bo.getProcDefId());
        return lqw;
    }

    /**
     * 新增项目流程关系
     */
    @Override
    public Boolean insertByBo(HdProjectFlwRelationBo bo) {
        HdProjectFlwRelation add = MapstructUtils.convert(bo, HdProjectFlwRelation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目流程关系
     */
    @Override
    public Boolean updateByBo(HdProjectFlwRelationBo bo) {
        HdProjectFlwRelation update = MapstructUtils.convert(bo, HdProjectFlwRelation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectFlwRelation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目流程关系
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
