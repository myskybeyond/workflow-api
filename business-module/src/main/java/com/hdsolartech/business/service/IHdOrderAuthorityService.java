package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.bo.HdOrderAuthorityBo;
import com.hdsolartech.business.domain.bo.HdProjectAuthorityBo;
import com.hdsolartech.business.domain.vo.HdOrderAuthorityVo;
import com.hdsolartech.business.domain.vo.HdProjectAuthorityVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 订单权限信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface IHdOrderAuthorityService {

    /**
     * 查询订单权限信息
     */
    HdOrderAuthorityVo queryById(Long id);

    /**
     * 查询订单权限信息列表
     */
    TableDataInfo<HdOrderAuthorityVo> queryPageList(HdOrderAuthorityBo bo, PageQuery pageQuery);

    /**
     * 查询订单权限信息列表
     */
    List<HdOrderAuthorityVo> queryList(HdOrderAuthorityBo bo);

    /**
     * 新增订单权限信息
     */
    Boolean insertByBo(HdOrderAuthorityBo bo);

    /**
     * 修改订单权限信息
     */
    Boolean updateByBo(HdOrderAuthorityBo bo);

    /**
     * 校验并批量删除订单权限信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询用户权限vo
     * @param bo
     * @return
     */
    HdOrderAuthorityVo queryByBo(HdOrderAuthorityBo bo );

}
