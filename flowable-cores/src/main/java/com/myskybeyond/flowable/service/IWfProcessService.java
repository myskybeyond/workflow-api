package com.myskybeyond.flowable.service;

import com.myskybeyond.flowable.core.FormConfOfFormCreate;
import com.myskybeyond.flowable.core.domain.ProcessQuery;
import com.myskybeyond.flowable.domain.bo.WfStartAndCopyBo;
import com.myskybeyond.flowable.domain.vo.WfDefinitionVo;
import com.myskybeyond.flowable.domain.vo.WfDetailVo;
import com.myskybeyond.flowable.domain.vo.WfTaskVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.flowable.bpmn.model.FlowElement;

import java.util.List;
import java.util.Map;

/**
 * @author KonBAI
 * @createTime 2022/3/24 18:57
 */
public interface IWfProcessService {

    /**
     * 查询可发起流程列表
     * @param pageQuery 分页参数
     * @return
     */
    TableDataInfo<WfDefinitionVo> selectPageStartProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询可发起流程列表
     */
    List<WfDefinitionVo> selectStartProcessList(ProcessQuery processQuery);

    /**
     * 查询我的流程列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageOwnProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询我的流程列表
     */
    List<WfTaskVo> selectOwnProcessList(ProcessQuery processQuery);

    /**
     * 查询代办任务列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageTodoProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询代办任务列表
     */
    List<WfTaskVo> selectTodoProcessList(ProcessQuery processQuery);

    /**
     * 查询待签任务列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageClaimProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询待签任务列表
     */
    List<WfTaskVo> selectClaimProcessList(ProcessQuery processQuery);

    /**
     * 查询已办任务列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageFinishedProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 查询已办任务列表
     */
    List<WfTaskVo> selectFinishedProcessList(ProcessQuery processQuery);

    /**
     * 查询流程部署关联表单信息
     * @param definitionId 流程定义ID
     * @param deployId 部署ID
     */
    FormConfOfFormCreate selectFormContent(String definitionId, String deployId, String procInsId);

    /**
     * 启动流程实例
     * @param procDefId 流程定义ID
     * @param variables 扩展参数
     */
    void startProcessByDefId(String procDefId, Map<String, Object> variables);

    /**
     * 删除流程实例
     */
    void deleteProcessByIds(String[] instanceIds);


    /**
     * 读取xml文件
     * @param processDefId 流程定义ID
     */
    String queryBpmnXmlById(String processDefId);


    /**
     * 查询流程任务详情信息
     * @param procInsId 流程实例ID
     * @param taskId 任务ID
     */
    WfDetailVo queryProcessDetail(String procInsId, String taskId);

    /**
     * 查询流程部署关联表单信息
     * @param definitionKey 流程定义Key
     */
    FormConfOfFormCreate selectFormContent(String definitionKey, String procInsId);

    /**
     * 查询流程列表
     * @param pageQuery 分页参数
     */
    TableDataInfo<WfTaskVo> selectPageProcessList(ProcessQuery processQuery, PageQuery pageQuery);

    /**
     * 关闭或激活流程实例
     * @param processInstanceId 流程实例ID
     * @param stateCode 状态（active:激活 suspended:挂起）
     */
    void updateState(String processInstanceId, String stateCode);

    /**
     * 查询流程实例的用户任务节点
     * @param processInstanceId 流程实例ID
     * @return
     */
    List<FlowElement> findJumpPorcessList(String processInstanceId);

    /**
     * 流程实例跳转
     * @param processInstanceId 流程实例ID
     * @param targetKey 用户任务节点key
     */
    void processJump(String processInstanceId, String targetKey);


    /**
     * 通过DefinitionKey启动流程
     * @param procDefKey 流程定义Key
     * @param startAndCopyBo 表单参数和抄送设置
     */
    void startProcessByDefKey(String procDefKey, WfStartAndCopyBo startAndCopyBo);
}
