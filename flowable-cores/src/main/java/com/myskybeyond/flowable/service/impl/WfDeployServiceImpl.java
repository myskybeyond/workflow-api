package com.myskybeyond.flowable.service.impl;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.flowable.core.domain.ProcessQuery;
import com.myskybeyond.flowable.domain.WfDeployForm;
import com.myskybeyond.flowable.domain.vo.WfDeployVo;
import com.myskybeyond.flowable.mapper.WfCategoryMapper;
import com.myskybeyond.flowable.mapper.WfDeployFormMapper;
import com.myskybeyond.flowable.service.AbstractService;
import com.myskybeyond.flowable.service.IWfDeployService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.flowable.common.engine.impl.db.SuspensionState;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.NativeProcessDefinitionQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author KonBAI
 * @createTime 2022/6/30 9:04
 */
@Service
@Primary
@Slf4j
public class WfDeployServiceImpl extends AbstractService implements IWfDeployService {

    private final WfDeployFormMapper deployFormMapper;

    public WfDeployServiceImpl(WfCategoryMapper wfCategoryMapper,
                               WfDeployFormMapper deployFormMapper) {
        super(wfCategoryMapper);
        this.deployFormMapper = deployFormMapper;
    }

    @Override
    public TableDataInfo<WfDeployVo> queryPageList(ProcessQuery processQuery, PageQuery pageQuery) {
        // 因为要匹配多个分类改为nativeQuery
        NativeProcessDefinitionQuery processDefinitionQuery = repositoryService.createNativeProcessDefinitionQuery();
        StringBuilder querySql = new StringBuilder("SELECT RES.* from ACT_RE_PROCDEF RES WHERE RES.VERSION_ = (select max(VERSION_) from ACT_RE_PROCDEF where KEY_ = RES.KEY_ and ( (TENANT_ID_ IS NOT NULL and TENANT_ID_ = RES.TENANT_ID_) or (TENANT_ID_ IS NULL and RES.TENANT_ID_ IS NULL) ) ) ");
        StringBuilder countSql = new StringBuilder("SELECT count(1) from ACT_RE_PROCDEF RES WHERE RES.VERSION_ = (select max(VERSION_) from ACT_RE_PROCDEF where KEY_ = RES.KEY_ and ( (TENANT_ID_ IS NOT NULL and TENANT_ID_ = RES.TENANT_ID_) or (TENANT_ID_ IS NULL and RES.TENANT_ID_ IS NULL) ) )");
        String parameterSql = buildProcessDefinitionSearch(processQuery);
        querySql.append(parameterSql);
        countSql.append(parameterSql);
        querySql.append(" order by RES.KEY_ asc");
        long pageTotal = processDefinitionQuery.sql(countSql.toString()).count();
        if (pageTotal <= 0) {
            return TableDataInfo.build();
        }
        int offset = pageQuery.getPageSize() * (pageQuery.getPageNum() - 1);
        List<ProcessDefinition> definitionList = processDefinitionQuery.sql(querySql.toString()).listPage(offset, pageQuery.getPageSize());
        List<Deployment> deployments = queryDeployments(definitionList);
        List<WfDeployVo> deployVoList = new ArrayList<>(definitionList.size());
        for (ProcessDefinition processDefinition : definitionList) {
            WfDeployVo vo = new WfDeployVo();
            vo.setDefinitionId(processDefinition.getId());
            vo.setProcessKey(processDefinition.getKey());
            vo.setProcessName(processDefinition.getName());
            vo.setVersion(processDefinition.getVersion());
            vo.setCategory(processDefinition.getCategory());
            vo.setDeploymentId(processDefinition.getDeploymentId());
            vo.setSuspended(processDefinition.isSuspended());
            // 流程部署信息
            fillFlowDeployInfo(deployments, processDefinition.getDeploymentId(), vo);

            deployVoList.add(vo);
        }
        Page<WfDeployVo> page = new Page<>();
        page.setRecords(deployVoList);
        page.setTotal(pageTotal);
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<WfDeployVo> queryPublishList(String processKey, PageQuery pageQuery) {
        // 创建查询条件
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(processKey)
            .orderByProcessDefinitionVersion()
            .desc();
        long pageTotal = processDefinitionQuery.count();
        if (pageTotal <= 0) {
            return TableDataInfo.build();
        }
        // 根据查询条件，查询所有版本
        int offset = pageQuery.getPageSize() * (pageQuery.getPageNum() - 1);
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery
            .listPage(offset, pageQuery.getPageSize());
        List<WfDeployVo> deployVoList = processDefinitionList.stream().map(item -> {
            WfDeployVo vo = new WfDeployVo();
            vo.setDefinitionId(item.getId());
            vo.setProcessKey(item.getKey());
            vo.setProcessName(item.getName());
            vo.setVersion(item.getVersion());
            vo.setCategory(item.getCategory());
            vo.setDeploymentId(item.getDeploymentId());
            vo.setSuspended(item.isSuspended());
            return vo;
        }).collect(Collectors.toList());
        Page<WfDeployVo> page = new Page<>();
        page.setRecords(deployVoList);
        page.setTotal(pageTotal);
        return TableDataInfo.build(page);
    }

    /**
     * 激活或挂起流程
     *
     * @param state 状态
     * @param definitionId 流程定义ID
     */
    @Override
    public void updateState(String definitionId, String state) {
        if (SuspensionState.ACTIVE.toString().equals(state)) {
            // 激活
            repositoryService.activateProcessDefinitionById(definitionId, true, null);
        } else if (SuspensionState.SUSPENDED.toString().equals(state)) {
            // 挂起
            repositoryService.suspendProcessDefinitionById(definitionId, true, null);
        }
    }

    @Override
    public String queryBpmnXmlById(String definitionId) {
        InputStream inputStream = repositoryService.getProcessModel(definitionId);
        try {
            return IoUtil.readUtf8(inputStream);
        } catch (IORuntimeException exception) {
            throw new RuntimeException("加载xml文件异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> deployIds) {
        /*
         *  添加业务校验：是否存在正在进行的任务(act_ru_task表根据proc_def_id_字段匹配)? 存在->不允许删除 不存在->可以删除
         */
        for (String deployId : deployIds) {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
            if(Objects.isNull(processDefinition)) {
                throw new RuntimeException("部署实例不存在");
            }
            long exist = taskService.createTaskQuery().processDefinitionId(processDefinition.getId()).count();
            if(exist > 0) {
                throw new RuntimeException("存在运行的任务,不允许删除");
            }else {
                repositoryService.deleteDeployment(deployId, true);
                deployFormMapper.delete(new LambdaQueryWrapper<WfDeployForm>().eq(WfDeployForm::getDeployId, deployId));
            }
        }
    }
}
