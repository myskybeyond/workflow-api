package com.myskybeyond.flowable.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.flowable.domain.WfCategory;
import com.myskybeyond.flowable.domain.vo.DataGroupByCategoryVo;
import com.myskybeyond.flowable.domain.vo.WfCategoryVo;
import com.myskybeyond.flowable.mapper.WfCategoryMapper;
import com.myskybeyond.flowable.service.AbstractService;
import com.myskybeyond.flowable.service.IWfCategoryService;
import com.myskybeyond.flowable.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.flowable.engine.RepositoryService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 流程分类Service业务层处理
 *
 * @author KonBAI
 * @date 2022-01-15
 */
@Service
@Primary
@Slf4j
public class WfCategoryServiceImpl extends AbstractService implements IWfCategoryService {

    private final WfCategoryMapper baseMapper;
    private final RepositoryService repositoryService;
    ;

    public WfCategoryServiceImpl(WfCategoryMapper wfCategoryMapper,
                                 RepositoryService repositoryService) {
        super(wfCategoryMapper);
        this.repositoryService = repositoryService;
        this.baseMapper = wfCategoryMapper;
    }

    @Override
    public WfCategoryVo queryById(Long categoryId) {
        return baseMapper.selectVoById(categoryId);
    }

    @Override
    public TableDataInfo<WfCategoryVo> queryPageList(WfCategory category, PageQuery pageQuery) {
        LambdaQueryWrapper<WfCategory> lqw = buildQueryWrapper(category);
        Page<WfCategoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<WfCategoryVo> queryList(WfCategory category) {
        LambdaQueryWrapper<WfCategory> lqw = buildQueryWrapper(category);
        lqw.orderByAsc(WfCategory::getCategoryPid).orderByAsc(WfCategory::getSort);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<WfCategory> buildQueryWrapper(WfCategory category) {
        Map<String, Object> params = category.getParams();
        LambdaQueryWrapper<WfCategory> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(category.getCategoryName()), WfCategory::getCategoryName, category.getCategoryName());
        lqw.eq(StringUtils.isNotBlank(category.getCode()), WfCategory::getCode, category.getCode());
        return lqw;
    }

    @Override
    public int insertCategory(WfCategory categoryBo) {
        WfCategory add = BeanUtil.toBean(categoryBo, WfCategory.class);
        return baseMapper.insert(add);
    }

    @Override
    public int updateCategory(WfCategory categoryBo) {
        WfCategory update = BeanUtil.toBean(categoryBo, WfCategory.class);
        return baseMapper.updateById(update);
    }

    @Override
    public int deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        /*
         * 目前只允许单个删除
         * 校验逻辑为：是否存在子级分类,存在->不允许删除,
         * 不存在-是否在流程模型中使用？使用->不允许删除 未使用->可以删除
         */
        if (isValid) {
            Long categoryId = ids.iterator().next();
            WfCategory wfCategory = baseMapper.selectById(categoryId);
            if (Objects.nonNull(wfCategory)) {
                // 是否存在下级
                List<String> lowerList = categoryLevelAndLowerLevel1(String.valueOf(categoryId));
                if (Objects.nonNull(lowerList) && lowerList.size() > 1) {
                    // 存在下级
                    for (String _categoryId : lowerList) {
                        long modelNumber = repositoryService.createModelQuery().modelCategory(_categoryId).count();
                        if (modelNumber > 0) {
                            log.warn("流程分类id: {} 存在下级且下级中流程分类id为: {}对象已使用,不允许删除", categoryId, _categoryId);
                            break;
                        }
                    }
                } else {
                    // 不存在下级
                    long modelNumber = repositoryService.createModelQuery().modelCategory(wfCategory.getCode()).count();
                    if (modelNumber > 0) {
                        log.warn("流程编号: {} 已使用,不允许删除", wfCategory.getCode());
                    } else {
                        log.info("删除流程分类,流程编号: {}", ids);
                        return baseMapper.deleteBatchIds(ids);
                    }
                }
            } else {
                log.error("流程分类id: {} 的对象不存在", categoryId);
            }
        } else {
            log.warn("未进行业务校验直接删除流程分类,流程分类id: {}", ids);
            return baseMapper.deleteBatchIds(ids);
        }
        return 0;
    }


