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
import com.hdsolartech.business.domain.bo.HdOrderFlwRelationBo;
import com.hdsolartech.business.domain.vo.HdOrderFlwRelationVo;
import com.hdsolartech.business.domain.HdOrderFlwRelation;
import com.hdsolartech.business.mapper.HdOrderFlwRelationMapper;
import com.hdsolartech.business.service.IHdOrderFlwRelationService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 订单流程关系Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-24
 */
@RequiredArgsConstructor
@Service
public class HdOrderFlwRelationServiceImpl implements IHdOrderFlwRelationService {

    private final HdOrderFlwRelationMapper baseMapper;

    /**
     * 查询订单流程关系
     */
    @Override
    public HdOrderFlwRelationVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 下单进度跟踪列表
     */
    @Override
    public Page<Map> queryPageList(HdOrderFlwRelationBo bo, PageQuery pageQuery) {
//        LambdaQueryWrapper<HdOrderFlwRelation> lqw = buildQueryWrapper(bo);
//        Page<HdOrderFlwRelationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
//        Page<Map> result = baseMapper.queryOrderList(bo, pageQuery.build(), LoginHelper.getUserId());
        Page<Map> result = baseMapper.queryOrderList(bo, pageQuery.build());

        return result;
    }

    /**
     * 查询订单流程关系列表
     */
    @Override
    public List<HdOrderFlwRelationVo> queryList(HdOrderFlwRelationBo bo) {
        LambdaQueryWrapper<HdOrderFlwRelation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdOrderFlwRelation> buildQueryWrapper(HdOrderFlwRelationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdOrderFlwRelation> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getType() != null, HdOrderFlwRelation::getType, bo.getType());
        lqw.eq(bo.getProjectId() != null, HdOrderFlwRelation::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdOrderFlwRelation::getOrderId, bo.getOrderId());
        lqw.eq(StringUtils.isNotBlank(bo.getActHiTaskinstId()), HdOrderFlwRelation::getActHiTaskinstId, bo.getActHiTaskinstId());
        lqw.eq(StringUtils.isNotBlank(bo.getProcInstId()), HdOrderFlwRelation::getProcInstId, bo.getProcInstId());
        lqw.eq(StringUtils.isNotBlank(bo.getProcDefId()), HdOrderFlwRelation::getProcDefId, bo.getProcDefId());
        return lqw;
    }

    /**
     * 新增订单流程关系
     */
    @Override
    public Boolean insertByBo(HdOrderFlwRelationBo bo) {
        HdOrderFlwRelation add = MapstructUtils.convert(bo, HdOrderFlwRelation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改订单流程关系
     */
    @Override
    public Boolean updateByBo(HdOrderFlwRelationBo bo) {
        HdOrderFlwRelation update = MapstructUtils.convert(bo, HdOrderFlwRelation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdOrderFlwRelation entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除订单流程关系
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
