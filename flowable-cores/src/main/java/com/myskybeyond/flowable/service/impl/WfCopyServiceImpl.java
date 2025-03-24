package com.myskybeyond.flowable.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.flowable.domain.WfCopy;
import com.myskybeyond.flowable.domain.bo.WfCopyBo;
import com.myskybeyond.flowable.domain.bo.WfTaskBo;
import com.myskybeyond.flowable.domain.vo.WfCopyVo;
import com.myskybeyond.flowable.mapper.WfCopyMapper;
import com.myskybeyond.flowable.service.IWfCopyService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 流程抄送Service业务层处理
 *
 * @author KonBAI
 * @date 2022-05-19
 */
@RequiredArgsConstructor
@Service
@Primary
public class WfCopyServiceImpl implements IWfCopyService {

    private final WfCopyMapper baseMapper;

    private final HistoryService historyService;
    private final RepositoryService repositoryService;

    /**
     * 查询流程抄送
     *
     * @param copyId 流程抄送主键
     * @return 流程抄送
     */
    @Override
    public WfCopyVo queryById(Long copyId){
        return baseMapper.selectVoById(copyId);
    }

    /**
     * 查询流程抄送列表
     *
     * @param bo 流程抄送
     * @return 流程抄送
     */
    @Override
    public TableDataInfo<WfCopyVo> selectPageList(WfCopyBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfCopy> lqw = buildQueryWrapper(bo);
        lqw.orderByDesc(WfCopy::getCreateTime);
        Page<WfCopyVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询流程抄送列表
     *
     * @param bo 流程抄送
     * @return 流程抄送
     */
    @Override
    public List<WfCopyVo> selectList(WfCopyBo bo) {
        LambdaQueryWrapper<WfCopy> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WfCopy> buildQueryWrapper(WfCopyBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WfCopy> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, WfCopy::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getProcessName()), WfCopy::getProcessName, bo.getProcessName());
        lqw.like(StringUtils.isNotBlank(bo.getOriginatorName()), WfCopy::getOriginatorName, bo.getOriginatorName());
        lqw.eq(Objects.nonNull(bo.getCopyId()), WfCopy::getCopyId, bo.getCopyId());
        lqw.like(StringUtils.isNotBlank(bo.getCopyNodeName()), WfCopy::getCopyNodeName, bo.getCopyNodeName());
        lqw.eq(Objects.nonNull(bo.getTaskId()), WfCopy::getTaskId, bo.getTaskId());
        lqw.between(Objects.nonNull(params.get("beginTime")) && Objects.nonNull(params.get("endTime")), WfCopy::getCreateTime, params.get("beginTime"), params.get("endTime"));
        return lqw;
    }

    @Override
    public Boolean makeCopy(WfTaskBo taskBo) {
        if (StringUtils.isBlank(taskBo.getCopyUserIds())) {
            // 若抄送用户为空，则不需要处理，返回成功
            return true;
        }
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(taskBo.getProcInsId()).singleResult();
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
        String[] ids = taskBo.getCopyUserIds().split(",");
        List<WfCopy> copyList = new ArrayList<>(ids.length);
        Long originatorId = LoginHelper.getUserId();
        String originatorName = LoginHelper.getLoginUser().getNickname();
        String title = historicProcessInstance.getProcessDefinitionName() + "-" + taskBo.getTaskName();
        for (String id : ids) {
            Long userId = Long.valueOf(id);
            WfCopy copy = new WfCopy();
            copy.setTitle(title);
            copy.setProcessId(historicProcessInstance.getProcessDefinitionId());
            copy.setProcessName(historicProcessInstance.getProcessDefinitionName());
            copy.setDeploymentId(historicProcessInstance.getDeploymentId());
            copy.setInstanceId(taskBo.getProcInsId());
            copy.setTaskId(taskBo.getTaskId());
            copy.setUserId(userId);
            copy.setOriginatorId(originatorId);
            copy.setOriginatorName(originatorName);
            copy.setProcessVersion(Objects.nonNull(pd) ? String.valueOf(pd.getVersion()) : "未知");
            copy.setCategory(Objects.nonNull(pd) ? pd.getCategory() : "");
            copy.setCopyNodeName(taskBo.getTaskName());
            copyList.add(copy);
        }
        return baseMapper.insertBatch(copyList);
    }
}
