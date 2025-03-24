package com.hdsolartech.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdProjectProgressStatistics;
import com.hdsolartech.business.domain.bo.HdProjectProgressStatisticsBo;
import com.hdsolartech.business.domain.vo.HdProjectProgressStatisticsVo;
import com.hdsolartech.business.mapper.HdProjectProgressStatisticsMapper;
import com.hdsolartech.business.service.IHdProjectProgressStatisticsService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 项目进度统计信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@RequiredArgsConstructor
@Service
public class HdProjectProgressStatisticsServiceImpl implements IHdProjectProgressStatisticsService {

    private final HdProjectProgressStatisticsMapper baseMapper;

    /**
     * 查询项目进度统计信息
     */
    @Override
    public HdProjectProgressStatisticsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目进度统计信息列表
     */
    @Override
    public TableDataInfo<HdProjectProgressStatisticsVo> queryPageList(HdProjectProgressStatisticsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectProgressStatistics> lqw = buildQueryWrapper(bo);
        Page<HdProjectProgressStatisticsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目进度统计信息列表
     */
    @Override
    public List<HdProjectProgressStatisticsVo> queryList(HdProjectProgressStatisticsBo bo) {
        LambdaQueryWrapper<HdProjectProgressStatistics> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectProgressStatistics> buildQueryWrapper(HdProjectProgressStatisticsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectProgressStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdProjectProgressStatistics::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdProjectProgressStatistics::getOrderId, bo.getOrderId());
        lqw.eq(bo.getProjectProgressId() != null, HdProjectProgressStatistics::getProjectProgressId, bo.getProjectProgressId());
        lqw.eq(StringUtils.isNotBlank(bo.getArrival()), HdProjectProgressStatistics::getArrival, bo.getArrival());
        lqw.eq(StringUtils.isNotBlank(bo.getProduction()), HdProjectProgressStatistics::getProduction, bo.getProduction());
        lqw.eq(StringUtils.isNotBlank(bo.getBlack()), HdProjectProgressStatistics::getBlack, bo.getBlack());
        lqw.eq(StringUtils.isNotBlank(bo.getWhite()), HdProjectProgressStatistics::getWhite, bo.getWhite());
        lqw.eq(StringUtils.isNotBlank(bo.getDelivery()), HdProjectProgressStatistics::getDelivery, bo.getDelivery());
        lqw.eq(bo.getOrderDate() != null, HdProjectProgressStatistics::getOrderDate, bo.getOrderDate());
        lqw.eq(bo.getDeliverStartDate() != null, HdProjectProgressStatistics::getDeliverStartDate, bo.getDeliverStartDate());
        lqw.eq(bo.getDeliverEndDate() != null, HdProjectProgressStatistics::getDeliverEndDate, bo.getDeliverEndDate());
        lqw.eq(bo.getStatisticsDate() != null, HdProjectProgressStatistics::getStatisticsDate, bo.getStatisticsDate());
        lqw.between(params.get("beginStatisticsDate") != null && params.get("endStatisticsDate") != null,
            HdProjectProgressStatistics::getStatisticsDate ,params.get("beginStatisticsDate"), params.get("endStatisticsDate"));
        lqw.orderByDesc(HdProjectProgressStatistics::getId);
        return lqw;
    }

    /**
     * 新增项目进度统计信息
     */
    @Override
    public Boolean insertByBo(HdProjectProgressStatisticsBo bo) {
        HdProjectProgressStatistics add = MapstructUtils.convert(bo, HdProjectProgressStatistics.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目进度统计信息
     */
    @Override
    public Boolean updateByBo(HdProjectProgressStatisticsBo bo) {
        HdProjectProgressStatistics update = MapstructUtils.convert(bo, HdProjectProgressStatistics.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectProgressStatistics entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目进度统计信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
    /**
     * 批量保存进度统计信息
     * @param list
     * @return
     */
    @Override
    public Boolean saveBatch(List<HdProjectProgressStatistics>  list){
        return baseMapper.insertBatch(list);
    }
}
