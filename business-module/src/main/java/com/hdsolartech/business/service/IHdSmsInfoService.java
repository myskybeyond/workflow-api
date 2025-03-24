package com.hdsolartech.business.service;

import com.hdsolartech.business.common.enums.SmsInfoSourceEnum;
import com.hdsolartech.business.domain.bo.HdSmsInfoBo;
import com.hdsolartech.business.domain.vo.HdSmsInfoVo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 消息历史记录信息Service接口
 *
 * @author Lion Li
 * @date 2024-04-30
 */
public interface IHdSmsInfoService {

    /**
     * 查询消息历史记录信息
     */
    HdSmsInfoVo queryById(Long id);

    /**
     * 查询消息历史记录信息列表
     */
    TableDataInfo<HdSmsInfoVo> queryPageList(HdSmsInfoBo bo, PageQuery pageQuery);

    /**
     * 查询消息历史记录信息列表
     */
    List<HdSmsInfoVo> queryList(HdSmsInfoBo bo);

    /**
     * 新增消息历史记录信息
     */
    Boolean insertByBo(HdSmsInfoBo bo);

    /**
     * 修改消息历史记录信息
     */
    Boolean updateByBo(HdSmsInfoBo bo);

    /**
     * 校验并批量删除消息历史记录信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 新增消息模板信息
     *
     * @param source 来源
     * @param templateIdsStr 消息模板id[]
     * @param toUserIdsStr 接收用户id[]
     * @param params 消息参数
     * @param otherParams 其他参数
     */
    Boolean insertByDingTalk(SmsInfoSourceEnum source, String templateIdsStr, String toUserIdsStr, Map<String, Object> params, Object... otherParams);

    /**
     * @param source     来源
     * @param content    发送内容
     * @param toUser     接收用户
     * @param fromUser   来源用户
     * @param params     消息参数
     * @return
     */
    Boolean insertByDingTalk(SmsInfoSourceEnum source, String content, String toUser, Long fromUser, Map<String, Object> params);

    /**
     * 标记为已读
     * @param ids 消息主键
     * @return
     */
    Boolean markRead(Collection<Long> ids);

    /**
     * 用户消息全已读
     * @param id 用户id
     * @return
     */
    Boolean markAllRead(Long id);
}