    /**
     * 校验分类编码是否唯一
     *
     * @param category 流程分类
     * @return 结果
     */
    @Override
    public boolean checkCategoryCodeUnique(WfCategory category) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<WfCategory>()
            .eq(WfCategory::getCode, category.getCode())
            .ne(ObjectUtil.isNotNull(category.getCategoryId()), WfCategory::getCategoryId, category.getCategoryId()));
        return !exist;
    }

    @Override
    public List<DataGroupByCategoryVo> totalModalGroupByCategory() {
        return baseMapper.totalModalGroupByCategory();
    }

    @Override
    public List<DataGroupByCategoryVo> totalDeploymentGroupByCategory() {
        return baseMapper.totalDeploymentGroupByCategory();
    }

    @Override
    public List<DataGroupByCategoryVo> totalMyProcessGroupByCategory(String userId) {
        if (StrUtil.isEmpty(userId)) {
            log.error("【按流程分类统计我的流程】发生错误:用户参数： {}为空", userId);
            throw new ServiceException("用户参数为空");
        }
        return baseMapper.totalMyProcessGroupByCategory(userId);
    }

    @Override
    public List<DataGroupByCategoryVo> totalToDoTaskGroupByCategory() {
        return baseMapper.totalToDoTaskGroupByCategory(TaskUtils.getUserId());
    }

    @Override
    public List<DataGroupByCategoryVo> totalFlowStartGroupByCategory() {
        String totalSql = "select RES.CATEGORY_ as `code`,count(1) as `total` from ACT_RE_PROCDEF RES WHERE RES.VERSION_ = (select max(VERSION_) from ACT_RE_PROCDEF where KEY_ = RES.KEY_ and ( (TENANT_ID_ IS NOT NULL and TENANT_ID_ = RES.TENANT_ID_) or (TENANT_ID_ IS NULL and RES.TENANT_ID_ IS NULL) ) ) and (RES.SUSPENSION_STATE_ = 1) group by RES.CATEGORY_";
        return baseMapper.totalExecutor(totalSql);
    }

    @Override
    public List<DataGroupByCategoryVo> totalFinishedGroupByCategory() {
        String totalSql = "select tt2.CATEGORY_ as `code`,count(tt1.ID_) as `total` from (select RES.* from  ACT_HI_TASKINST RES WHERE RES.ASSIGNEE_ = {} and RES.END_TIME_ is not null) tt1 left join act_re_procdef tt2 on tt1.PROC_DEF_ID_=tt2.ID_ GROUP BY tt2.CATEGORY_";
        return baseMapper.totalExecutor(StrUtil.format(totalSql, TaskUtils.getUserId()));
    }

    @Override
    public List<DataGroupByCategoryVo> totalClaimGroupByCategory() {
        String totalSql = "select tt2.CATEGORY_ as `code`,count(1) as `total` from \n" +
            "\t(SELECT\n" +
            "\t\tRES.* \n" +
            "\tFROM\n" +
            "\t\tACT_RU_TASK RES \n" +
            "\tWHERE\n" +
            "\t\tRES.ASSIGNEE_ IS NULL \n" +
            "\t\tAND EXISTS (\n" +
            "\t\tSELECT\n" +
            "\t\t\tLINK.ID_ \n" +
            "\t\tFROM\n" +
            "\t\t\tACT_RU_IDENTITYLINK LINK \n" +
            "\t\tWHERE\n" +
            "\t\t\tLINK.TYPE_ = 'candidate' \n" +
            "\t\t\tAND LINK.TASK_ID_ = RES.ID_ \n" +
            "\t\t\tAND ( LINK.USER_ID_ = {} OR ( LINK.GROUP_ID_ IN ( {} ) ) ) \n" +
            "\t\t) \n" +
            "\t\tAND RES.SUSPENSION_STATE_ = 1) tt1 left join act_re_procdef tt2 on tt1.proc_def_id_=tt2.ID_ GROUP BY tt2.CATEGORY_";
        return baseMapper.totalExecutor(StrUtil.format(totalSql, TaskUtils.getUserId(), getCandidateGroupStr(TaskUtils.getCandidateGroup())));
    }

    @Override
    public List<DataGroupByCategoryVo> totalProcessGroupByCategory() {
        return baseMapper.totalMyProcessGroupByCategory(null);
    }

    @Override
    public List<DataGroupByCategoryVo> totalTaskGroupByCategory() {
        String totalSql = "select t1.CATEGORY_ as `code`,count(RES.ID_) as total  from act_ru_task RES left join act_re_procdef t1 on RES.PROC_DEF_ID_=t1.ID_ GROUP BY t1.CATEGORY_";
        return baseMapper.totalExecutor(totalSql);
    }

    @Override
    public List<DataGroupByCategoryVo> totalFormGroupByCategory() {
        String totalSql = "SELECT category_code as `code`,count(1) as total FROM `wf_form` where del_flag='0' GROUP BY category_code";
        return baseMapper.totalExecutor(totalSql);
    }

    /**
     * 拼sql in条件
     *
     * @return candidateGroup
     */
    private String getCandidateGroupStr(List<String> candidateGroupList) {
        List<String> quotedList = candidateGroupList.stream()
            .map(s -> "'" + s + "'")
            .collect(Collectors.toList());
        return String.join(StringUtils.SEPARATOR, quotedList);
    }
}
