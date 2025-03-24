package com.hdsolartech.business.controller;

import java.util.List;

import cn.dev33.satoken.annotation.SaMode;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import com.hdsolartech.business.domain.vo.HdProjectOperLogVo;
import com.hdsolartech.business.domain.bo.HdProjectOperLogBo;
import com.hdsolartech.business.service.IHdProjectOperLogService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目操作信息
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectOperLog")
public class HdProjectOperLogController extends BaseController {

    private final IHdProjectOperLogService hdProjectOperLogService;

    /**
     * 查询项目操作信息列表
     */
    @SaCheckPermission(value = {"business:projectOperLog:list", "business:projectInfo:detail","business:orderInfo:detail"}, mode = SaMode.OR)
    @GetMapping("/list")
    public TableDataInfo<HdProjectOperLogVo> list(HdProjectOperLogBo bo, PageQuery pageQuery) {
        return hdProjectOperLogService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目操作信息列表
     */
    @SaCheckPermission("business:projectOperLog:export")
    @Log(title = "项目操作信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectOperLogBo bo, HttpServletResponse response) {
        List<HdProjectOperLogVo> list = hdProjectOperLogService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目操作信息", HdProjectOperLogVo.class, response);
    }

    /**
     * 获取项目操作信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:projectOperLog:query")
    @GetMapping("/{id}")
    public R<HdProjectOperLogVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectOperLogService.queryById(id));
    }

    /**
     * 新增项目操作信息
     */
    @SaCheckPermission("business:projectOperLog:add")
    @Log(title = "项目操作信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectOperLogBo bo) {
        return toAjax(hdProjectOperLogService.insertByBo(bo));
    }

    /**
     * 修改项目操作信息
     */
    @SaCheckPermission("business:projectOperLog:edit")
    @Log(title = "项目操作信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectOperLogBo bo) {
        return toAjax(hdProjectOperLogService.updateByBo(bo));
    }

    /**
     * 删除项目操作信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectOperLog:remove")
    @Log(title = "项目操作信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectOperLogService.deleteWithValidByIds(List.of(ids), true));
    }
}
