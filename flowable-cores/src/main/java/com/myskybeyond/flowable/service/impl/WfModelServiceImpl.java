package com.myskybeyond.flowable.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.flowable.common.constant.ProcessConstants;
import com.myskybeyond.flowable.common.enums.FormType;
import com.myskybeyond.flowable.domain.bo.WfModelBo;
import com.myskybeyond.flowable.domain.dto.WfMetaInfoDto;
import com.myskybeyond.flowable.domain.vo.WfFormVo;
import com.myskybeyond.flowable.domain.vo.WfModelVo;
import com.myskybeyond.flowable.mapper.WfCategoryMapper;
import com.myskybeyond.flowable.service.AbstractService;
import com.myskybeyond.flowable.service.IWfDeployFormService;
import com.myskybeyond.flowable.service.IWfFormService;
import com.myskybeyond.flowable.service.IWfModelService;
import com.myskybeyond.flowable.utils.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.repository.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author KonBAI
 * @createTime 2022/6/21 9:11
 */
@Service
@Slf4j
@Primary
public class WfModelServiceImpl extends AbstractService implements IWfModelService {

    private final IWfFormService formService;
    private final IWfDeployFormService deployFormService;

    public WfModelServiceImpl(WfCategoryMapper wfCategoryMapper,
                              IWfFormService formService,
                              IWfDeployFormService deployFormService) {
        super(wfCategoryMapper);
        this.formService = formService;
        this.deployFormService = deployFormService;
    }

    @Override
    public TableDataInfo<WfModelVo> list(WfModelBo modelBo, PageQuery pageQuery) {
//        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByCreateTime().desc();
        StringBuilder countSql = new StringBuilder("SELECT count(1) from ACT_RE_MODEL RES WHERE RES.VERSION_ = (select max(VERSION_) from ACT_RE_MODEL where KEY_ = RES.KEY_)");
        StringBuilder querySql = new StringBuilder("SELECT RES.* from ACT_RE_MODEL RES WHERE RES.VERSION_ = (select max(VERSION_) from ACT_RE_MODEL where KEY_ = RES.KEY_)");
        // 构建查询条件
        if (StringUtils.isNotBlank(modelBo.getModelKey())) {
//            modelQuery.modelKey(modelBo.getModelKey());
            countSql.append(" and RES.KEY_ = '").append(modelBo.getModelKey()).append("'");
            querySql.append(" and RES.KEY_ = '").append(modelBo.getModelKey()).append("'");
        }
        if (StringUtils.isNotBlank(modelBo.getModelName())) {
//            modelQuery.modelNameLike("%" + modelBo.getModelName() + "%");
            countSql.append(" and RES.NAME_ like '%").append(modelBo.getModelName()).append("%'");
            querySql.append(" and RES.NAME_ like '%").append(modelBo.getModelName()).append("%'");
        }
        // 取本级以及下级所有分类
        if (StringUtils.isNotBlank(modelBo.getCategory())) {
//            modelQuery.modelCategory(modelBo.getCategory());
            String codes = categoryLevelAndLowerLevel(modelBo.getCategory());
            if(StrUtil.isNotEmpty(codes)) {
                countSql.append(" and RES.CATEGORY_ in (").append(codes).append(")");
                querySql.append(" and RES.CATEGORY_ in (").append(codes).append(")");
            }
        }
        querySql.append(" order by RES.CREATE_TIME_ desc");
        NativeModelQuery modelQuery = repositoryService.createNativeModelQuery().sql("");
        // 执行查询
        long pageTotal = modelQuery.sql(countSql.toString()).count();
        if (pageTotal <= 0) {
            return TableDataInfo.build();
        }
        int offset = pageQuery.getPageSize() * (pageQuery.getPageNum() - 1);
        List<Model> modelList = modelQuery.sql(querySql.toString()).listPage(offset, pageQuery.getPageSize());
        List<WfModelVo> modelVoList = new ArrayList<>(modelList.size());
        modelList.forEach(model -> {
            WfModelVo modelVo = new WfModelVo();
            modelVo.setModelId(model.getId());
            modelVo.setModelName(model.getName());
            modelVo.setModelKey(model.getKey());
            modelVo.setCategory(model.getCategory());
            modelVo.setCreateTime(model.getCreateTime());
            modelVo.setVersion(model.getVersion());
            WfMetaInfoDto metaInfo = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
            if (metaInfo != null) {
                modelVo.setDescription(metaInfo.getDescription());
                modelVo.setFormType(metaInfo.getFormType());
                modelVo.setFormId(metaInfo.getFormId());
            }
            modelVoList.add(modelVo);
        });
        Page<WfModelVo> page = new Page<>();
        page.setRecords(modelVoList);
        page.setTotal(pageTotal);
        return TableDataInfo.build(page);
    }

