package com.hdsolartech.business.controller;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.hdsolartech.business.domain.bo.HdCategoryInfoBo;
import com.hdsolartech.business.domain.vo.HdCategoryInfoVo;
import com.hdsolartech.business.service.IHdCategoryInfoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统类别信息
 *
 * @author Lion Li
 * @date 2024-05-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/categoryInfo")
public class HdCategoryInfoController extends BaseController {

    private final IHdCategoryInfoService hdCategoryInfoService;

    /**
     * 查询系统类别信息列表
     */
    @SaCheckPermission("business:categoryInfo:list")
    @GetMapping("/list")
    public R<List<HdCategoryInfoVo>> list(HdCategoryInfoBo bo) {
        List<HdCategoryInfoVo> list = hdCategoryInfoService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 导出系统类别信息列表
     */
    @SaCheckPermission("business:categoryInfo:export")
    @Log(title = "系统类别信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdCategoryInfoBo bo, HttpServletResponse response) {
        List<HdCategoryInfoVo> list = hdCategoryInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "系统类别信息", HdCategoryInfoVo.class, response);
    }

    /**
     * 获取系统类别信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:categoryInfo:query")
    @GetMapping("/{id}")
    public R<HdCategoryInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdCategoryInfoService.queryById(id));
    }

    /**
     * 新增系统类别信息
     */
    @SaCheckPermission("business:categoryInfo:add")
    @Log(title = "系统类别信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdCategoryInfoBo bo) {
        return toAjax(hdCategoryInfoService.insertByBo(bo));
    }

    /**
     * 修改系统类别信息
     */
    @SaCheckPermission("business:categoryInfo:edit")
    @Log(title = "系统类别信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdCategoryInfoBo bo) {
        return toAjax(hdCategoryInfoService.updateByBo(bo));
    }

    /**
     * 删除系统类别信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:categoryInfo:remove")
    @Log(title = "系统类别信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        if (hdCategoryInfoService.hasChildById(ids[0])) {
            return R.warn("存在下级分类,不允许删除");
        }
        if (hdCategoryInfoService.checkExistComponent(ids[0])) {
            return R.warn("分类存在组件,不允许删除");
        }
        return toAjax(hdCategoryInfoService.deleteWithValidByIds(List.of(ids), true));
    }


    /**
     * 查询系统类别信息列表
     */
    @SaCheckPermission(value = {"business:categoryInfo:list", "business:projectInfo:detail","business:orderInfo:detail"}, mode = SaMode.OR)
    @GetMapping("/numList")
    public R<List<HdCategoryInfoVo>> numList(HdCategoryInfoBo bo) {
        List<HdCategoryInfoVo> list = hdCategoryInfoService.queryNumList(bo);
        return R.ok(list);
    }

    /**
     * 查询系统类别信息列表
     */
    @GetMapping("/fileNumList")
    public R<List<HdCategoryInfoVo>> fileNumList(HdCategoryInfoBo bo) {
        List<HdCategoryInfoVo> list = hdCategoryInfoService.queryFileNumList(bo);
        return R.ok(list);
    }
    /**
     * 查询系统类别信息列表
     */
    @GetMapping("/myFileNumList")
    public R<List<HdCategoryInfoVo>> myFileNumList(HdCategoryInfoBo bo) {
        List<HdCategoryInfoVo> list = hdCategoryInfoService.queryMyFileNumList(bo);
        return R.ok(list);
    }

}
