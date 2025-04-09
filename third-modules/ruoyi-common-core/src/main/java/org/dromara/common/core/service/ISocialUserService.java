package org.dromara.common.core.service;

import org.dromara.common.core.domain.vo.UserSocial;

import java.util.List;
import java.util.Map;

/**
 * @author 86137
 * @version 1.0
 * <p>
 * Brief description of the class.
 * @date 2025/1/20
 */
public interface ISocialUserService {

    /**
     * 查询组织下用户id
     * @param toUsers 系统用户id,多个用户以英文逗号分割
     * @return 钉钉组织下的用户id
     */
    Map<String, String> queryByUserIdsGroupBySource(String toUsers);

    /**
     * 查询组织下用户id
     * @param toUsers 系统用户id,多个用户以英文逗号分割
     * @return 组织下的用户对像
     */
    List<UserSocial> queryByUserIdsUnGroup(String toUsers);
}
