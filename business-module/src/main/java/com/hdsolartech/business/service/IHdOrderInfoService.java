package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdOrderInfo;
import com.hdsolartech.business.domain.vo.HdOrderInfoVo;
import com.hdsolartech.business.domain.bo.HdOrderInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 销售订单信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface IHdOrderInfoService {
    /**
     * 查询领导工作台首页数据信息
     * @return
     */
    Map queryIndexInfo();
    /**
     * 查询销售订单信息
     */
    HdOrderInfoVo queryById(Long id);

    /**
     * 查询销售订单信息列表
     */
    TableDataInfo<HdOrderInfoVo> queryPageList(HdOrderInfoBo bo, PageQuery pageQuery);

    /**
     * 查询销售订单信息列表
     */
    List<HdOrderInfoVo> queryList(HdOrderInfoBo bo);

    /**
     * 新增销售订单信息
     */
    Boolean insertByBo(HdOrderInfoBo bo);

    /**
     * 修改销售订单信息
     */
    Boolean updateByBo(HdOrderInfoBo bo);

    /**
     * 校验并批量删除销售订单信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);




    /**
     * 修改销售订单进度信息
     */
    Boolean progress(HdOrderInfoBo bo);

    /**
     * 获取未完成的订单
     * @return
     */
    List<Long>  undoneList();

    /**
     * 查询销售订单信息列表
     */
    List<HdOrderInfoVo> queryMyList(HdOrderInfoBo bo);
}
