package com.myskybeyond.flowable.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.flowable.core.domain.ProcessQuery;
import com.myskybeyond.flowable.domain.WfCategory;
import com.myskybeyond.flowable.domain.bo.WfTaskBo;
import com.myskybeyond.flowable.domain.vo.AbstractWfDeployVo;
import com.myskybeyond.flowable.domain.vo.WfTaskVo;
import com.myskybeyond.flowable.mapper.WfCategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.myskybeyond.flowable.common.constant.ProcessConstants.PROCESS_STATUS_KEY;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 流程查询抽象类
 * @date 2024/5/21
 */
@Slf4j
public abstract class AbstractService extends AbstractModelService {

    public final static String QUERY_SQL_PROCESS_COMMON = "SELECT RES.* , DEF.KEY_ as PROC_DEF_KEY_, DEF.NAME_ as PROC_DEF_NAME_, DEF.VERSION_ as PROC_DEF_VERSION_, DEF.CATEGORY_ as PROC_DEF_CATEGORY_, DEF.DEPLOYMENT_ID_ as DEPLOYMENT_ID_ from ACT_HI_PROCINST RES left outer join ACT_RE_PROCDEF DEF on RES.PROC_DEF_ID_ = DEF.ID_";
    public final static String TOTAL_SQL_PROCESS_COMMON = "SELECT count(1) from ACT_HI_PROCINST RES left outer join ACT_RE_PROCDEF DEF on RES.PROC_DEF_ID_ = DEF.ID_";
    public final static String ORDER_SQL_PROCESS_COMMON = " order by RES.START_TIME_ desc";


    private final WfCategoryMapper wfCategoryMapper;

    public AbstractService(WfCategoryMapper wfCategoryMapper) {
        this.wfCategoryMapper = wfCategoryMapper;
    }

    /**
     * 查询指定类别的本级及下级类别code
     *
     * @param start 类别id
     * @return 字符串 "sales_orders,test_code_1"
     */
    public String categoryLevelAndLowerLevel(String start) {
        if (start == null) {
            return null;
        }
        List<WfCategory> list = wfCategoryMapper.queryConnectByPiror(Long.valueOf(start));
        List<String> idList = list.stream().map(wfCategory -> "'" + wfCategory.getCode() + "'").toList();
        return String.join(StringUtils.SEPARATOR, idList);
    }

    public List<String> categoryLevelAndLowerLevel1(String start) {
        if (start == null) {
            return null;
        }
        List<WfCategory> list = wfCategoryMapper.queryConnectByPiror(Long.valueOf(start));
        return list.stream().map(WfCategory::getCode).toList();
    }

    /**
     * 构建流程定义搜索
     */
    public String buildProcessDefinitionSearch(ProcessQuery process) {
        StringBuilder splitSql = new StringBuilder();
        // 流程标识
        if (StringUtils.isNotBlank(process.getProcessKey())) {
            splitSql.append(" and RES.KEY_ like '%").append(process.getProcessKey()).append("%'");
        }
        // 流程名称
        if (StringUtils.isNotBlank(process.getProcessName())) {
            splitSql.append(" and RES.NAME_ like '%").append(process.getProcessName()).append("%'");
        }
        // 流程分类
        if (StringUtils.isNotBlank(process.getCategory())) {
            String codes = categoryLevelAndLowerLevel(process.getCategory());
            if (StrUtil.isNotEmpty(codes)) {
                splitSql.append(" and RES.CATEGORY_ in (").append(codes).append(")");
            }
        }
        // 流程状态
        if (StringUtils.isNotBlank(process.getState())) {
            if (SuspensionState.ACTIVE.toString().equals(process.getState())) {
                splitSql.append(" and RES.SUSPENSION_STATE_ = ").append(SuspensionState.ACTIVE);
            } else if (SuspensionState.SUSPENDED.toString().equals(process.getState())) {
                splitSql.append(" and RES.SUSPENSION_STATE_ = ").append(SuspensionState.SUSPENDED);
            }
        }
        return splitSql.toString();
    }

