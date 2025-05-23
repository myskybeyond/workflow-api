package com.myskybeyond.flowable.service;

import com.myskybeyond.flowable.core.domain.ProcessQuery;
import com.myskybeyond.flowable.domain.bo.WfStartAndCopyBo;
import com.myskybeyond.flowable.domain.bo.WfTaskBo;
import com.myskybeyond.flowable.domain.vo.WfTaskVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.engine.runtime.ProcessInstance;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author KonBAI
 * @createTime 2022/3/10 00:12
 */
public interface IWfTaskService {

    /**
     * 审批任务
     *
     * @param task 请求实体参数
     */
    void complete(WfTaskBo task);

    /**
     * 拒绝任务
     *
     * @param taskBo
     */
    void taskReject(WfTaskBo taskBo);


    /**
     * 退回任务
     *
     * @param bo 请求实体参数
     */
    void taskReturn(WfTaskBo bo);

    /**
     * 获取所有可回退的节点
     *
     * @param bo
     * @return
     */
    List<FlowElement> findReturnTaskList(WfTaskBo bo);

    /**
     * 删除任务
     *
     * @param bo 请求实体参数
     */
    void deleteTask(WfTaskBo bo);

    /**
     * 认领/签收任务
     *
     * @param bo 请求实体参数
     */
    void claim(WfTaskBo bo);

    /**
     * 取消认领/签收任务
     *
     * @param bo 请求实体参数
     */
    void unClaim(WfTaskBo bo);

    /**
     * 委派任务
     *
     * @param bo 请求实体参数
     */
    void delegateTask(WfTaskBo bo);


    /**
     * 转办任务
     *
     * @param bo 请求实体参数
     */
    void transferTask(WfTaskBo bo);

    /**
     * 取消申请
     * @param bo
     * @return
     */
    void stopProcess(WfTaskBo bo);

    /**
     * 撤回流程
     * @param bo
     * @return
     */
    void revokeProcess(WfTaskBo bo);

    /**
     * 获取流程过程图
     * @param processId
     * @return
     */
    InputStream diagram(String processId);

    /**
     * 获取流程变量
     * @param taskId 任务ID
     * @return 流程变量
     */
    Map<String, Object> getProcessVariables(String taskId);

    /**
     * 启动第一个任务
     * @param processInstance 流程实例
     * @param variables 流程参数
     */
    void startFirstTask(ProcessInstance processInstance, Map<String, Object> variables);

    /**
     * 查询任务列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageTaskList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 关闭任务
     * @param bo
     */
    void closeTask(WfTaskBo bo);

    /**
     * 挂起任务
     * @param bo
     */
    void assumeTask(WfTaskBo bo);

    /**
     * 激活任务
     * @param bo
     */
    void activeTask(WfTaskBo bo);

    /**
     * 启动第一个任务
     * @param processInstance 流程实例
     * @param form 流程参数
     */
    void startFirstTask(ProcessInstance processInstance, WfStartAndCopyBo form);
}
