package com.myskybeyond.web.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.vo.UserSocial;
import org.dromara.common.core.service.IDingTalkUserService;
import org.dromara.common.core.service.ISocialUserService;
import org.dromara.system.domain.vo.SysSocialVo;
import org.dromara.system.service.ISysSocialService;
import org.dromara.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.myskybeyond.dingtalk.sevice.DingTalkServiceApi.DING_TALK;


/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/6/6
 */
@Service
@Slf4j
@Data
@Deprecated
public class DingTalkUserService implements IDingTalkUserService, ISocialUserService {
    private final ISysSocialService sysSocialService;
    private final ISysUserService sysUserService;

    @Override
    public Map<String, String> queryByUserIdsGroupBySource(String toUsers) {
        List<SysSocialVo> userList = queryDingTalkSocials(toUsers);
        if (userList != null) {
            Map<String, String> map = userList.stream().collect(Collectors.groupingBy(SysSocialVo::getSource, Collectors.mapping(SysSocialVo::getDingtalkUserId, Collectors.joining(","))));
            log.info("系统用户: {} 换取三方应用用户id的结果为(按钉钉组织分组): {}", toUsers, map);
            return map;
        } else {
            return null;
        }
    }

    @Override
    public List<UserSocial> queryByUserIdsUnGroup(String toUsers) {
        List<SysSocialVo> userList = queryDingTalkSocials(toUsers);
        List<UserSocial> result = new ArrayList<>();
        if (userList != null) {
            userList.forEach(u -> {
                result.add(new UserSocial(u.getSource(), String.valueOf(u.getUserId()), u.getDingtalkUserId(), u.getSystemNickName()));
            });
        }
        log.info("系统用户: {} 换取三方应用用户id的结果为(未分组): {}", toUsers, result);
        return result;
    }

    private List<SysSocialVo> queryDingTalkSocials(String toUsers) {
        List<SysSocialVo> userList = new ArrayList<>();
        if (StrUtil.isEmpty(toUsers)) {
            log.error("用户参数: {} 为空", toUsers);
            return null;
        }
        String[] ids = toUsers.split(",");
        for (String id : ids) {
            List<SysSocialVo> sysSocialVos = sysSocialService.queryListByUserId(Long.valueOf(id));
            if (!sysSocialVos.isEmpty()) {
                userList.addAll(sysSocialVos);
            } else {
                log.warn("用户id:{} 未绑定第三方平台", id);
            }
        }
        List<SysSocialVo> dingTalkSocials = new ArrayList<>();
        userList.forEach(u -> {
            if (u.getSource().startsWith(DING_TALK)) {
                if (StrUtil.isEmpty(u.getDingtalkUserId())) {
                    log.error("系统错误-系统用户: {} 没有找到钉钉组织的用户id,请修复sys_social表数据", u.getUserId());
                } else {
                    dingTalkSocials.add(u);
                }
            } else {
                log.debug("钉钉social用户处理跳过三方平台: {}", u.getSource());
            }
        });
        return dingTalkSocials;
    }
}
