package org.dromara.common.core.service;

import org.dromara.common.core.domain.vo.UserSocial;

import java.util.List;
import java.util.Map;

/**
 * @author 86137
 * @version 1.0
 * 系统用户id换取钉钉组织下用户id
 * @date 2024/6/6
 */
@Deprecated
public interface IDingTalkUserService {

    /**
     * 查询钉钉组织下用户id
     * @param toUsers 系统用户id,多个用户以英文逗号分割
     * @return 钉钉组织下的用户id
     */
    Map<String, String> queryByUserIdsGroupBySource(String toUsers);

    /**
     * 查询钉钉组织下用户id
     * @param toUsers 系统用户id,多个用户以英文逗号分割
     * @return 钉钉组织下的用户对昂
     */
    List<UserSocial> queryByUserIdsUnGroup(String toUsers);
}
