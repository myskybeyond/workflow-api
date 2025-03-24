package com.myskybeyond.flowable.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.flowable.common.constant.ProcessConstants;
import com.myskybeyond.flowable.common.constant.TaskConstants;
import com.myskybeyond.flowable.common.enums.FlowComment;
import com.myskybeyond.flowable.common.enums.ProcessStatus;
import com.myskybeyond.flowable.core.FormConf;
import com.myskybeyond.flowable.core.FormConfOfFormCreate;
import com.myskybeyond.flowable.core.domain.ProcessQuery;
import com.myskybeyond.flowable.domain.WfDeployForm;
import com.myskybeyond.flowable.domain.bo.WfStartAndCopyBo;
import com.myskybeyond.flowable.domain.vo.*;
import com.myskybeyond.flowable.flow.FlowableUtils;
import com.myskybeyond.flowable.mapper.WfCategoryMapper;
import com.myskybeyond.flowable.mapper.WfDeployFormMapper;
import com.myskybeyond.flowable.service.AbstractService;
import com.myskybeyond.flowable.service.IWfCopyService;
import com.myskybeyond.flowable.service.IWfProcessService;
import com.myskybeyond.flowable.service.IWfTaskService;
import com.myskybeyond.flowable.utils.ModelUtils;
import com.myskybeyond.flowable.utils.ProcessFormUtils;
import com.myskybeyond.flowable.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.ProcessService;
import org.dromara.common.core.service.UserService;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.vo.SysDeptVo;
import org.dromara.system.domain.vo.SysPostVo;
import org.dromara.system.domain.vo.SysRoleVo;
import org.dromara.system.service.ISysDeptService;
import org.dromara.system.service.ISysPostService;
import org.dromara.system.service.ISysRoleService;
import org.flowable.bpmn.constants.BpmnXMLConstants;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.NativeHistoricProcessInstanceQuery;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Comment;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.history.NativeHistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.myskybeyond.flowable.common.constant.ProcessConstants.PROCESS_STATUS_KEY;
import static com.myskybeyond.flowable.utils.ModelUtils.getAllUserTaskEvent;

/**
 * 流程管理接口实现类
 *
 * @author MySkyBeyond
 */
@Service
@Primary
@Slf4j
public class WfProcessServiceImpl extends AbstractService implements IWfProcessService, ProcessService {

    private final IWfTaskService wfTaskService;
    private final UserService userService;
    private final ISysRoleService roleService;
    private final ISysDeptService deptService;
    private final ISysPostService postService;
    private final WfDeployFormMapper deployFormMapper;
    private final IWfCopyService copyService;

    public WfProcessServiceImpl(WfCategoryMapper wfCategoryMapper,
                                IWfTaskService wfTaskService,
                                UserService userService,
                                ISysRoleService roleService,
                                ISysDeptService deptService,
                                ISysPostService postService,
                                WfDeployFormMapper deployFormMapper,
                                IWfCopyService copyService) {
        super(wfCategoryMapper);
        this.wfTaskService = wfTaskService;
        this.userService = userService;
        this.roleService = roleService;
        this.deptService = deptService;
        this.postService = postService;
        this.deployFormMapper = deployFormMapper;
        this.copyService = copyService;
    }