    /**
     * 构建历史流程实例搜索
     * and DEF.KEY_ = '236615b4-168c-11ef-' and DEF.NAME_ = '业务流程_1715744550869' and DEF.CATEGORY_ = '1775036683124469761'
     */
    public String buildHistoricProcessInstanceSearch(ProcessQuery process) {
        StringBuilder parameterSql = new StringBuilder();
        Map<String, Object> params = process.getParams();
        // 流程编号
        if (StringUtils.isNotBlank(process.getProcessInstanceId())) {
            parameterSql.append(" and RES.ID_ = '").append(process.getProcessInstanceId()).append("'");
        }
        // 流程标识
        if (StringUtils.isNotBlank(process.getProcessKey())) {
            parameterSql.append(" and DEF.KEY_ = '").append(process.getProcessKey()).append("'");
        }
        // 流程名称
        if (StringUtils.isNotBlank(process.getProcessName())) {
            parameterSql.append(" and DEF.NAME_ like '%").append(process.getProcessName()).append("%'");
        }
        // 流程名称
        if (StringUtils.isNotBlank(process.getCategory())) {
            String codes = categoryLevelAndLowerLevel(process.getCategory());
            if (StrUtil.isNotEmpty(codes)) {
                parameterSql.append(" and DEF.CATEGORY_ in (").append(codes).append(")");
            }
        }
        if (params.get("beginTime") != null && params.get("endTime") != null) {
            parameterSql.append(" and RES.START_TIME_ >= '").append(params.get("beginTime")).append("'");
            parameterSql.append(" and RES.START_TIME_ < '").append(params.get("endTime")).append("'");
        }
        // 流程状态
        if (StringUtils.isNotBlank(process.getState())) {
            parameterSql.append(" and exists (select 1 from act_hi_varinst t2 where RES.ID_ = t2.PROC_INST_ID_ and t2.NAME_='processStatus' and t2.TEXT_='").append(process.getState()).append("')");
        }
        // 流程发起人
        if (StringUtils.isNotBlank(process.getStartUserId())) {
            parameterSql.append(" and RES.START_USER_ID_ = '").append(process.getStartUserId()).append("'");
        }
        return parameterSql.toString();
    }

    /**
     * 构建任务搜索
     */
    public void buildTaskSearch(TaskQuery query, ProcessQuery process) {
        Map<String, Object> params = process.getParams();
        if (StringUtils.isNotBlank(process.getProcessKey())) {
            query.processDefinitionKeyLike("%" + process.getProcessKey() + "%");
        }
        if (StringUtils.isNotBlank(process.getProcessName())) {
            query.processDefinitionNameLike("%" + process.getProcessName() + "%");
        }
        if (params.get("beginTime") != null && params.get("endTime") != null) {
            query.taskCreatedAfter(DateUtils.parseDate(params.get("beginTime")));
            query.taskCreatedBefore(DateUtils.parseDate(params.get("endTime")));
        }
        if (StringUtils.isNotBlank(process.getCategory())) {
            List<String> categories = categoryLevelAndLowerLevel1(process.getCategory());
            query.processCategoryIn(categories);
        }
        if (StringUtils.isNotBlank(process.getTaskId())) {
            query.taskId(process.getTaskId());
        }
    }

    public void buildHistoricTaskInstanceSearch(HistoricTaskInstanceQuery query, ProcessQuery process) {
        Map<String, Object> params = process.getParams();
        if (StringUtils.isNotBlank(process.getProcessKey())) {
            query.processDefinitionKeyLike("%" + process.getProcessKey() + "%");
        }
        if (StringUtils.isNotBlank(process.getProcessName())) {
            query.processDefinitionNameLike("%" + process.getProcessName() + "%");
        }
        if (params.get("beginTime") != null && params.get("endTime") != null) {
            query.taskCompletedAfter(DateUtils.parseDate(params.get("beginTime")));
            query.taskCompletedBefore(DateUtils.parseDate(params.get("endTime")));
        }
        if (StringUtils.isNotBlank(process.getCategory())) {
            List<String> categories = categoryLevelAndLowerLevel1(process.getCategory());
            query.processCategoryIn(categories);
        }
        if (StringUtils.isNotBlank(process.getProcessInstanceId())) {
            query.processInstanceId(process.getProcessInstanceId());
        }
        if (StringUtils.isNotBlank(process.getTaskId())) {
            query.taskId(process.getTaskId());
        }
    }

