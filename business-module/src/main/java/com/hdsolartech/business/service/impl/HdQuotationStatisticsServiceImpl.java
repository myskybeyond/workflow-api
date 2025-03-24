package com.hdsolartech.business.service.impl;

import com.hdsolartech.business.domain.HdProjectInfo;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hdsolartech.business.domain.bo.HdQuotationStatisticsBo;
import com.hdsolartech.business.domain.vo.HdQuotationStatisticsVo;
import com.hdsolartech.business.domain.HdQuotationStatistics;
import com.hdsolartech.business.mapper.HdQuotationStatisticsMapper;
import com.hdsolartech.business.service.IHdQuotationStatisticsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 项目报价统计信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-28
 */
@RequiredArgsConstructor
@Service
public class HdQuotationStatisticsServiceImpl implements IHdQuotationStatisticsService {

    private final HdQuotationStatisticsMapper baseMapper;

    /**
     * 查询项目报价统计信息
     */
    @Override
    public HdQuotationStatisticsVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目报价统计信息列表
     */
    @Override
    public TableDataInfo<HdQuotationStatisticsVo> queryPageList(HdQuotationStatisticsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdQuotationStatistics> lqw = buildQueryWrapper(bo);
        Page<HdQuotationStatisticsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目报价统计信息列表
     */
    @Override
    public List<HdQuotationStatisticsVo> queryList(HdQuotationStatisticsBo bo) {
        LambdaQueryWrapper<HdQuotationStatistics> lqw = buildQueryWrapper(bo);


        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdQuotationStatistics> buildQueryWrapper(HdQuotationStatisticsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdQuotationStatistics> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdQuotationStatistics::getProjectId, bo.getProjectId());
        lqw.eq(StringUtils.isNotBlank(bo.getProjectType()), HdQuotationStatistics::getProjectType, bo.getProjectType());
        lqw.eq(bo.getDeptId() != null, HdQuotationStatistics::getDeptId, bo.getDeptId());
        lqw.between(params.get("beginStatisticsDate") != null && params.get("endStatisticsDate") != null,
            HdQuotationStatistics::getStatisticsDate ,params.get("beginStatisticsDate"), params.get("endStatisticsDate"));
        lqw.orderByDesc(HdQuotationStatistics::getId);
        return lqw;
    }

    /**
     * 新增项目报价统计信息
     */
    @Override
    public Boolean insertByBo(HdQuotationStatisticsBo bo) {
        HdQuotationStatistics add = MapstructUtils.convert(bo, HdQuotationStatistics.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目报价统计信息
     */
    @Override
    public Boolean updateByBo(HdQuotationStatisticsBo bo) {
        HdQuotationStatistics update = MapstructUtils.convert(bo, HdQuotationStatistics.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdQuotationStatistics entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目报价统计信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
