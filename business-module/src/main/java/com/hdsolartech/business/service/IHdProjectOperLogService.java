package com.hdsolartech.business.service;


import com.hdsolartech.business.domain.vo.HdProjectOperLogVo;
import com.hdsolartech.business.domain.bo.HdProjectOperLogBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目操作信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-04
 */
public interface IHdProjectOperLogService {

    /**
     * 查询项目操作信息
     */
    HdProjectOperLogVo queryById(Long id);

    /**
     * 查询项目操作信息列表
     */
    TableDataInfo<HdProjectOperLogVo> queryPageList(HdProjectOperLogBo bo, PageQuery pageQuery);

    /**
     * 查询项目操作信息列表
     */
    List<HdProjectOperLogVo> queryList(HdProjectOperLogBo bo);

    /**
     * 新增项目操作信息
     */
    Boolean insertByBo(HdProjectOperLogBo bo);

    /**
     * 修改项目操作信息
     */
    Boolean updateByBo(HdProjectOperLogBo bo);

    /**
     * 校验并批量删除项目操作信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 记录操作日志
     * @param category 类别
     * @param ProjectId 项目id
     * @param orderId 订单id
     * @param oper  操作类型
     * @param desc  操作明细
     */
    void recordLog(Long category, Long ProjectId, Long orderId, String oper,Long result,String desc);

}