    public void buildNativeHistoricTaskInstanceSearch(StringBuilder querySql, ProcessQuery process) {
        Map<String, Object> params = process.getParams();
        if (StringUtils.isNotBlank(process.getProcessName())) {
            // 通过关联act_re_procdef表查询
            querySql.append(" and exists (select 1 from act_re_procdef tt1 where tt1.ID_ =RES.PROC_DEF_ID_ and tt1.NAME_ like '%").append(process.getProcessName()).append("%')");
        }
        if (params.get("beginTime") != null && params.get("endTime") != null) {
            querySql.append(" and RES.END_TIME_ >= '").append(params.get("beginTime")).append("'");
            querySql.append(" and RES.END_TIME_ <= '").append(params.get("endTime")).append("'");
        }
        if (StringUtils.isNotBlank(process.getCategory())) {
            String categories = categoryLevelAndLowerLevel(process.getCategory());
            // 通过关联act_re_procdef表查询
            querySql.append(" and exists (select 1 from act_re_procdef tt1 where tt1.ID_ =RES.PROC_DEF_ID_ and tt1.CATEGORY_ in (").append(categories).append("))");
        }
        if (StringUtils.isNotBlank(process.getTaskId())) {
            querySql.append(" and RES.ID_ = '").append(process.getTaskId()).append("'");
        }
    }

    /**
     * 根据category查询流程定义ids
     *
     * @param category 流程分类
     * @return 流程分类集合
     */
    public Set<String> queryProcessIdsByCategory(String category) {
        Set<String> processIds = new HashSet<>();
        if (StrUtil.isNotEmpty(category)) {
            List<String> categoryAndLower = categoryLevelAndLowerLevel1(category);
            List<String> result = wfCategoryMapper.queryIdsByCategory("act_re_procdef", categoryAndLower);
            processIds.addAll(result);
            return processIds;
        }
        return processIds;
    }

    /**
     * 构建流程定义搜索
     */
    public void buildProcessDefinitionSearch(ProcessDefinitionQuery query, ProcessQuery process) {
        // 流程标识
        if (StringUtils.isNotBlank(process.getProcessKey())) {
            query.processDefinitionKeyLike("%" + process.getProcessKey() + "%");
        }
        // 流程名称
        if (StringUtils.isNotBlank(process.getProcessName())) {
            query.processDefinitionNameLike("%" + process.getProcessName() + "%");
        }
        // 流程分类
        if (StringUtils.isNotBlank(process.getCategory())) {
            //!!!pay your attention ... 没有根据category in方法,间接通过processDefIds实现
            Set<String> processDefIds = queryProcessIdsByCategory(process.getCategory());
            // fix：修复分类下不存在流程的情况,不存在用流程分类限制理论上不会重复的
            if (processDefIds.isEmpty()) {
                processDefIds.add(process.getCategory());
            }
            query.processDefinitionIds(processDefIds);
        }
        // 流程状态
        if (StringUtils.isNotBlank(process.getState())) {
            if (SuspensionState.ACTIVE.toString().equals(process.getState())) {
                query.active();
            } else if (SuspensionState.SUSPENDED.toString().equals(process.getState())) {
                query.suspended();
            }
        }
    }

