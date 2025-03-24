package com.hdsolartech.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdOrderFlwRelation;
import com.hdsolartech.business.domain.vo.HdOrderFlwRelationVo;
import com.hdsolartech.business.domain.bo.HdOrderFlwRelationBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 订单流程关系Service接口
 *
 * @author Lion Li
 * @date 2024-06-24
 */
public interface IHdOrderFlwRelationService {

    /**
     * 查询订单流程关系
     */
    HdOrderFlwRelationVo queryById(Long id);

    /**
     * 下单进度跟踪列表
     */
    Page<Map> queryPageList(HdOrderFlwRelationBo bo, PageQuery pageQuery);

    /**
     * 查询订单流程关系列表
     */
    List<HdOrderFlwRelationVo> queryList(HdOrderFlwRelationBo bo);

    /**
     * 新增订单流程关系
     */
    Boolean insertByBo(HdOrderFlwRelationBo bo);

    /**
     * 修改订单流程关系
     */
    Boolean updateByBo(HdOrderFlwRelationBo bo);

    /**
     * 校验并批量删除订单流程关系信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
