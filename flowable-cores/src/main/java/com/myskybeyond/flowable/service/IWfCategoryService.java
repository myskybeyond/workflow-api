package com.myskybeyond.flowable.service;

import com.myskybeyond.flowable.domain.WfCategory;
import com.myskybeyond.flowable.domain.vo.DataGroupByCategoryVo;
import com.myskybeyond.flowable.domain.vo.WfCategoryVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 流程分类Service接口
 *
 * @author KonBAI
 * @date 2022-01-15
 */
public interface IWfCategoryService {
    /**
     * 查询单个
     * @return
     */
    WfCategoryVo queryById(Long categoryId);

    /**
     * 查询列表
     */
    TableDataInfo<WfCategoryVo> queryPageList(WfCategory category, PageQuery pageQuery);

    /**
     * 查询列表
     */
    List<WfCategoryVo> queryList(WfCategory category);

    /**
     * 新增流程分类
     *
     * @param category 流程分类信息
     * @return 结果
     */
    int insertCategory(WfCategory category);

    /**
     * 编辑流程分类
     * @param category 流程分类信息
     * @return 结果
     */
    int updateCategory(WfCategory category);

    /**
     * 校验并删除数据
     * @param ids 主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    int deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 校验分类编码是否唯一
     *
     * @param category 流程分类
     * @return 结果
     */
    boolean checkCategoryCodeUnique(WfCategory category);

    /**
     * 按流程分类统计流程模型
     * @return
     */
    List<DataGroupByCategoryVo> totalModalGroupByCategory();

    /**
     * 按流程分类统计流程部署
     * @return
     */
    List<DataGroupByCategoryVo> totalDeploymentGroupByCategory();

    /**
     * 按流程分类统计我的流程
     * @param userId 用户id
     * @return
     */
    List<DataGroupByCategoryVo> totalMyProcessGroupByCategory(String userId);

    /**
     * 按流程分类统计我的待办
     * @return
     */
    List<DataGroupByCategoryVo> totalToDoTaskGroupByCategory();

    /**
     * 按流程分类统计流程发起
     * @return
     */
    List<DataGroupByCategoryVo> totalFlowStartGroupByCategory();

    /**
     * 按流程分类统计我的已办
     * @return
     */
    List<DataGroupByCategoryVo> totalFinishedGroupByCategory();

    /**
     * 按流程分类统计我的待签
     * @return
     */
    List<DataGroupByCategoryVo> totalClaimGroupByCategory();

    /**
     * 按流程分类统计流程
     * @return
     */
    List<DataGroupByCategoryVo> totalProcessGroupByCategory();

    /**
     * 按流程分类统计任务
     * @return
     */
    List<DataGroupByCategoryVo> totalTaskGroupByCategory();

    /**
     * 按流程分类统计表单
     * @return
     */
    List<DataGroupByCategoryVo> totalFormGroupByCategory();

}