    @Override
    public List<WfModelVo> list(WfModelBo modelBo) {
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByCreateTime().desc();
        // 构建查询条件
        if (StringUtils.isNotBlank(modelBo.getModelKey())) {
            modelQuery.modelKey(modelBo.getModelKey());
        }
        if (StringUtils.isNotBlank(modelBo.getModelName())) {
            modelQuery.modelNameLike("%" + modelBo.getModelName() + "%");
        }
        if (StringUtils.isNotBlank(modelBo.getCategory())) {
            modelQuery.modelCategory(modelBo.getCategory());
        }
        List<Model> modelList = modelQuery.list();
        List<WfModelVo> modelVoList = new ArrayList<>(modelList.size());
        modelList.forEach(model -> {
            WfModelVo modelVo = new WfModelVo();
            modelVo.setModelId(model.getId());
            modelVo.setModelName(model.getName());
            modelVo.setModelKey(model.getKey());
            modelVo.setCategory(model.getCategory());
            modelVo.setCreateTime(model.getCreateTime());
            modelVo.setVersion(model.getVersion());
            WfMetaInfoDto metaInfo = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
            if (metaInfo != null) {
                modelVo.setDescription(metaInfo.getDescription());
                modelVo.setFormType(metaInfo.getFormType());
                modelVo.setFormId(metaInfo.getFormId());
            }
            modelVoList.add(modelVo);
        });
        return modelVoList;
    }

    @Override
    public TableDataInfo<WfModelVo> historyList(WfModelBo modelBo, PageQuery pageQuery) {
        ModelQuery modelQuery = repositoryService.createModelQuery()
            .modelKey(modelBo.getModelKey())
            .orderByModelVersion()
            .desc();
        // 执行查询（不显示最新版，-1）
        long pageTotal = modelQuery.count() - 1;
        if (pageTotal <= 0) {
            return TableDataInfo.build();
        }
        // offset+1，去掉最新版
        int offset = 1 + pageQuery.getPageSize() * (pageQuery.getPageNum() - 1);
        List<Model> modelList = modelQuery.listPage(offset, pageQuery.getPageSize());
        List<WfModelVo> modelVoList = new ArrayList<>(modelList.size());
        modelList.forEach(model -> {
            WfModelVo modelVo = new WfModelVo();
            modelVo.setModelId(model.getId());
            modelVo.setModelName(model.getName());
            modelVo.setModelKey(model.getKey());
            modelVo.setCategory(model.getCategory());
            modelVo.setCreateTime(model.getCreateTime());
            modelVo.setVersion(model.getVersion());
            WfMetaInfoDto metaInfo = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
            if (metaInfo != null) {
                modelVo.setDescription(metaInfo.getDescription());
                modelVo.setFormType(metaInfo.getFormType());
                modelVo.setFormId(metaInfo.getFormId());
            }
            modelVoList.add(modelVo);
        });
        Page<WfModelVo> page = new Page<>();
        page.setRecords(modelVoList);
        page.setTotal(pageTotal);
        return TableDataInfo.build(page);
    }

