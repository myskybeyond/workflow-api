package com.myskybeyond.flowable.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.myskybeyond.flowable.domain.WfDeployForm;
import com.myskybeyond.flowable.domain.WfForm;
import com.myskybeyond.flowable.domain.bo.WfFormBo;
import com.myskybeyond.flowable.domain.vo.WfFormVo;
import com.myskybeyond.flowable.mapper.WfCategoryMapper;
import com.myskybeyond.flowable.mapper.WfFormMapper;
import com.myskybeyond.flowable.service.AbstractService;
import com.myskybeyond.flowable.service.IWfFormService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 流程表单Service业务层处理
 *
 * @author KonBAI
 * @createTime 2022/3/7 22:07
 */
@Service
@Primary
@Slf4j
public class WfFormServiceImpl extends AbstractService implements IWfFormService {

    private final WfFormMapper baseMapper;

    public WfFormServiceImpl(WfCategoryMapper wfCategoryMapper,
                             WfFormMapper baseMapper) {
        super(wfCategoryMapper);
        this.baseMapper = baseMapper;
    }

    /**
     * 查询流程表单
     *
     * @param formId 流程表单ID
     * @return 流程表单
     */
    @Override
    public WfFormVo queryById(Long formId) {
        return baseMapper.selectVoById(formId);
    }

    /**
     * 查询流程表单列表
     *
     * @param bo 流程表单
     * @return 流程表单
     */
    @Override
    public TableDataInfo<WfFormVo> queryPageList(WfFormBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfForm> lqw = buildQueryWrapper(bo);
        Page<WfFormVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询流程表单列表
     *
     * @param bo 流程表单
     * @return 流程表单
     */
    @Override
    public List<WfFormVo> queryList(WfFormBo bo) {
        LambdaQueryWrapper<WfForm> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 新增流程表单
     *
     * @param bo 流程表单
     * @return 结果
     */
    @Override
    public int insertForm(WfFormBo bo) {
        WfForm wfForm = new WfForm();
        wfForm.setFormName(bo.getFormName());
        wfForm.setContent(bo.getContent());
        wfForm.setRemark(bo.getRemark());
        wfForm.setCategoryCode(bo.getCategoryCode());
        return baseMapper.insert(wfForm);
    }

    @Override
    public boolean checkFormNameUnique(WfFormBo form) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<WfForm>()
            .eq(WfForm::getFormName, form.getFormName())
            .ne(ObjectUtil.isNotNull(form.getFormId()), WfForm::getFormId, form.getFormId()));
        return !exist;
    }

    /**
     * 修改流程表单
     *
     * @param bo 流程表单
     * @return 结果
     */
    @Override
    public int updateForm(WfFormBo bo) {
        return baseMapper.update(new WfForm(), new LambdaUpdateWrapper<WfForm>()
            .set(StrUtil.isNotBlank(bo.getFormName()), WfForm::getFormName, bo.getFormName())
            .set(StrUtil.isNotBlank(bo.getContent()), WfForm::getContent, bo.getContent())
            .set(StrUtil.isNotBlank(bo.getRemark()), WfForm::getRemark, bo.getRemark())
            .set(StrUtil.isNotBlank(bo.getCategoryCode()), WfForm::getCategoryCode, bo.getCategoryCode())
            .eq(WfForm::getFormId, bo.getFormId()));
    }

    /**
     * 批量删除流程表单
     *
     * @param ids 需要删除的流程表单ID
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids) {
        /*
         * 目前只允许单个删除
         * 校验逻辑为：是否在流程部署中使用? 使用->不允许删除 未使用->允许删除
         */
        Long formId = ids.iterator().next();
        WfForm wfForm = baseMapper.selectById(formId);
        if (ObjectUtil.isNull(wfForm)) {
            log.error("删除流程表单失败,流程表单id: {} 不存在", formId);
            return false;
        } else {
            LambdaQueryWrapper<WfDeployForm> lqw = Wrappers.lambdaQuery(WfDeployForm.class).select(WfDeployForm::getDeployId).eq(WfDeployForm::getFormName, wfForm.getFormName());
            List<String> deployIds = SimpleQuery.list(lqw, WfDeployForm::getDeployId);
            if (ObjectUtil.isNotNull(deployIds) && !deployIds.isEmpty()) {
                log.error("流程表单id: {} 已被流程模型部署id: {} 使用,不允许删除", formId, String.join(StringUtils.SEPARATOR, deployIds));
                return false;
            } else {
                return baseMapper.deleteBatchIds(ids) > 0;
            }

        }
    }

    private LambdaQueryWrapper<WfForm> buildQueryWrapper(WfFormBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<WfForm> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFormName()), WfForm::getFormName, bo.getFormName());
        // 流程名称 查询本级及下级
        if (StringUtils.isNotBlank(bo.getCategoryCode())) {
            List<String> codes = categoryLevelAndLowerLevel1(bo.getCategoryCode());
            lqw.in(WfForm::getCategoryCode, codes);
        }
        lqw.orderByDesc(WfForm::getUpdateTime);
        return lqw;
    }
}
