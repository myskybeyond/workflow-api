package com.hdsolartech.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdSmsTemplateInfo;
import com.hdsolartech.business.domain.bo.HdSmsTemplateInfoBo;
import com.hdsolartech.business.domain.bo.TestMsgSendBo;
import com.hdsolartech.business.domain.vo.HdSmsTemplateInfoVo;
import com.hdsolartech.business.listener.GlobalEventPublisher;
import com.hdsolartech.business.listener.SendDingTalkMsgEvent;
import com.hdsolartech.business.mapper.HdSmsTemplateInfoMapper;
import com.hdsolartech.business.service.IHdSmsTemplateInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 消息模板信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class HdSmsTemplateInfoServiceImpl implements IHdSmsTemplateInfoService {

    private final HdSmsTemplateInfoMapper baseMapper;
    private final GlobalEventPublisher eventPublisher;

    /**
     * 查询消息模板信息
     */
    @Override
    public HdSmsTemplateInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询消息模板信息列表
     */
    @Override
    public TableDataInfo<HdSmsTemplateInfoVo> queryPageList(HdSmsTemplateInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdSmsTemplateInfo> lqw = buildQueryWrapper(bo);
        Page<HdSmsTemplateInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询消息模板信息列表
     */
    @Override
    public List<HdSmsTemplateInfoVo> queryList(HdSmsTemplateInfoBo bo) {
        LambdaQueryWrapper<HdSmsTemplateInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdSmsTemplateInfo> buildQueryWrapper(HdSmsTemplateInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdSmsTemplateInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdSmsTemplateInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), HdSmsTemplateInfo::getCode, bo.getCode());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), HdSmsTemplateInfo::getContent, bo.getContent());
        lqw.eq(bo.getStatus() != null, HdSmsTemplateInfo::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增消息模板信息
     */
    @Override
    public Boolean insertByBo(HdSmsTemplateInfoBo bo) {
        bo.setStatus(0L);
        HdSmsTemplateInfo add = MapstructUtils.convert(bo, HdSmsTemplateInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改消息模板信息
     */
    @Override
    public Boolean updateByBo(HdSmsTemplateInfoBo bo) {
        HdSmsTemplateInfo update = MapstructUtils.convert(bo, HdSmsTemplateInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdSmsTemplateInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除消息模板信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<HdSmsTemplateInfoVo> queryByIds(Long[] ids) {
        return baseMapper.selectVoBatchIds(Arrays.asList(ids));
    }

    @Override
    public Boolean changeStatus(Long id, Long status) {
        if(ObjectUtil.isEmpty(id) || ObjectUtil.isEmpty(status)) {
            log.error("消息模板管理->修改状态失败,参数为空");
            throw new RuntimeException("请求参数为空");
        }
        HdSmsTemplateInfo smsTemplateInfo = baseMapper.selectById(id);
        smsTemplateInfo.setStatus(status);
        int effectRows = baseMapper.updateById(smsTemplateInfo);
        return effectRows > 0;
    }

    @Override
    public void testSend(TestMsgSendBo bo) {
        // 通知admin模块去发消息
        SendDingTalkMsgEvent sendDingTalkMsgEvent = new SendDingTalkMsgEvent(this,bo);
        eventPublisher.publishEvent(sendDingTalkMsgEvent);
    }
}
