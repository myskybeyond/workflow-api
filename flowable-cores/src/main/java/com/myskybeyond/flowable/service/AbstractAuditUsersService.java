package com.myskybeyond.flowable.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.myskybeyond.flowable.factory.FlowServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.dromara.system.domain.SysUserDept;
import org.dromara.system.domain.SysUserPost;
import org.dromara.system.domain.SysUserRole;
import org.flowable.engine.history.HistoricProcessInstance;

import java.util.*;

import static com.myskybeyond.flowable.common.constant.ProcessConstants.*;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 审批用户获取
 * @date 2024/5/23
 */
@Slf4j
public abstract class AbstractAuditUsersService extends FlowServiceFactory {

    public Set<String> queryCandidateUserIds(String dataType, List<Long> groups) {
        Set<String> candidateUserIds = new LinkedHashSet<>();
        List<Long> userIds = new ArrayList<>();
        if (PROCESS_CUSTOM_USER_TYPE_USERS.equals(dataType)) {
            // 添加候选用户id
            candidateUserIds.addAll(groups.stream().map(String::valueOf).toList());
        } else if (PROCESS_CUSTOM_USER_TYPE_ROLES.equals(dataType)) {
            // 通过角色id，获取所有用户id集合
            LambdaQueryWrapper<SysUserRole> lqw = Wrappers.lambdaQuery(SysUserRole.class).select(SysUserRole::getUserId).in(SysUserRole::getRoleId, groups);
            userIds = SimpleQuery.list(lqw, SysUserRole::getUserId);
        } else if (PROCESS_CUSTOM_USER_TYPE_DEPTS.equals(dataType)) {
            // 通过部门id，获取所有用户id集合
            LambdaQueryWrapper<SysUserDept> lqw = Wrappers.lambdaQuery(SysUserDept.class).select(SysUserDept::getUserId).in(SysUserDept::getDeptId, groups);
            userIds = SimpleQuery.list(lqw, SysUserDept::getUserId);
        } else if (PROCESS_CUSTOM_USER_TYPE_POSTS.equals(dataType)) {
            // 通过岗位id，获取所有用户id集合
            LambdaQueryWrapper<SysUserPost> lqw = Wrappers.lambdaQuery(SysUserPost.class).select(SysUserPost::getUserId).in(SysUserPost::getPostId, groups);
            userIds = SimpleQuery.list(lqw, SysUserPost::getUserId);
            // add end
        }
        // 添加候选用户id
        userIds.forEach(id -> candidateUserIds.add(String.valueOf(id)));
        return candidateUserIds;
    }

    /**
     * 解析消息通知人
     *
     * @param completeCandidateUsersTypes  接收参数类型 value examples: ROLES&USERS&DEPTS&INITIATOR&POSTS
     * @param completeCandidateUsersValues 接收参数值 value examples: 1,2,3&3,4&34,35&${INITIATOR}&9,33
     * @param processInstanceId            流程实例
     * @return 'userId1,userId2,...'
     */
    public String parseUserIdsOfSpecifiedRule(String completeCandidateUsersTypes, String completeCandidateUsersValues, String processInstanceId) {
        Set<String> userIdSet = new HashSet<>();
        if (StrUtil.isEmpty(completeCandidateUsersTypes) || StrUtil.isEmpty(completeCandidateUsersValues)) {
            log.error("解析消息通知人-入参为空,返回null");
            return null;
        }
        List<Long> userIdList = new ArrayList<>();
        String[] candidateUsersTypesArray = completeCandidateUsersTypes.split("&");
        String[] candidateUsersValuesArray = completeCandidateUsersValues.split("&");
        for (int i = 0; i < candidateUsersTypesArray.length; i++) {
            String candidateUsersType = candidateUsersTypesArray[i];
            if (PROCESS_CUSTOM_USER_TYPE_ROLES.equals(candidateUsersType)) {
                String roleIds = candidateUsersValuesArray[i];
                // 查找角色下的用户
                LambdaQueryWrapper<SysUserRole> lqw = Wrappers.lambdaQuery(SysUserRole.class).select(SysUserRole::getUserId).in(SysUserRole::getRoleId, roleIds);
                userIdList.addAll(SimpleQuery.list(lqw, SysUserRole::getUserId));
            } else if (PROCESS_CUSTOM_USER_TYPE_USERS.equals(candidateUsersType)) {
                String userIds = candidateUsersValuesArray[i];
                // 解析成Long[]
                if (StrUtil.isNotEmpty(userIds)) {
                    String[] userIdStrArray = userIds.split(",");
                    List<Long> userList = new ArrayList<>();
                    for (String s : userIdStrArray) {
                        userList.add(Long.valueOf(s));
                    }
                    userIdList.addAll(userList);
                }
            } else if (PROCESS_CUSTOM_USER_TYPE_DEPTS.equals(candidateUsersType)) {
                String deptIds = candidateUsersValuesArray[i];
                LambdaQueryWrapper<SysUserDept> lqw = Wrappers.lambdaQuery(SysUserDept.class).select(SysUserDept::getUserId).in(SysUserDept::getDeptId, deptIds);
                userIdList.addAll(SimpleQuery.list(lqw, SysUserDept::getUserId));
            } else if (PROCESS_CUSTOM_USER_TYPE_INITIATOR.equals(candidateUsersType)) {
                // 查询流程发起人
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
                Long userId = Long.parseLong(historicProcessInstance.getStartUserId());
                userIdList.add(userId);
            } else if (PROCESS_CUSTOM_USER_TYPE_POSTS.equals(candidateUsersType)) {
                String postIds = candidateUsersValuesArray[i];
                // 查询岗位下的用户
                LambdaQueryWrapper<SysUserPost> lqw = Wrappers.lambdaQuery(SysUserPost.class).select(SysUserPost::getUserId).in(SysUserPost::getPostId, postIds);
                userIdList.addAll(SimpleQuery.list(lqw, SysUserPost::getUserId));
            } else {
                log.error("不支持的接收参数类型: {},跳过处理", candidateUsersType);
            }
        }
        userIdList.forEach(id -> userIdSet.add(String.valueOf(id)));
        String userIdsStr = String.join(",", userIdSet);
        log.info("解析消息通知系统用户id-入参 用户类别：{} 用户值：{} 解析后的消息通知系统用户id为: {}", completeCandidateUsersTypes, completeCandidateUsersValues, userIdsStr);
        return userIdsStr;
    }
}
