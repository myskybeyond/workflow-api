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
import com.hdsolartech.business.domain.vo.HdProjectFileVersionInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectFileVersionInfoBo;
import com.hdsolartech.business.service.IHdProjectFileVersionInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目文件版本信息
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectFileVersionInfo")
public class HdProjectFileVersionInfoController extends BaseController {

    private final IHdProjectFileVersionInfoService hdProjectFileVersionInfoService;

    /**
     * 查询项目文件版本信息列表
     */
    @SaCheckPermission("business:projectFileVersionInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdProjectFileVersionInfoVo> list(HdProjectFileVersionInfoBo bo, PageQuery pageQuery) {
        return hdProjectFileVersionInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目文件版本信息列表
     */
    @SaCheckPermission("business:projectFileVersionInfo:export")
    @Log(title = "项目文件版本信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectFileVersionInfoBo bo, HttpServletResponse response) {
        List<HdProjectFileVersionInfoVo> list = hdProjectFileVersionInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目文件版本信息", HdProjectFileVersionInfoVo.class, response);
    }

    /**
     * 获取项目文件版本信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:projectFileVersionInfo:query")
    @GetMapping("/{id}")
    public R<HdProjectFileVersionInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectFileVersionInfoService.queryById(id));
    }

    /**
     * 新增项目文件版本信息
     */
    @SaCheckPermission("business:projectFileVersionInfo:add")
    @Log(title = "项目文件版本信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectFileVersionInfoBo bo) {
        return toAjax(hdProjectFileVersionInfoService.insertByBo(bo));
    }

    /**
     * 修改项目文件版本信息
     */
    @SaCheckPermission("business:projectFileVersionInfo:edit")
    @Log(title = "项目文件版本信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectFileVersionInfoBo bo) {
        return toAjax(hdProjectFileVersionInfoService.updateByBo(bo));
    }

    /**
     * 删除项目文件版本信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectFileVersionInfo:remove")
    @Log(title = "项目文件版本信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectFileVersionInfoService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 查询项目文件版本信息列表
     */
    @SaCheckPermission(value = {"business:projectFileVersionInfo:list", "business:projectInfo:detail","business:orderInfo:detail"}, mode = SaMode.OR)
    @GetMapping("/versionList")
    public R<List<HdProjectFileVersionInfoVo>> versionList( HdProjectFileVersionInfoBo bo ) {
        return R.ok(hdProjectFileVersionInfoService.queryList(bo));
    }
}
