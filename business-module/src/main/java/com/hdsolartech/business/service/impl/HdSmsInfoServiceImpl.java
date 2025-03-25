package com.hdsolartech.business.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.common.enums.SmsInfoSourceEnum;
import com.hdsolartech.business.domain.HdSmsInfo;
import com.hdsolartech.business.domain.bo.HdSmsInfoBo;
import com.hdsolartech.business.domain.vo.HdSmsInfoVo;
import com.hdsolartech.business.domain.vo.HdSmsTemplateInfoVo;
import com.hdsolartech.business.mapper.HdSmsInfoMapper;
import com.hdsolartech.business.service.IHdSmsInfoService;
import com.hdsolartech.business.service.IHdSmsTemplateInfoService;
import com.hdsolartech.business.service.ISmsTemplateVariables;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.vo.UserSocial;
import org.dromara.common.core.service.UserService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.hdsolartech.business.common.constant.SmsInfoConstants.*;

/**
 * 消息历史记录信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class HdSmsInfoServiceImpl implements IHdSmsInfoService {

    private final HdSmsInfoMapper baseMapper;
    private final IHdSmsTemplateInfoService templateInfoService;
    private final ISmsTemplateVariables smsTemplateVariables;
    private final UserService userService;

    public static final String KEY_PARAMS_USER_SOCIALS = "userSocials";

    /**
     * 查询消息历史记录信息
     */
    @Override
    public HdSmsInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询消息历史记录信息列表
     */
    @Override
    public TableDataInfo<HdSmsInfoVo> queryPageList(HdSmsInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdSmsInfo> lqw = buildQueryWrapper(bo);
        Page<HdSmsInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询消息历史记录信息列表
     */
    @Override
    public List<HdSmsInfoVo> queryList(HdSmsInfoBo bo) {
        LambdaQueryWrapper<HdSmsInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdSmsInfo> buildQueryWrapper(HdSmsInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdSmsInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getSmsTemplateId() != null, HdSmsInfo::getSmsTemplateId, bo.getSmsTemplateId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdSmsInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), HdSmsInfo::getCode, bo.getCode());
        lqw.eq(bo.getProjectId() != null, HdSmsInfo::getProjectId, bo.getProjectId());
        lqw.like(StringUtils.isNotBlank(bo.getProjectName()), HdSmsInfo::getProjectName, bo.getProjectName());
        lqw.eq(bo.getOrderId() != null, HdSmsInfo::getOrderId, bo.getOrderId());
        lqw.like(StringUtils.isNotBlank(bo.getOrderName()), HdSmsInfo::getOrderName, bo.getOrderName());
        lqw.eq(StringUtils.isNotBlank(bo.getProcId()), HdSmsInfo::getProcId, bo.getProcId());
        lqw.like(StringUtils.isNotBlank(bo.getProcName()), HdSmsInfo::getProcName, bo.getProcName());
        lqw.eq(StringUtils.isNotBlank(bo.getSource()), HdSmsInfo::getSource, bo.getSource());
        lqw.eq(bo.getSourceUserId() != null, HdSmsInfo::getSourceUserId, bo.getSourceUserId());
        lqw.eq(bo.getUserId() != null, HdSmsInfo::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), HdSmsInfo::getContent, bo.getContent());
        lqw.eq(bo.getIsRead() != null, HdSmsInfo::getIsRead, bo.getIsRead());
        lqw.eq(StringUtils.isNotBlank(bo.getSmsParams()), HdSmsInfo::getParams, bo.getParams());
        lqw.eq(StringUtils.isNotBlank(bo.getTemplateContent()), HdSmsInfo::getTemplateContent, bo.getTemplateContent());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            HdSmsInfo::getCreateTime, params.get("beginTime"), params.get("endTime"));
        lqw.like(StringUtils.isNotBlank(bo.getProcNodeName()), HdSmsInfo::getProcNodeName, bo.getProcNodeName());
        lqw.orderByAsc(HdSmsInfo::getIsRead);
        lqw.orderByDesc(HdSmsInfo::getCreateTime);
        return lqw;
    }

    /**
     * 新增消息历史记录信息
     */
    @Override
    public Boolean insertByBo(HdSmsInfoBo bo) {
        HdSmsInfo add = MapstructUtils.convert(bo, HdSmsInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改消息历史记录信息
     */
    @Override
    public Boolean updateByBo(HdSmsInfoBo bo) {
        HdSmsInfo update = MapstructUtils.convert(bo, HdSmsInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdSmsInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除消息历史记录信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean insertByDingTalk(SmsInfoSourceEnum smsInfoSourceEnum, String templateIdsStr, String toUserIdsStr, Map<String, Object> params, Object... otherParams) {
        List<HdSmsInfo> hdSmsTemplateInfoBos = new ArrayList<>();
        if (StrUtil.isEmpty(templateIdsStr) || StrUtil.isEmpty(toUserIdsStr)) {
            log.error("保存消息,idsStr或toUser参数为空,请检查调用方法的传参");
            return false;
        }
        String[] templateIdArray = templateIdsStr.split(StringUtils.SEPARATOR);
        // String[]转换为Long[]
        Long[] templateIdsLongArray = new Long[templateIdArray.length];
        for (int i = 0; i < templateIdArray.length; i++) {
            templateIdsLongArray[i] = Long.valueOf(templateIdArray[i]);
        }
        List<HdSmsTemplateInfoVo> templateInfoVos = templateInfoService.queryByIds(templateIdsLongArray);

        for (HdSmsTemplateInfoVo templateVo : templateInfoVos) {
            List<HdSmsInfo> list = constructHdSmsInfoList(smsInfoSourceEnum.getSource(), templateVo.getContent(), toUserIdsStr, null, templateVo.getId(), params, otherParams);
            hdSmsTemplateInfoBos.addAll(list);
        }
        return baseMapper.insertBatch(hdSmsTemplateInfoBos);
    }

    @Override
    public Boolean insertByDingTalk(SmsInfoSourceEnum smsInfoSourceEnum, String content, String toUser, Long fromUser, Map<String, Object> params) {
        List<HdSmsInfo> list = constructHdSmsInfoList(smsInfoSourceEnum.getSource(), content, toUser, fromUser, null, params);
        return baseMapper.insertBatch(list);
    }

    @Override
    public Boolean markRead(Collection<Long> ids) {
        List<HdSmsInfo> list = baseMapper.selectBatchIds(ids);
        list.forEach(h -> h.setIsRead(1L));
        return baseMapper.updateBatchById(list);
    }

    @Override
    public Boolean markAllRead(Long id) {
        if (Objects.isNull(id)) {
            log.error("用户消息全部已读发生错误,用户参数为空");
            return false;
        }
        HdSmsInfoBo bo = new HdSmsInfoBo();
        bo.setUserId(id);
        bo.setIsRead(0L);
        LambdaQueryWrapper<HdSmsInfo> lqw = buildQueryWrapper(bo);
        List<HdSmsInfo> toMarkReadList = baseMapper.selectList(lqw);
        toMarkReadList.forEach(h -> h.setIsRead(1L));
        // 因mybatis-plus对于参数空直接报错不友好，此处添加处理
        if (!toMarkReadList.isEmpty()) {
            return baseMapper.updateBatchById(toMarkReadList);
        } else {
            return false;
        }
    }

    private List<HdSmsInfo> constructHdSmsInfoList(String source, String content, String toUser, Long fromUser, Long templateId, Map<String, Object> params, Object... otherParams) {
        List<HdSmsInfo> hdSmsTemplateInfoBos = new ArrayList<>();
        if (StrUtil.isEmpty(content) || StrUtil.isEmpty(toUser)) {
            log.error("content或toUser参数为空,请检查调用方法的传参");
        }
        HdSmsTemplateInfoVo templateInfoVo = new HdSmsTemplateInfoVo();
        if (Objects.nonNull(templateId)) {
            templateInfoVo = templateInfoService.queryById(templateId);
        }
        String[] userIdArray = toUser.split(StringUtils.SEPARATOR);
        for (String userId : userIdArray) {
            HdSmsInfo hdSmsInfoBo = new HdSmsInfo();
            hdSmsInfoBo.setSource(source);
            String userName = null;
            if(Objects.nonNull(otherParams) && otherParams.length > 0){
                List<UserSocial> userSocials = (List<UserSocial>) otherParams[0];
                Optional<UserSocial> foundUserSocial = userSocials.stream()
                    .filter(userSocial -> userId.equals(userSocial.getUserId()))
                    .findFirst();
                if(foundUserSocial.isPresent()) {
                    UserSocial userSocial = foundUserSocial.get();
                    userName = userSocial.getNickName();
                }
            }
            if(StrUtil.isEmpty(userName)) {
                try{
                    // TODO 此处待优化，现为总是查询用户昵称，优化为根据需要查询用户昵称
                    userName = userService.selectNicknameById(Long.valueOf(userId));
                }catch (NumberFormatException ex) {
                    log.error("用户id解析错误，期待类型Long，实际入参: {}", userId);
                }
            }
            String contentFormated = smsTemplateVariables.format(content, params, userName);
            hdSmsInfoBo.setContent(contentFormated);
            hdSmsInfoBo.setUserId(Long.valueOf(userId));
            hdSmsInfoBo.setIsRead(0L);
            hdSmsInfoBo.setSourceUserId(fromUser);
            hdSmsInfoBo.setSmsTemplateId(templateId);
            hdSmsInfoBo.setName(templateInfoVo.getName());
            hdSmsInfoBo.setCode(templateInfoVo.getCode());
            hdSmsInfoBo.setTemplateContent(templateInfoVo.getContent());
            if (Objects.nonNull(params)) {
                hdSmsInfoBo.setSmsParams(params.toString());
                String projectStr = MapUtil.getStr(params, PROJECT_ID);
                String orderStr = MapUtil.getStr(params, ORDER_ID);
                if (StrUtil.isNotEmpty(projectStr)) {
                    String[] projectArray = projectStr.split(StringUtils.SEPARATOR);
                    if (projectArray.length == 2) {
                        hdSmsInfoBo.setProjectName(projectArray[1]);
                        try {
                            hdSmsInfoBo.setProjectId(Long.valueOf(projectArray[0]));
                        } catch (NumberFormatException ex) {
                            log.error("projectId: {} 解析错误,忽略属性入库保存,期待类型Long", projectArray[0]);
                        }
                    } else {
                        log.error("projectId: {} 解析错误,忽略属性入库保存,期待类型:projectId,projectName'", projectStr);
                    }
                }
                if (StrUtil.isNotEmpty(orderStr)) {
                    String[] orderArray = orderStr.split(StringUtils.SEPARATOR);
                    if (orderArray.length == 2) {
                        hdSmsInfoBo.setOrderName(orderArray[1]);
                        try {
                            hdSmsInfoBo.setOrderId(Long.valueOf(orderArray[0]));
                        } catch (NumberFormatException ex) {
                            log.error("orderId: {} 解析错误,忽略属性入库保存,期待类型Long", orderArray[0]);
                        }
                    } else {
                        log.error("orderId: {} 解析错误,忽略属性入库保存,期待类型:orderId,orderName'", orderStr);
                    }
                }
                hdSmsInfoBo.setProcName(MapUtil.getStr(params, PROCESS_DEF_NAME));
                hdSmsInfoBo.setProcId(MapUtil.getStr(params, PROCESS_DEF_ID));
                hdSmsInfoBo.setProcNodeName(MapUtil.getStr(params, PROCESS_NODE_NAME));
            }
            hdSmsTemplateInfoBos.add(hdSmsInfoBo);
        }
        return hdSmsTemplateInfoBos;
    }
}
