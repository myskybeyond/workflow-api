package org.dromara.system.domain.vo;

import lombok.Data;

import java.util.Set;

/**
 * 登录用户信息
 *
 * @author Michelle.Chung
 */
@Data
public class UserInfoVo {

    /**
     * 用户基本信息
     */
    private SysUserVo user;

    /**
     * 菜单权限
     */
    private Set<String> permissions;

    /**
     * 角色权限
     */
    private Set<String> roles;
    /**
     * 岗位名臣列表
     */
    private String posts;

    /**
     * 是否需要修改密码
     */
    private boolean forceResetPass;

}
