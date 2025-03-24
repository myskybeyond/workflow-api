package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.bo.HdSmsTemplateInfoBo;
import com.hdsolartech.business.domain.bo.TestMsgSendBo;
import com.hdsolartech.business.domain.vo.HdSmsTemplateInfoVo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 消息模板信息Service接口
 *
 * @author Lion Li
 * @date 2024-04-30
 */
public interface IHdSmsTemplateInfoService {

    /**
     * 查询消息模板信息
     */
    HdSmsTemplateInfoVo queryById(Long id);

    /**
     * 查询消息模板信息列表
     */
    TableDataInfo<HdSmsTemplateInfoVo> queryPageList(HdSmsTemplateInfoBo bo, PageQuery pageQuery);

    /**
     * 查询消息模板信息列表
     */
    List<HdSmsTemplateInfoVo> queryList(HdSmsTemplateInfoBo bo);

    /**
     * 新增消息模板信息
     */
    Boolean insertByBo(HdSmsTemplateInfoBo bo);

    /**
     * 修改消息模板信息
     */
    Boolean updateByBo(HdSmsTemplateInfoBo bo);

    /**
     * 校验并批量删除消息模板信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 查询消息模板信息
     */
    List<HdSmsTemplateInfoVo> queryByIds(Long[] ids);

    /**
     * 修改消息模板状态
     * @param id 消息模板id
     * @param status 0-正常 1-停用
     */
    Boolean changeStatus(Long id, Long status);

    /**
     * 测试发送
     * @param bo
     */
    void testSend(TestMsgSendBo bo);
}