    public void fillFlowDeployInfo(List<Deployment> deployments, String deploymentId, AbstractWfDeployVo vo) {
        // 流程发起人信息
        Optional<Deployment> deploymentOptional = deployments.stream().filter(s -> s.getId().equals(deploymentId)).findFirst();
        if (deploymentOptional.isPresent()) {
            Deployment deployment = deploymentOptional.get();
            vo.setDeploymentTime(deployment.getDeploymentTime());
        }
    }

    public List<Deployment> queryDeployments(List<ProcessDefinition> definitionList) {
        List<String> deploymentIds = new ArrayList<>();
        definitionList.forEach(d -> deploymentIds.add(d.getDeploymentId()));
        return repositoryService.createDeploymentQuery().deploymentIds(deploymentIds).list();
    }

    public TableDataInfo<WfTaskVo> selectPageTaskProcessList(TaskQuery taskQuery, ProcessQuery processQuery, PageQuery pageQuery) {
        Page<WfTaskVo> page = new Page<>();
        // 构建搜索条件
        buildTaskSearch(taskQuery, processQuery);
        page.setTotal(taskQuery.count());
        int offset = pageQuery.getPageSize() * (pageQuery.getPageNum() - 1);
        List<Task> taskList = taskQuery.listPage(offset, pageQuery.getPageSize());
        List<HistoricProcessInstance> historicProcessInstances = queryHisProcessInstances(taskList);
        List<WfTaskVo> flowList = new ArrayList<>();
        for (Task task : taskList) {
            WfTaskVo flowTask = new WfTaskVo();
            // 当前流程信息
            flowTask.setTaskId(task.getId());
            flowTask.setTaskDefKey(task.getTaskDefinitionKey());
            flowTask.setCreateTime(task.getCreateTime());
            flowTask.setProcDefId(task.getProcessDefinitionId());
            flowTask.setTaskName(task.getName());
            flowTask.setProcInsId(task.getProcessInstanceId());
            fillFlowDefInfo(historicProcessInstances, task.getProcessInstanceId(), flowTask);
            // 流程变量
            flowTask.setProcVars(task.getProcessVariables());
            flowTask.setTaskStatus(task.getState());
            flowList.add(flowTask);
        }
        page.setRecords(flowList);
        return TableDataInfo.build(page);
    }

    public List<HistoricProcessInstance> queryHisProcessInstances(List<? extends TaskInfo> flowData) {
        if (flowData.isEmpty()) {
            return new ArrayList<>();
        }
        Set<String> processInstanceIds = new HashSet<>();
        flowData.forEach(d -> processInstanceIds.add(d.getProcessInstanceId()));
        List<HistoricProcessInstance> hisProcessInstanceList = new ArrayList<>();
        if (!processInstanceIds.isEmpty()) {
            hisProcessInstanceList = historyService.createHistoricProcessInstanceQuery()
                .processInstanceIds(processInstanceIds).list();
        }
        return hisProcessInstanceList;
    }

    // 填充流程定义相关属性
    public void fillFlowDefInfo(List<HistoricProcessInstance> historicProcessInstances, String processInstanceId, WfTaskVo flowTask) {
        // 流程发起人信息
        Optional<HistoricProcessInstance> historicProcessInstanceOptional = historicProcessInstances.stream().filter(s -> s.getId().equals(processInstanceId)).findFirst();
        if (historicProcessInstanceOptional.isPresent()) {
            HistoricProcessInstance historicProcessInstance = historicProcessInstanceOptional.get();
            Long userId = Long.parseLong(historicProcessInstance.getStartUserId());
            flowTask.setStartUserId(userId);

            // 流程定义信息
            flowTask.setCategory(historicProcessInstance.getProcessDefinitionCategory());
            flowTask.setProcDefVersion(historicProcessInstance.getProcessDefinitionVersion());
            flowTask.setDeployId(historicProcessInstance.getDeploymentId());
            flowTask.setProcDefName(historicProcessInstance.getProcessDefinitionName());
            flowTask.setProcDefKey(historicProcessInstance.getProcessDefinitionKey());
            flowTask.setProcDefId(historicProcessInstance.getProcessDefinitionId());
        }
    }