    /**
     * 流程定义列表
     *
     * @param pageQuery 分页参数
     * @return 流程定义分页列表数据
     */
    @Override
    public TableDataInfo<WfDefinitionVo> selectPageStartProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        Page<WfDefinitionVo> page = new Page<>();
        // 流程定义列表数据查询
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
            .latestVersion()
            .active()
            .orderByProcessDefinitionKey()
            .desc();
        // 构建搜索条件
        buildProcessDefinitionSearch(processDefinitionQuery, processQuery);
        long pageTotal = processDefinitionQuery.count();
        page.setTotal(pageTotal);
        int offset = pageQuery.getPageSize() * (pageQuery.getPageNum() - 1);
        List<ProcessDefinition> definitionList = processDefinitionQuery.listPage(offset, pageQuery.getPageSize());
        List<Deployment> deployments = queryDeployments(definitionList);
        List<WfDefinitionVo> definitionVoList = new ArrayList<>();
        for (ProcessDefinition processDefinition : definitionList) {
            WfDefinitionVo vo = new WfDefinitionVo();
            vo.setDefinitionId(processDefinition.getId());
            vo.setProcessKey(processDefinition.getKey());
            vo.setProcessName(processDefinition.getName());
            vo.setVersion(processDefinition.getVersion());
            vo.setDeploymentId(processDefinition.getDeploymentId());
            vo.setSuspended(processDefinition.isSuspended());
            vo.setCategory(processDefinition.getCategory());

            fillFlowDeployInfo(deployments, processDefinition.getDeploymentId(), vo);

            definitionVoList.add(vo);
        }
        page.setRecords(definitionVoList);
        return TableDataInfo.build(page);
    }

    @Override
    public List<WfDefinitionVo> selectStartProcessList(ProcessQuery processQuery) {
        List<WfDefinitionVo> definitionVoList = new ArrayList<>();
        //  TODO 后续有此业务需求可添加此方法实现
        return definitionVoList;
    }

    @Override
    public TableDataInfo<WfTaskVo> selectPageOwnProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        Page<WfTaskVo> page = new Page<>();
        /*
          add by JianTao Li 20240521
          tips:HistoricProcessInstanceQuery or()不支持相同属性多次匹配,比如类别=张三或者等于李四这种是不支持的，执行结果总是匹配最后一次
          or()可以匹配不同属性,比如类别=张三 名称=李四,这样是可以的，输出的sql为 and (类别=张三 or 名称=李四)
          想不明白为什么不支持相同属性多次匹配!!!
          改为NativeQuery根据筛选条件匹配
         */
        NativeHistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createNativeHistoricProcessInstanceQuery();
        StringBuilder querySql = new StringBuilder(QUERY_SQL_PROCESS_COMMON);
        querySql.append("   WHERE RES.START_USER_ID_ = ").append(TaskUtils.getUserId());
        StringBuilder countSql = new StringBuilder(TOTAL_SQL_PROCESS_COMMON);
        countSql.append("   WHERE RES.START_USER_ID_ = ").append(TaskUtils.getUserId());
        processQueryAndConstructReturn(processQuery, pageQuery, querySql, countSql, historicProcessInstanceQuery, page);
        return TableDataInfo.build(page);
    }

    private void processQueryAndConstructReturn(ProcessQuery processQuery, PageQuery pageQuery, StringBuilder querySql, StringBuilder countSql, NativeHistoricProcessInstanceQuery historicProcessInstanceQuery, Page<WfTaskVo> page) {
        // 构建搜索条件
        String parameterSql = buildHistoricProcessInstanceSearch(processQuery);
        querySql.append(parameterSql);
        countSql.append(parameterSql);
        querySql.append(ORDER_SQL_PROCESS_COMMON);
        int offset = pageQuery.getPageSize() * (pageQuery.getPageNum() - 1);
        List<HistoricProcessInstance> historicProcessInstances = historicProcessInstanceQuery.sql(querySql.toString()).listPage(offset, pageQuery.getPageSize());
        page.setTotal(historicProcessInstanceQuery.sql(countSql.toString()).count());
        List<Task> tasks = queryHisTasks(historicProcessInstances);
        List<HistoricVariableInstance> processStatusVariables = queryProcessInstanceVariables(historicProcessInstances);
        List<WfTaskVo> taskVoList = new ArrayList<>();
        for (HistoricProcessInstance hisIns : historicProcessInstances) {
            WfTaskVo taskVo = new WfTaskVo();

            String processStatus = fillProcessVariables(processStatusVariables, hisIns.getId(), taskVo);

            taskVo.setCreateTime(hisIns.getStartTime());
            taskVo.setFinishTime(hisIns.getEndTime());
            taskVo.setProcInsId(hisIns.getId());

            // 计算耗时
            if (Objects.nonNull(hisIns.getEndTime())) {
                taskVo.setDuration(DateUtils.getDatePoor(hisIns.getEndTime(), hisIns.getStartTime()));
            } else {
                taskVo.setDuration(DateUtils.getDatePoor(DateUtils.getNowDate(), hisIns.getStartTime()));
            }
            taskVo.setDeployId(hisIns.getDeploymentId());

            fillFlowDefInfo(historicProcessInstances, hisIns.getId(), taskVo);

            // 进行中|已挂起的流程设置当前节点信息
            if (ProcessStatus.RUNNING.getStatus().equals(processStatus) || ProcessStatus.SUSPENDED.getStatus().equals(processStatus)) {
                fillFlowTaskInfo(tasks, hisIns.getId(), taskVo);
            }
            taskVoList.add(taskVo);
        }
        page.setRecords(taskVoList);
    }

    @Override
    public List<WfTaskVo> selectOwnProcessList(ProcessQuery processQuery) {
        List<WfTaskVo> taskVoList = new ArrayList<>();
        //  TODO 后续有此业务需求可添加此方法实现
        return taskVoList;
    }

    @Override
    public TableDataInfo<WfTaskVo> selectPageTodoProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        TaskQuery taskQuery = taskService.createTaskQuery()
            .active()
            .includeProcessVariables()
            .taskCandidateOrAssigned(TaskUtils.getUserId())
            .taskCandidateGroupIn(TaskUtils.getCandidateGroup())
            .orderByTaskCreateTime().desc();
        return selectPageTaskProcessList(taskQuery, processQuery, pageQuery);
    }

    @Override
    public List<WfTaskVo> selectTodoProcessList(ProcessQuery processQuery) {
        List<WfTaskVo> taskVoList = new ArrayList<>();
        //  TODO 后续有此业务需求可添加此方法实现
        return taskVoList;
    }

    @Override
    public TableDataInfo<WfTaskVo> selectPageClaimProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        TaskQuery taskQuery = taskService.createTaskQuery()
            .active()
            .includeProcessVariables()
            .taskCandidateUser(TaskUtils.getUserId())
            .taskCandidateGroupIn(TaskUtils.getCandidateGroup())
            .orderByTaskCreateTime().desc();
        return selectPageTaskProcessList(taskQuery, processQuery, pageQuery);
    }

    @Override
    public List<WfTaskVo> selectClaimProcessList(ProcessQuery processQuery) {
        List<WfTaskVo> flowList = new ArrayList<>();
        //  TODO 后续有此业务需求可添加此方法实现
        return flowList;
    }

    @Override
    public TableDataInfo<WfTaskVo> selectPageFinishedProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        Page<WfTaskVo> page = new Page<>();
        NativeHistoricTaskInstanceQuery taskInstanceQuery = historyService.createNativeHistoricTaskInstanceQuery();
        // 改为nativeQuery 因为taskCompletedAfter和taskCompletedBefore同时使用逻辑关系是or,未找到原因 add by JianTao Li 202040528
        StringBuilder baseQuerySql = new StringBuilder("select {} from  ACT_HI_TASKINST RES WHERE RES.ASSIGNEE_ = '");
        baseQuerySql.append(TaskUtils.getUserId()).append("'");
        // 构建搜索条件
        buildNativeHistoricTaskInstanceSearch(baseQuerySql, processQuery);
        baseQuerySql.append(" and RES.END_TIME_ is not null ");
        int offset = pageQuery.getPageSize() * (pageQuery.getPageNum() - 1);
        page.setTotal(taskInstanceQuery.sql(StrUtil.format(baseQuerySql.toString(), "count(1)")).count());

        baseQuerySql.append(" order by RES.END_TIME_ desc");

        List<HistoricTaskInstance> historicTaskInstanceList = taskInstanceQuery.sql(StrUtil.format(baseQuerySql.toString(), "RES.*")).listPage(offset, pageQuery.getPageSize());
        List<HistoricProcessInstance> historicProcessInstances = queryHisProcessInstances(historicTaskInstanceList);
        List<WfTaskVo> hisTaskList = new ArrayList<>();
        for (HistoricTaskInstance histTask : historicTaskInstanceList) {
            WfTaskVo flowTask = new WfTaskVo();
            // 当前流程信息
            flowTask.setTaskId(histTask.getId());
            // 审批人员信息
            flowTask.setCreateTime(histTask.getCreateTime());
            flowTask.setFinishTime(histTask.getEndTime());
            flowTask.setDuration(DateUtil.formatBetween(histTask.getDurationInMillis(), BetweenFormatter.Level.SECOND));
            flowTask.setProcDefId(histTask.getProcessDefinitionId());
            flowTask.setTaskDefKey(histTask.getTaskDefinitionKey());
            flowTask.setTaskName(histTask.getName());
            flowTask.setProcInsId(histTask.getProcessInstanceId());
            flowTask.setHisProcInsId(histTask.getProcessInstanceId());

            fillFlowDefInfo(historicProcessInstances, histTask.getProcessInstanceId(), flowTask);

            // 流程变量
            flowTask.setProcVars(histTask.getProcessVariables());

            hisTaskList.add(flowTask);
        }
        page.setRecords(hisTaskList);
        return TableDataInfo.build(page);
    }

    @Override
    public List<WfTaskVo> selectFinishedProcessList(ProcessQuery processQuery) {
        List<WfTaskVo> hisTaskList = new ArrayList<>();
        //  TODO 后续有此业务需求可添加此方法实现
        return hisTaskList;
    }

    @Override
    @Deprecated
    public FormConfOfFormCreate selectFormContent(String definitionId, String deployId, String procInsId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(definitionId);
        if (ObjectUtil.isNull(bpmnModel)) {
            throw new RuntimeException("获取流程设计失败！");
        }
        // edit by JianTao Li 20240424 发起流程获取第一个用户任务的表单
//        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        UserTask firstUserTask = ModelUtils.getFirstUserTaskEvent(bpmnModel);
        if (ObjectUtil.isNull(firstUserTask)) {
            throw new RuntimeException("获取用户任务表单失败！");
        }
        /*
           edit by JianTao Li 20240522
           修改为：重新发起一个同样的流程，版本已最新为准。
         */
        WfDeployForm deployForm = deployFormMapper.selectOne(new LambdaQueryWrapper<WfDeployForm>()
            .eq(WfDeployForm::getDeployId, deployId)
            .eq(WfDeployForm::getFormKey, firstUserTask.getFormKey())
            .eq(WfDeployForm::getNodeKey, firstUserTask.getId()));
        // edit end
        FormConfOfFormCreate formConf = JsonUtils.parseObject(deployForm.getContent(), FormConfOfFormCreate.class);
        if (ObjectUtil.isNull(formConf)) {
            throw new RuntimeException("获取流程表单失败！");
        }
        if (ObjectUtil.isNotEmpty(procInsId)) {
            // 获取流程实例
            HistoricProcessInstance historicProcIns = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInsId)
                .includeProcessVariables()
                .singleResult();
            // 填充表单信息
            ProcessFormUtils.fillFormData(formConf, historicProcIns.getProcessVariables(), false);
        }
        return formConf;
    }

    /**
     * 根据流程定义ID启动流程实例
     *
     * @param procDefId 流程定义Id
     * @param variables 流程变量
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProcessByDefId(String procDefId, Map<String, Object> variables) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(procDefId).singleResult();
            startProcess(processDefinition, variables);
        } catch (Exception e) {
            log.error("根据流程定义ID启动流程实例发生异常,异常信息: {}", e.getMessage());
            throw new ServiceException("流程启动错误");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProcessByIds(String[] instanceIds) {
        List<String> ids = Arrays.asList(instanceIds);
        // 校验流程是否结束
        long activeInsCount = runtimeService.createProcessInstanceQuery()
            .processInstanceIds(new HashSet<>(ids)).active().count();
        if (activeInsCount > 0) {
            throw new ServiceException("不允许删除进行中的流程实例");
        }
        // 删除历史流程实例
        historyService.bulkDeleteHistoricProcessInstances(ids);
    }

    /**
     * 读取xml文件
     *
     * @param processDefId 流程定义ID
     */
    @Override
    public String queryBpmnXmlById(String processDefId) {
        InputStream inputStream = repositoryService.getProcessModel(processDefId);
        try {
            return IoUtil.readUtf8(inputStream);
        } catch (IORuntimeException exception) {
            throw new RuntimeException("加载xml文件异常");
        }
    }

    /**
     * 流程详情信息
     *
     * @param procInsId 流程实例ID
     * @param taskId    任务ID
     * @return
     */
    @Override
    public WfDetailVo queryProcessDetail(String procInsId, String taskId) {
        WfDetailVo detailVo = new WfDetailVo();
        // 获取流程实例
        HistoricProcessInstance historicProcIns = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(procInsId)
            .includeProcessVariables()
            .singleResult();
        if (StringUtils.isNotBlank(taskId)) {
            HistoricTaskInstance taskIns = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .includeIdentityLinks()
                .includeProcessVariables()
                .includeTaskLocalVariables()
                .singleResult();
            if (taskIns == null) {
                throw new ServiceException("没有可办理的任务！");
            }
            detailVo.setTaskFormData(currTaskFormData(historicProcIns.getDeploymentId(), taskIns));
        }
        // 获取Bpmn模型信息
        InputStream inputStream = repositoryService.getProcessModel(historicProcIns.getProcessDefinitionId());
        String bpmnXmlStr = StrUtil.utf8Str(IoUtil.readBytes(inputStream, false));
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(bpmnXmlStr);
        detailVo.setBpmnXml(bpmnXmlStr);
        detailVo.setHistoryProcNodeList(historyProcNodeList(historicProcIns));
        detailVo.setProcessFormList(processFormList(bpmnModel, historicProcIns));
        detailVo.setFlowViewer(getFlowViewer(bpmnModel, procInsId));
        return detailVo;
    }

    @Override
    public FormConfOfFormCreate selectFormContent(String definitionKey, String procInsId) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(definitionKey).latestVersion().singleResult();
        if (ObjectUtil.isNull(processDefinition)) {
            log.error("流程Key: {} 获取失败", definitionKey);
            throw new RuntimeException("获取流程设计失败！");
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        if (ObjectUtil.isNull(bpmnModel)) {
            throw new RuntimeException("获取流程设计失败！");
        }
        UserTask firstUserTask = ModelUtils.getFirstUserTaskEvent(bpmnModel);
        if (ObjectUtil.isNull(firstUserTask)) {
            throw new RuntimeException("获取用户任务表单失败！");
        }
        WfDeployForm deployForm = deployFormMapper.selectOne(new LambdaQueryWrapper<WfDeployForm>()
            .eq(WfDeployForm::getDeployId, processDefinition.getDeploymentId())
            .eq(WfDeployForm::getFormKey, firstUserTask.getFormKey())
            .eq(WfDeployForm::getNodeKey, firstUserTask.getId()));
        // edit end
        FormConfOfFormCreate formConf = JsonUtils.parseObject(deployForm.getContent(), FormConfOfFormCreate.class);
        if (ObjectUtil.isNull(formConf)) {
            throw new RuntimeException("获取流程表单失败！");
        }
        if (ObjectUtil.isNotEmpty(procInsId)) {
            // 获取流程实例
            HistoricProcessInstance historicProcIns = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procInsId)
                .includeProcessVariables()
                .singleResult();
            // 填充表单信息
            ProcessFormUtils.fillFormData(formConf, historicProcIns.getProcessVariables(), false);
        }
        return formConf;
    }

    @Override
    public TableDataInfo<WfTaskVo> selectPageProcessList(ProcessQuery processQuery, PageQuery pageQuery) {
        Page<WfTaskVo> page = new Page<>();
        NativeHistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createNativeHistoricProcessInstanceQuery();
        StringBuilder querySql = new StringBuilder(QUERY_SQL_PROCESS_COMMON);
        querySql.append(" WHERE 1 = 1 ");
        StringBuilder countSql = new StringBuilder(TOTAL_SQL_PROCESS_COMMON);
        countSql.append(" WHERE 1 = 1 ");
        // 构建搜索条件
        processQueryAndConstructReturn(processQuery, pageQuery, querySql, countSql, historicProcessInstanceQuery, page);
        return TableDataInfo.build(page);
    }

    @Override
    @Transactional
    public void updateState(String processInstanceId, String stateCode) {
        Object variable = runtimeService.getVariable(processInstanceId, PROCESS_STATUS_KEY);
        ProcessStatus status = ProcessStatus.getProcessStatus(Convert.toStr(variable));
        if (SuspensionState.ACTIVE.toString().equals(stateCode)) {
            // 激活
            if (ObjectUtil.isNotNull(status) && ProcessStatus.RUNNING == status) {
                log.warn("流程: {} 为进行中状态,无需再次激活", processInstanceId);
                throw new RuntimeException("该流程为进行中状态,无需再次激活");
            }
            runtimeService.activateProcessInstanceById(processInstanceId);
            log.info("流程: {} 设置为进行中状态.", processInstanceId);
            runtimeService.setVariable(processInstanceId, PROCESS_STATUS_KEY, ProcessStatus.RUNNING.getStatus());
        } else if (SuspensionState.SUSPENDED.toString().equals(stateCode)) {
            // 挂起
            if (ObjectUtil.isNotNull(status) && ProcessStatus.SUSPENDED == status) {
                log.warn("流程: {} 为已挂起状态,无需再次挂起", processInstanceId);
                throw new RuntimeException("流程为已挂起状态,无需再次挂起");
            }
            log.info("流程: {} 设置为已挂起状态.", processInstanceId);
            runtimeService.setVariable(processInstanceId, PROCESS_STATUS_KEY, ProcessStatus.SUSPENDED.getStatus());
            runtimeService.suspendProcessInstanceById(processInstanceId);
        }
    }

    @Override
    public List<FlowElement> findJumpPorcessList(String processInstanceId) {
        if (StrUtil.isEmpty(processInstanceId)) {
            throw new ServiceException("参数流程实例ID为空");
        }
        // 获取流程实例的流程定义ID
        String processDefinitionId = runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult()
            .getProcessDefinitionId();
        // 获取BPMN模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Collection<UserTask> userTasks = getAllUserTaskEvent(bpmnModel);
        return new ArrayList<>(userTasks);
    }

    @Override
    public void processJump(String processInstanceId, String targetKey) {
        if (StrUtil.isEmpty(processInstanceId) || StrUtil.isEmpty(targetKey)) {
            throw new RuntimeException("参数为空,请联系管理员！");
        }
        // 流程实例是否能找到
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
        if (ObjectUtil.isNull(processInstance)) {
            throw new RuntimeException("获取流程实例信息异常！");
        }
        if (processInstance.isSuspended()) {
            throw new RuntimeException("流程处于挂起状态");
        }
        // 获取流程模型信息
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        // 获取跳转的节点元素
        FlowElement target = ModelUtils.getFlowElementById(bpmnModel, targetKey);
        // 获取所有正常进行的任务节点 Key，这些任务不能直接使用，需要找出其中需要撤回的任务
        List<Task> runTaskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        List<String> runTaskKeyList = new ArrayList<>();
        runTaskList.forEach(item -> runTaskKeyList.add(item.getTaskDefinitionKey()));
        // 需退回任务列表
        List<String> currentIds = new ArrayList<>();
//        // 通过父级网关的出口连线，结合 runTaskList 比对，获取需要撤回的任务
        List<UserTask> currentUserTaskList = FlowableUtils.iteratorFindChildUserTasks(target, runTaskKeyList, null, null);
        currentUserTaskList.forEach(item -> currentIds.add(item.getId()));

        // 循环获取那些需要被撤回的节点的ID，用来设置驳回原因
        List<String> currentTaskIds = new ArrayList<>();
        currentIds.forEach(currentId -> runTaskList.forEach(runTask -> {
            if (currentId.equals(runTask.getTaskDefinitionKey())) {
                currentTaskIds.add(runTask.getId());
            }
        }));
        identityService.setAuthenticatedUserId(TaskUtils.getUserId());
        // 设置跳转意见
        for (String currentTaskId : currentTaskIds) {
            taskService.addComment(currentTaskId, processInstanceId, FlowComment.JUMP.getType(), "管理员操作流程实例跳转");
        }
        try {
            runtimeService.createChangeActivityStateBuilder().processInstanceId(processInstanceId).moveActivityIdsToSingleActivityId(currentIds, targetKey).changeState();
        } catch (FlowableObjectNotFoundException e) {
            throw new RuntimeException("未找到流程实例，流程可能已发生变化");
        } catch (FlowableException e) {
            log.error("流程节点跳转发生异常,异常信息: {}", e.getMessage());
            throw new RuntimeException("无法取消或开始活动");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProcessByDefKey(String procDefKey, WfStartAndCopyBo startAndCopyBo) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(procDefKey).latestVersion().singleResult();
            startProcess(processDefinition, startAndCopyBo);
        } catch (Exception e) {
            log.error("通过流程定义Key: {} 启动流程发生异常,异常信息: {}", procDefKey, e.getMessage());
            throw new ServiceException("流程启动错误");
        }
    }

    /**
     * 启动流程实例
     */
    private void startProcess(ProcessDefinition procDef, Map<String, Object> variables) {
        if (ObjectUtil.isNotNull(procDef) && procDef.isSuspended()) {
            throw new ServiceException("流程已被挂起，请先激活流程");
        }
        // 设置流程发起人Id到流程中
        String userIdStr = TaskUtils.getUserId();
        identityService.setAuthenticatedUserId(userIdStr);
        variables.put(BpmnXMLConstants.ATTRIBUTE_EVENT_START_INITIATOR, userIdStr);
        // 设置流程状态为进行中
        variables.put(PROCESS_STATUS_KEY, ProcessStatus.RUNNING.getStatus());
        // 发起流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDef.getId(), variables);
        // 第一个用户任务为发起人，则自动完成任务
        wfTaskService.startFirstTask(processInstance, variables);
    }

    /**
     * 启动流程实例
     */
    private void startProcess(ProcessDefinition procDef, WfStartAndCopyBo form) {
        if (ObjectUtil.isNotNull(procDef) && procDef.isSuspended()) {
            throw new ServiceException("流程已被挂起，请先激活流程");
        }
        // 设置流程发起人Id到流程中
        String userIdStr = TaskUtils.getUserId();
        identityService.setAuthenticatedUserId(userIdStr);
        Map<String, Object> variables = form.getVariables();
        variables.put(BpmnXMLConstants.ATTRIBUTE_EVENT_START_INITIATOR, userIdStr);
        // 设置流程状态为进行中
        variables.put(PROCESS_STATUS_KEY, ProcessStatus.RUNNING.getStatus());
        // 发起流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(procDef.getId(), variables);
        // 第一个用户任务为发起人，则自动完成任务
        wfTaskService.startFirstTask(processInstance, form);
    }


    /**
     * 获取流程变量
     *
     * @param taskId 任务ID
     * @return 流程变量
     */
    @Deprecated
    private Map<String, Object> getProcessVariables(String taskId) {
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery()
            .includeProcessVariables()
            .finished()
            .taskId(taskId)
            .singleResult();
        if (Objects.nonNull(historicTaskInstance)) {
            return historicTaskInstance.getProcessVariables();
        }
        return taskService.getVariables(taskId);
    }

    /**
     * 获取当前任务流程表单信息
     */
    private FormConfOfFormCreate currTaskFormData(String deployId, HistoricTaskInstance taskIns) {
        WfDeployFormVo deployFormVo = deployFormMapper.selectVoOne(new LambdaQueryWrapper<WfDeployForm>()
            .eq(WfDeployForm::getDeployId, deployId)
            .eq(WfDeployForm::getFormKey, taskIns.getFormKey())
            .eq(WfDeployForm::getNodeKey, taskIns.getTaskDefinitionKey()));
        if (ObjectUtil.isNotEmpty(deployFormVo)) {
            FormConfOfFormCreate currTaskFormData = JsonUtils.parseObject(deployFormVo.getContent(), FormConfOfFormCreate.class);
            if (null != currTaskFormData) {
                currTaskFormData.getOption().getSubmitBtn().setShow(false);
                currTaskFormData.getOption().getResetBtn().setShow(false);
                ProcessFormUtils.fillFormData(currTaskFormData, taskIns.getTaskLocalVariables(), false);
                return currTaskFormData;
            }
        }
        return null;
    }

    /**
     * 获取历史流程表单信息
     */
    private List<FormConfOfFormCreate> processFormList(BpmnModel bpmnModel, HistoricProcessInstance historicProcIns) {
        List<FormConfOfFormCreate> procFormList = new ArrayList<>();

        List<HistoricActivityInstance> activityInstanceList = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(historicProcIns.getId()).finished()
            .activityTypes(CollUtil.newHashSet(BpmnXMLConstants.ELEMENT_EVENT_START, BpmnXMLConstants.ELEMENT_TASK_USER))
            .orderByHistoricActivityInstanceStartTime().asc()
            .list();
        List<String> processFormKeys = new ArrayList<>();
        for (HistoricActivityInstance activityInstance : activityInstanceList) {
            // 获取当前节点流程元素信息
            FlowElement flowElement = ModelUtils.getFlowElementById(bpmnModel, activityInstance.getActivityId());
            // 获取当前节点表单Key
            String formKey = ModelUtils.getFormKey(flowElement);
            if (formKey == null) {
                continue;
            }
            boolean localScope = Convert.toBool(ModelUtils.getElementAttributeValue(flowElement, ProcessConstants.PROCESS_FORM_LOCAL_SCOPE), false);
            Map<String, Object> variables;
            if (localScope) {
                // 查询任务节点参数，并转换成Map
                variables = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(historicProcIns.getId())
                    .taskId(activityInstance.getTaskId())
                    .list()
                    .stream()
                    .collect(Collectors.toMap(HistoricVariableInstance::getVariableName, HistoricVariableInstance::getValue));
            } else {
                if (processFormKeys.contains(formKey)) {
                    continue;
                }
                variables = historicProcIns.getProcessVariables();
                processFormKeys.add(formKey);
            }
            // 非节点表单此处查询结果可能有多条，只获取第一条信息
            List<WfDeployFormVo> formInfoList = deployFormMapper.selectVoList(new LambdaQueryWrapper<WfDeployForm>()
                .eq(WfDeployForm::getDeployId, historicProcIns.getDeploymentId())
                .eq(WfDeployForm::getFormKey, formKey)
                .eq(localScope, WfDeployForm::getNodeKey, flowElement.getId()));

            //@update by Brath：避免空集合导致的NULL空指针
            WfDeployFormVo formInfo = formInfoList.stream().findFirst().orElse(null);

            if (ObjectUtil.isNotNull(formInfo)) {
                // 旧数据 formInfo.getFormName() 为 null
                String formName = Optional.ofNullable(formInfo.getFormName()).orElse(StringUtils.EMPTY);
                String title = localScope ? formName.concat("(" + flowElement.getName() + ")") : formName;
                FormConfOfFormCreate formConf = JsonUtils.parseObject(formInfo.getContent(), FormConfOfFormCreate.class);
                if (null != formConf) {
                    formConf.setTitle(title);
                    formConf.getOption().getSubmitBtn().setShow(false);
                    formConf.getOption().getResetBtn().setShow(false);
                    ProcessFormUtils.fillFormData(formConf, variables, true);
                    procFormList.add(formConf);
                }
            }
        }
        return procFormList;
    }

    @Deprecated
    private void buildStartFormData(HistoricProcessInstance historicProcIns, Process process, String deployId, List<FormConf> procFormList) {
        procFormList = procFormList == null ? new ArrayList<>() : procFormList;
        HistoricActivityInstance startInstance = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(historicProcIns.getId())
            .activityId(historicProcIns.getStartActivityId())
            .singleResult();
        StartEvent startEvent = (StartEvent) process.getFlowElement(startInstance.getActivityId());
        WfDeployFormVo startFormInfo = deployFormMapper.selectVoOne(new LambdaQueryWrapper<WfDeployForm>()
            .eq(WfDeployForm::getDeployId, deployId)
            .eq(WfDeployForm::getFormKey, startEvent.getFormKey())
            .eq(WfDeployForm::getNodeKey, startEvent.getId()));
        if (ObjectUtil.isNotNull(startFormInfo)) {
            FormConf formConf = JsonUtils.parseObject(startFormInfo.getContent(), FormConf.class);
            if (null != formConf) {
                formConf.setTitle(startEvent.getName());
                formConf.setDisabled(true);
                formConf.setFormBtns(false);
                ProcessFormUtils.fillFormData(formConf, historicProcIns.getProcessVariables());
                procFormList.add(formConf);
            }
        }
    }

    @Deprecated
    private void buildUserTaskFormData(String procInsId, String deployId, Process process, List<FormConf> procFormList) {
        procFormList = procFormList == null ? new ArrayList<>() : procFormList;
        List<HistoricActivityInstance> activityInstanceList = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(procInsId).finished()
            .activityType(BpmnXMLConstants.ELEMENT_TASK_USER)
            .orderByHistoricActivityInstanceStartTime().asc()
            .list();
        for (HistoricActivityInstance instanceItem : activityInstanceList) {
            UserTask userTask = (UserTask) process.getFlowElement(instanceItem.getActivityId(), true);
            String formKey = userTask.getFormKey();
            if (formKey == null) {
                continue;
            }
            // 查询任务节点参数，并转换成Map
            Map<String, Object> variables = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(procInsId)
                .taskId(instanceItem.getTaskId())
                .list()
                .stream()
                .collect(Collectors.toMap(HistoricVariableInstance::getVariableName, HistoricVariableInstance::getValue));
            WfDeployFormVo deployFormVo = deployFormMapper.selectVoOne(new LambdaQueryWrapper<WfDeployForm>()
                .eq(WfDeployForm::getDeployId, deployId)
                .eq(WfDeployForm::getFormKey, formKey)
                .eq(WfDeployForm::getNodeKey, userTask.getId()));
            if (ObjectUtil.isNotNull(deployFormVo)) {
                FormConf formConf = JsonUtils.parseObject(deployFormVo.getContent(), FormConf.class);
                if (null != formConf) {
                    formConf.setTitle(userTask.getName());
                    formConf.setDisabled(true);
                    formConf.setFormBtns(false);
                    ProcessFormUtils.fillFormData(formConf, variables);
                    procFormList.add(formConf);
                }
            }
        }
    }

    /**
     * 获取历史任务信息列表
     */
    private List<WfProcNodeVo> historyProcNodeList(HistoricProcessInstance historicProcIns) {
        String procInsId = historicProcIns.getId();
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(procInsId)
            .activityTypes(CollUtil.newHashSet(BpmnXMLConstants.ELEMENT_EVENT_START, BpmnXMLConstants.ELEMENT_EVENT_END, BpmnXMLConstants.ELEMENT_TASK_USER))
            .orderByHistoricActivityInstanceStartTime().desc()
            .orderByHistoricActivityInstanceEndTime().desc()
            .list();

        List<Comment> commentList = taskService.getProcessInstanceComments(procInsId);

        List<WfProcNodeVo> elementVoList = new ArrayList<>();
        for (HistoricActivityInstance activityInstance : historicActivityInstanceList) {
            WfProcNodeVo elementVo = new WfProcNodeVo();
            // MI_END代表多实例任务中或签后结束，未执行的多实例任务会被关闭
            if ("MI_END".equals(activityInstance.getDeleteReason())) {
                continue;
            }
            elementVo.setProcDefId(activityInstance.getProcessDefinitionId());
            elementVo.setActivityId(activityInstance.getActivityId());
            elementVo.setActivityName(activityInstance.getActivityName());
            elementVo.setActivityType(activityInstance.getActivityType());
            elementVo.setCreateTime(activityInstance.getStartTime());
            elementVo.setEndTime(activityInstance.getEndTime());
            if (ObjectUtil.isNotNull(activityInstance.getDurationInMillis())) {
                elementVo.setDuration(DateUtil.formatBetween(activityInstance.getDurationInMillis(), BetweenFormatter.Level.SECOND));
            }

            if (BpmnXMLConstants.ELEMENT_EVENT_START.equals(activityInstance.getActivityType())) {
                if (ObjectUtil.isNotNull(historicProcIns)) {
                    Long userId = Long.parseLong(historicProcIns.getStartUserId());
                    String nickName = userService.selectNicknameById(userId);
                    if (nickName != null) {
                        elementVo.setAssigneeId(userId);
                        elementVo.setAssigneeName(nickName);
                    }
                }
            } else if (BpmnXMLConstants.ELEMENT_TASK_USER.equals(activityInstance.getActivityType())) {
                if (StringUtils.isNotBlank(activityInstance.getAssignee())) {
                    Long userId = Long.parseLong(activityInstance.getAssignee());
                    String nickName = userService.selectNicknameById(userId);
                    elementVo.setAssigneeId(userId);
                    elementVo.setAssigneeName(nickName);
                }
                // 展示审批人员
                List<HistoricIdentityLink> linksForTask = historyService.getHistoricIdentityLinksForTask(activityInstance.getTaskId());
                StringBuilder stringBuilder = new StringBuilder();
                for (HistoricIdentityLink identityLink : linksForTask) {
                    if ("candidate".equals(identityLink.getType())) {
                        if (StringUtils.isNotBlank(identityLink.getUserId())) {
                            Long userId = Long.parseLong(identityLink.getUserId());
                            String nickName = userService.selectNicknameById(userId);
                            stringBuilder.append(nickName).append(",");
                        }
                        if (StringUtils.isNotBlank(identityLink.getGroupId())) {
                            if (identityLink.getGroupId().startsWith(TaskConstants.ROLE_GROUP_PREFIX)) {
                                Long roleId = Long.parseLong(StringUtils.stripStart(identityLink.getGroupId(), TaskConstants.ROLE_GROUP_PREFIX));
                                SysRoleVo role = roleService.selectRoleById(roleId);
                                stringBuilder.append(role.getRoleName()).append(",");
                            } else if (identityLink.getGroupId().startsWith(TaskConstants.DEPT_GROUP_PREFIX)) {
                                Long deptId = Long.parseLong(StringUtils.stripStart(identityLink.getGroupId(), TaskConstants.DEPT_GROUP_PREFIX));
                                SysDeptVo dept = deptService.selectDeptById(deptId);
                                stringBuilder.append(dept.getDeptName()).append(",");
                            } else if (identityLink.getGroupId().startsWith(TaskConstants.POST_GROUP_PREFIX)) {
                                // add by JianTao Li 20240425 流程实例查询添加岗位支持
                                Long positionId = Long.parseLong(StringUtils.stripStart(identityLink.getGroupId(), TaskConstants.POST_GROUP_PREFIX));
                                SysPostVo post = postService.selectPostById(positionId);
                                stringBuilder.append(post.getPostName()).append(",");
                            }
                        }
                    }
                }
                if (StringUtils.isNotBlank(stringBuilder)) {
                    elementVo.setCandidate(stringBuilder.substring(0, stringBuilder.length() - 1));
                }
                // 获取意见评论内容
                if (CollUtil.isNotEmpty(commentList)) {
                    List<Comment> comments = new ArrayList<>();
                    for (Comment comment : commentList) {

                        if (comment.getTaskId().equals(activityInstance.getTaskId())) {
                            comments.add(comment);
                        }
                    }
                    elementVo.setCommentList(comments);
                }
            }
            elementVoList.add(elementVo);
        }
        return elementVoList;
    }

    /**
     * 获取流程执行过程
     *
     * @param procInsId
     * @return
     */
    private WfViewerVo getFlowViewer(BpmnModel bpmnModel, String procInsId) {
        // 构建查询条件
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(procInsId);
        List<HistoricActivityInstance> allActivityInstanceList = query.list();
        if (CollUtil.isEmpty(allActivityInstanceList)) {
            return new WfViewerVo();
        }
        // 查询所有已完成的元素
        List<HistoricActivityInstance> finishedElementList = allActivityInstanceList.stream()
            .filter(item -> ObjectUtil.isNotNull(item.getEndTime())).collect(Collectors.toList());
        // 所有已完成的连线
        Set<String> finishedSequenceFlowSet = new HashSet<>();
        // 所有已完成的任务节点
        Set<String> finishedTaskSet = new HashSet<>();
        finishedElementList.forEach(item -> {
            if (BpmnXMLConstants.ELEMENT_SEQUENCE_FLOW.equals(item.getActivityType())) {
                finishedSequenceFlowSet.add(item.getActivityId());
            } else {
                finishedTaskSet.add(item.getActivityId());
            }
        });
        // 查询所有未结束的节点
        Set<String> unfinishedTaskSet = allActivityInstanceList.stream()
            .filter(item -> ObjectUtil.isNull(item.getEndTime()))
            .map(HistoricActivityInstance::getActivityId)
            .collect(Collectors.toSet());
        // DFS 查询未通过的元素集合
        Set<String> rejectedSet = FlowableUtils.dfsFindRejects(bpmnModel, unfinishedTaskSet, finishedSequenceFlowSet, finishedTaskSet);
        return new WfViewerVo(finishedTaskSet, finishedSequenceFlowSet, unfinishedTaskSet, rejectedSet);
    }

    @Override
    public Boolean startProcessByModelKey(String modelKey, Map<String, Object> variables) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey(modelKey).latestVersion().singleResult();
            startProcess(processDefinition, variables);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("创建流程实例出现异常", e);
            return Boolean.FALSE;
        }
    }

    @Override
    public String getProjectIdByTaskId(String taskId) {
        //任务ID获取所有的流程变量
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            log.error("根据任务id获取任务信息失败：" + taskId);
            return StrUtil.EMPTY;
        }
        // 获取流程变量
        List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
            .processInstanceId(task.getProcessInstanceId())
            .list();

        String projectId = StrUtil.EMPTY;
        // 遍历并打印变量信息
        for (HistoricVariableInstance variable : variables) {
            if (ProcessConstants.PROJECT_ID_KEY.equals(variable.getVariableName())) {
                Object projectIdObj = variable.getValue();
                if (ObjectUtil.isNull(projectIdObj)) {

                } else {
                    projectId = projectIdObj.toString().split(StringUtils.SEPARATOR)[0];
                }
                break;
            }
        }
        return projectId;
    }

    @Override
    public String getOrderIdByTaskId(String taskId) {
        //任务ID获取所有的流程变量
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            log.error("根据任务id获取任务信息失败：" + taskId);
            return StrUtil.EMPTY;
        }
        // 获取流程变量
        List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery()
            .processInstanceId(task.getProcessInstanceId())
            .list();

        String orderId = StrUtil.EMPTY;
        // 遍历并打印变量信息
        for (HistoricVariableInstance variable : variables) {
            if (ProcessConstants.ORDER_ID_KEY.equals(variable.getVariableName())) {
                Object orderIdObj = variable.getValue();
                if (ObjectUtil.isNull(orderIdObj)) {

                } else {
                    orderId = orderIdObj.toString().split(StringUtils.SEPARATOR)[0];
                }
                break;
            }
        }
        return orderId;
    }

}
