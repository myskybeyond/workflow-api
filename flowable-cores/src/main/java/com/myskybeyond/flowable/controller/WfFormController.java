package com.myskybeyond.flowable.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myskybeyond.flowable.domain.bo.WfFormBo;
import com.myskybeyond.flowable.domain.vo.WfFormVo;
import com.myskybeyond.flowable.service.IWfDeployFormService;
import com.myskybeyond.flowable.service.IWfFormService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.QueryGroup;
import org.dromara.common.excel.utils.ExcelUtil;
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
 * 流程表单Controller
 *
 * @author KonBAI
 * @createTime 2022/3/7 22:07
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/form")
public class WfFormController extends BaseController {

    private final IWfFormService formService;

    private final IWfDeployFormService deployFormService;

    /**
     * 查询流程表单列表
     */
    @SaCheckPermission("workflow:form:list")
    @GetMapping("/list")
    public TableDataInfo<WfFormVo> list(@Validated(QueryGroup.class) WfFormBo bo, PageQuery pageQuery) {
        return formService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出流程表单列表
     */
    @SaCheckPermission("workflow:form:export")
    @Log(title = "流程表单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated WfFormBo bo, HttpServletResponse response) {
        List<WfFormVo> list = formService.queryList(bo);
        ExcelUtil.exportExcel(list, "流程表单", WfFormVo.class, response);
    }

    /**
     * 获取流程表单详细信息
     * @param formId 主键
     */
    @SaCheckPermission("workflow:form:query")
    @GetMapping(value = "/{formId}")
    public R<WfFormVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable("formId") Long formId) {
        return R.ok(formService.queryById(formId));
    }

    /**
     * 新增流程表单
     */
    @SaCheckPermission("workflow:form:add")
    @Log(title = "流程表单", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@RequestBody WfFormBo bo) {
        if (!formService.checkFormNameUnique(bo)) {
            return R.fail("新增流程表单'" + bo.getFormName() + "'失败，表单名称已存在");
        }
        return toAjax(formService.insertForm(bo));
    }

    /**
     * 修改流程表单
     */
    @SaCheckPermission("workflow:form:edit")
    @Log(title = "流程表单", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@RequestBody WfFormBo bo) {
        if (!formService.checkFormNameUnique(bo)) {
            return R.fail("修改流程表单'" + bo.getFormName() + "'失败，表单名称已存在");
        }
        return toAjax(formService.updateForm(bo));
    }

    /**
     * 删除流程表单
     * @param formIds 主键串
     */
    @SaCheckPermission("workflow:form:remove")
    @Log(title = "流程表单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{formIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] formIds) {
        return toAjax(formService.deleteWithValidByIds(Arrays.asList(formIds)) ? 1 : 0);
    }


    /**
     * 挂载流程表单
     */
//    @Log(title = "流程表单", businessType = BusinessType.INSERT)
//    @PostMapping("/addDeployForm")
//    public R<Void> addDeployForm(@RequestBody WfDeployForm deployForm) {
//        return toAjax(deployFormService.insertWfDeployForm(deployForm));
//    }
}