    public List<Task> queryHisTasks(List<HistoricProcessInstance> historicProcessInstances) {
        if (historicProcessInstances.isEmpty()) {
            return new ArrayList<>();
        }
        Set<String> processInstanceIds = new HashSet<>();
        historicProcessInstances.forEach(d -> processInstanceIds.add(d.getId()));
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIds).includeIdentityLinks().list();
    }

    public String fillProcessVariables(List<HistoricVariableInstance> hisProcessVariables, String processInstanceId, WfTaskVo taskVo) {
        String processStatus = null;
        Optional<HistoricVariableInstance> historicVariableInstanceOptional = hisProcessVariables.stream().filter(s -> s.getProcessInstanceId().equals(processInstanceId)).findFirst();
        if (historicVariableInstanceOptional.isPresent()) {
            HistoricVariableInstance processStatusVariable = historicVariableInstanceOptional.get();
            // 流程变量状态信息
            if (ObjectUtil.isNotNull(processStatusVariable)) {
                processStatus = Convert.toStr(processStatusVariable.getValue());
            }
            taskVo.setProcessStatus(processStatus);
        }
        return processStatus;
    }

    public List<HistoricVariableInstance> queryProcessInstanceVariables(List<HistoricProcessInstance> historicProcessInstances) {
        if (historicProcessInstances.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> processInstanceIds = new ArrayList<>();
        historicProcessInstances.forEach(d -> processInstanceIds.add(d.getId()));
        StringBuilder sqlBuilder = getStringBuilder(processInstanceIds);
        return historyService.createNativeHistoricVariableInstanceQuery()
            .sql(sqlBuilder.toString())
            .list();
    }

    private static @NotNull StringBuilder getStringBuilder(List<String> processInstanceIds) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM ACT_HI_VARINST WHERE PROC_INST_ID_ IN (");
        for (int i = 0; i < processInstanceIds.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(", ");
            }
            sqlBuilder.append("'");
            sqlBuilder.append(processInstanceIds.get(i));
            sqlBuilder.append("'");
        }
        sqlBuilder.append(") AND NAME_ = '");
        sqlBuilder.append(PROCESS_STATUS_KEY);
        sqlBuilder.append("'");
        return sqlBuilder;
    }

    public void fillFlowTaskInfo(List<Task> taskList, String processInstanceId, WfTaskVo flowTask) {
        List<Task> tasksOfProcess = taskList.stream().filter(s -> s.getProcessInstanceId().equals(processInstanceId)).toList();
        if (CollUtil.isNotEmpty(tasksOfProcess)) {
            flowTask.setTaskName(tasksOfProcess.stream().map(Task::getName).filter(StringUtils::isNotEmpty).collect(Collectors.joining(",")));
            // 流程实例当前处理人
            tasksOfProcess.stream().max(Comparator.comparing(Task::getCreateTime)).ifPresent(t -> flowTask.setAssigneeId(StrUtil.isNotEmpty(t.getAssignee()) ? Long.valueOf(t.getAssignee()) : null));
        }
    }

    /**
     * 处理模型设计任务节点配置的抄送人
     *
     * @param taskBo 任务办理参数
     */
    public void constructFinalCopyUserIds(WfTaskBo taskBo, BpmnModel bpmnModel, Task task) {
        String copyUserIdsFromModelSetting = getCopyUserIdsFromModel(bpmnModel, task);
        if (StringUtils.isNotEmpty(taskBo.getCopyUserIds())) {
            if (StringUtils.isNotEmpty(copyUserIdsFromModelSetting)) {
                taskBo.setCopyUserIds(taskBo.getCopyUserIds() + StringUtils.SEPARATOR + copyUserIdsFromModelSetting);
            }
        } else {
            taskBo.setCopyUserIds(copyUserIdsFromModelSetting);
        }
        log.info("任务编号：{} 的抄送人为: {}", taskBo.getTaskId(), taskBo.getCopyUserIds());
    }
}