    @Override
    public WfModelVo getModel(String modelId) {
        // 获取流程模型
        Model model = repositoryService.getModel(modelId);
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("流程模型不存在！");
        }
        // 获取流程图
        String bpmnXml = queryBpmnXmlById(modelId);
        WfModelVo modelVo = new WfModelVo();
        modelVo.setModelId(model.getId());
        modelVo.setModelName(model.getName());
        modelVo.setModelKey(model.getKey());
        modelVo.setCategory(model.getCategory());
        modelVo.setCreateTime(model.getCreateTime());
        modelVo.setVersion(model.getVersion());
        modelVo.setBpmnXml(bpmnXml);
        WfMetaInfoDto metaInfo = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
        if (metaInfo != null) {
            modelVo.setDescription(metaInfo.getDescription());
            modelVo.setFormType(metaInfo.getFormType());
            modelVo.setFormId(metaInfo.getFormId());
            if (FormType.PROCESS.getType().equals(metaInfo.getFormType())) {
                WfFormVo wfFormVo = formService.queryById(metaInfo.getFormId());
                modelVo.setContent(wfFormVo.getContent());
            }
        }
        return modelVo;
    }

    @Override
    public String queryBpmnXmlById(String modelId) {
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        return StrUtil.utf8Str(bpmnBytes);
    }

    @Override
    public void insertModel(WfModelBo modelBo) {
        Model model = repositoryService.newModel();
        model.setName(modelBo.getModelName());
        model.setKey(modelBo.getModelKey());
        model.setCategory(modelBo.getCategory());
        String metaInfo = buildMetaInfo(new WfMetaInfoDto(), modelBo.getDescription());
        model.setMetaInfo(metaInfo);
        // 保存流程模型
        repositoryService.saveModel(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateModel(WfModelBo modelBo) {
        // 根据模型Key查询模型信息
        Model model = repositoryService.getModel(modelBo.getModelId());
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("流程模型不存在！");
        }
        model.setCategory(modelBo.getCategory());
        WfMetaInfoDto metaInfoDto = JsonUtils.parseObject(model.getMetaInfo(), WfMetaInfoDto.class);
        String metaInfo = buildMetaInfo(metaInfoDto, modelBo.getDescription());
        model.setMetaInfo(metaInfo);
        // 保存流程模型
        repositoryService.saveModel(model);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveModel(WfModelBo modelBo) {
        // 查询模型信息
        Model model = repositoryService.getModel(modelBo.getModelId());
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("流程模型不存在！");
        }
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(modelBo.getBpmnXml());
        if (ObjectUtil.isEmpty(bpmnModel)) {
            throw new RuntimeException("获取模型设计失败！");
        }
        String processName = bpmnModel.getMainProcess().getName();
        // 获取开始节点
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        if (ObjectUtil.isNull(startEvent)) {
            throw new RuntimeException("开始节点不存在，请检查流程设计是否有误！");
        }
        // 获取开始节点配置的表单Key
//        if (StrUtil.isBlank(startEvent.getFormKey())) {
//            throw new RuntimeException("请配置流程表单");
//        }
        Model newModel;
        if (Boolean.TRUE.equals(modelBo.getNewVersion())) {
            newModel = repositoryService.newModel();
            newModel.setName(processName);
            newModel.setKey(model.getKey());
            newModel.setCategory(model.getCategory());
            newModel.setMetaInfo(model.getMetaInfo());
            newModel.setVersion(model.getVersion() + 1);
        } else {
            newModel = model;
            // 设置流程名称
            newModel.setName(processName);
        }
        // 保存流程模型
        repositoryService.saveModel(newModel);
        // 保存 BPMN XML
        byte[] bpmnXmlBytes = StringUtils.getBytes(modelBo.getBpmnXml(), StandardCharsets.UTF_8);
        repositoryService.addModelEditorSource(newModel.getId(), bpmnXmlBytes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void latestModel(String modelId) {
        // 获取流程模型
        Model model = repositoryService.getModel(modelId);
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("流程模型不存在！");
        }
        Integer latestVersion = repositoryService.createModelQuery()
            .modelKey(model.getKey())
            .latestVersion()
            .singleResult()
            .getVersion();
        if (model.getVersion().equals(latestVersion)) {
            throw new RuntimeException("当前版本已是最新版！");
        }
        // 获取 BPMN XML
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        Model newModel = repositoryService.newModel();
        newModel.setName(model.getName());
        newModel.setKey(model.getKey());
        newModel.setCategory(model.getCategory());
        newModel.setMetaInfo(model.getMetaInfo());
        newModel.setVersion(latestVersion + 1);
        // 保存流程模型
        repositoryService.saveModel(newModel);
        // 保存 BPMN XML
        repositoryService.addModelEditorSource(newModel.getId(), bpmnBytes);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(Collection<String> ids) {
        /*
         *  业务校验：模型是否已部署? 是->不允许删除 否->可以删除
         */
        ids.forEach(id -> {
            Model model = repositoryService.getModel(id);
            if (ObjectUtil.isNull(model)) {
                throw new RuntimeException("流程模型不存在！");
            }
            List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(model.getKey()).list();
            if(Objects.nonNull(processDefinitions) && !processDefinitions.isEmpty()){
                throw new RuntimeException("流程模型已部署,不允许删除！");
            }
            repositoryService.deleteModel(id);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deployModel(String modelId) {
        // 获取流程模型
        Model model = repositoryService.getModel(modelId);
        if (ObjectUtil.isNull(model)) {
            throw new RuntimeException("流程模型不存在！");
        }
        // 获取流程图
        byte[] bpmnBytes = repositoryService.getModelEditorSource(modelId);
        if (ArrayUtil.isEmpty(bpmnBytes)) {
            throw new RuntimeException("请先设计流程图！");
        }
        String bpmnXml = StringUtils.toEncodedString(bpmnBytes, StandardCharsets.UTF_8);
        BpmnModel bpmnModel = ModelUtils.getBpmnModel(bpmnXml);
        String processName = model.getName() + ProcessConstants.SUFFIX;
        // 部署流程
        Deployment deployment = repositoryService.createDeployment()
            .name(model.getName())
            .key(model.getKey())
            .category(model.getCategory())
            .addBytes(processName, bpmnBytes)
            .deploy();
        ProcessDefinition procDef = repositoryService.createProcessDefinitionQuery()
            .deploymentId(deployment.getId())
            .singleResult();
        // 修改流程定义的分类，便于搜索流程
        repositoryService.setProcessDefinitionCategory(procDef.getId(), model.getCategory());
        // 保存部署表单
        return deployFormService.saveInternalDeployForm(deployment.getId(), bpmnModel);
    }

    /**
     * 构建模型扩展信息
     * @return
     */
    private String buildMetaInfo(WfMetaInfoDto metaInfo, String description) {
        // 只有非空，才进行设置，避免更新时的覆盖
        if (StringUtils.isNotEmpty(description)) {
            metaInfo.setDescription(description);
        }
        if (StringUtils.isNotEmpty(metaInfo.getCreateUser())) {
            metaInfo.setCreateUser(LoginHelper.getUsername());
        }
        return JsonUtils.toJsonString(metaInfo);
    }
}
