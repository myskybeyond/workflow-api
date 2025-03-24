package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdProjectDeliveryInfo;
import com.hdsolartech.business.domain.vo.HdProjectDeliveryInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectDeliveryInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目发货信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-19
 */
public interface IHdProjectDeliveryInfoService {

    /**
     * 查询项目发货信息
     */
    HdProjectDeliveryInfoVo queryById(Long id);

    /**
     * 查询项目发货信息列表
     */
    TableDataInfo<HdProjectDeliveryInfoVo> queryPageList(HdProjectDeliveryInfoBo bo, PageQuery pageQuery);

    /**
     * 查询项目发货信息列表
     */
    List<HdProjectDeliveryInfoVo> queryList(HdProjectDeliveryInfoBo bo);

    /**
     * 新增项目发货信息
     */
    Boolean insertByBo(HdProjectDeliveryInfoBo bo);

    /**
     * 修改项目发货信息
     */
    Boolean updateByBo(HdProjectDeliveryInfoBo bo);

    /**
     * 校验并批量删除项目发货信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 同步发送流程
     */
    Boolean sync(HdProjectDeliveryInfoBo bo);
}
