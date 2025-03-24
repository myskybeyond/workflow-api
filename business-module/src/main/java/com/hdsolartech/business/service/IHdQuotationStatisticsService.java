package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdQuotationStatistics;
import com.hdsolartech.business.domain.vo.HdQuotationStatisticsVo;
import com.hdsolartech.business.domain.bo.HdQuotationStatisticsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目报价统计信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-28
 */
public interface IHdQuotationStatisticsService {

    /**
     * 查询项目报价统计信息
     */
    HdQuotationStatisticsVo queryById(Long id);

    /**
     * 查询项目报价统计信息列表
     */
    TableDataInfo<HdQuotationStatisticsVo> queryPageList(HdQuotationStatisticsBo bo, PageQuery pageQuery);

    /**
     * 查询项目报价统计信息列表
     */
    List<HdQuotationStatisticsVo> queryList(HdQuotationStatisticsBo bo);

    /**
     * 新增项目报价统计信息
     */
    Boolean insertByBo(HdQuotationStatisticsBo bo);

    /**
     * 修改项目报价统计信息
     */
    Boolean updateByBo(HdQuotationStatisticsBo bo);

    /**
     * 校验并批量删除项目报价统计信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
