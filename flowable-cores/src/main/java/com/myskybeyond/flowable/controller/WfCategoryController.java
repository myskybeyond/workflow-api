package com.myskybeyond.flowable.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myskybeyond.flowable.domain.WfCategory;
import com.myskybeyond.flowable.domain.vo.DataGroupByCategoryVo;
import com.myskybeyond.flowable.domain.vo.WfCategoryVo;
import com.myskybeyond.flowable.service.IWfCategoryService;
import com.myskybeyond.flowable.utils.TaskUtils;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 流程分类Controller
 *
 * @author KonBAI
 * @createTime 2022/3/10 00:12
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/category")
public class WfCategoryController extends BaseController {

    private final IWfCategoryService categoryService;

    /**
     * 查询流程分类列表
     */
    @SaCheckPermission("workflow:category:list")
    @GetMapping("/list")
    public TableDataInfo<WfCategoryVo> list(WfCategory category, PageQuery pageQuery) {
        return categoryService.queryPageList(category, pageQuery);
    }

    /**
     * 查询全部的流程分类列表
     */
//    @SaCheckPermission("workflow:category:list")
    @SaCheckLogin
    @GetMapping("/listAll")
    public R<List<WfCategoryVo>> listAll(WfCategory category) {
        return R.ok(categoryService.queryList(category));
    }

    /**
     * 获取流程分类详细信息
     * @param categoryId 分类主键
     */
    @SaCheckPermission("workflow:category:query")
    @GetMapping("/{categoryId}")
    public R<WfCategoryVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("categoryId") Long categoryId) {
        return R.ok(categoryService.queryById(categoryId));
    }

    /**
     * 新增流程分类
     */
    @SaCheckPermission("workflow:category:add")
    @Log(title = "流程分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated @RequestBody WfCategory category) {
        if (!categoryService.checkCategoryCodeUnique(category)) {
            return R.fail("新增流程分类'" + category.getCategoryName() + "'失败，流程编码已存在");
        }
        return toAjax(categoryService.insertCategory(category));
    }

    /**
     * 修改流程分类
     */
    @SaCheckPermission("workflow:category:edit")
    @Log(title = "流程分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated @RequestBody WfCategory category) {
        if (!categoryService.checkCategoryCodeUnique(category)) {
            return R.fail("修改流程分类'" + category.getCategoryName() + "'失败，流程编码已存在");
        }
        return toAjax(categoryService.updateCategory(category));
    }

    /**
     * 删除流程分类
     * @param categoryIds 分类主键串
     */
    @SaCheckPermission("workflow:category:remove")
    @Log(title = "流程分类" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{categoryIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] categoryIds) {
        return toAjax(categoryService.deleteWithValidByIds(Arrays.asList(categoryIds), true));
    }

    @SaCheckPermission("workflow:model:list")
    @GetMapping("/totalModalGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalModalGroupByCategory() {
        return R.ok(categoryService.totalModalGroupByCategory());
    }

    @SaCheckPermission("workflow:deploy:list")
    @GetMapping("/totalDeploymentGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalDeploymentGroupByCategory() {
        return R.ok(categoryService.totalDeploymentGroupByCategory());
    }

    @SaCheckPermission("workflow:process:ownList")
    @GetMapping("/totalMyProcessGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalMyProcessGroupByCategory() {
        return R.ok(categoryService.totalMyProcessGroupByCategory(TaskUtils.getUserId()));
    }

    @SaCheckPermission("workflow:process:todoList")
    @GetMapping("/totalToDoTaskGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalToDoTaskGroupByCategory() {
        return R.ok(categoryService.totalToDoTaskGroupByCategory());
    }

    @SaCheckPermission("workflow:process:startList")
    @GetMapping("/totalFlowStartGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalFlowStartGroupByCategory() {
        return R.ok(categoryService.totalFlowStartGroupByCategory());
    }

    @SaCheckPermission("workflow:process:finishedList")
    @GetMapping("/totalFinishedGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalFinishedGroupByCategory() {
        return R.ok(categoryService.totalFinishedGroupByCategory());
    }

    @SaCheckPermission("workflow:process:claimList")
    @GetMapping("/totalClaimGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalClaimGroupByCategory() {
        return R.ok(categoryService.totalClaimGroupByCategory());
    }

    @SaCheckPermission("workflow:process:instances")
    @GetMapping("/totalProcessGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalProcessGroupByCategory() {
        return R.ok(categoryService.totalProcessGroupByCategory());
    }

    @SaCheckPermission("workflow:task:list")
    @GetMapping("/totalTaskGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalTaskGroupByCategory() {
        return R.ok(categoryService.totalTaskGroupByCategory());
    }

    @SaCheckPermission("workflow:form:list")
    @GetMapping("/totalFormGroupByCategory")
    public R<List<DataGroupByCategoryVo>> totalFormGroupByCategory() {
        return R.ok(categoryService.totalFormGroupByCategory());
    }
}
