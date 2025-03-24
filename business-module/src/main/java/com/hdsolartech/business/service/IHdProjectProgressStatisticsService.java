package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdProjectProgressStatistics;
import com.hdsolartech.business.domain.vo.HdProjectProgressStatisticsVo;
import com.hdsolartech.business.domain.bo.HdProjectProgressStatisticsBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目进度统计信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-29
 */
public interface IHdProjectProgressStatisticsService {

    /**
     * 查询项目进度统计信息
     */
    HdProjectProgressStatisticsVo queryById(Long id);

    /**
     * 查询项目进度统计信息列表
     */
    TableDataInfo<HdProjectProgressStatisticsVo> queryPageList(HdProjectProgressStatisticsBo bo, PageQuery pageQuery);

    /**
     * 查询项目进度统计信息列表
     */
    List<HdProjectProgressStatisticsVo> queryList(HdProjectProgressStatisticsBo bo);

    /**
     * 新增项目进度统计信息
     */
    Boolean insertByBo(HdProjectProgressStatisticsBo bo);

    /**
     * 修改项目进度统计信息
     */
    Boolean updateByBo(HdProjectProgressStatisticsBo bo);

    /**
     * 校验并批量删除项目进度统计信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 批量保存统计信息
     * @param list
     * @return
     */
    Boolean saveBatch(List<HdProjectProgressStatistics>  list);
}
