package org.dromara.common.core.service;

/**
 * 通用 用户服务
 *
 * @author Lion Li
 */
public interface UserService {

    /**
     * 通过用户ID查询用户账户
     *
     * @param userId 用户ID
     * @return 用户账户
     */
    String selectUserNameById(Long userId);

    /**
     * 通过用户ID查询用户账户
     *
     * @param userId 用户ID
     * @return 用户账户
     */
    String selectNicknameById(Long userId);

    /**
     * 通过用户账号查询userId
     *
     * @param userName 用户名称
     * @return 用户账户
     */
    Long selectUserIdByUserName(String userName);


    /**
     * 通过用户id查询dept
     * @param userId
     * @return
     */
    String selectDeptByUserId(Long userId);

}
