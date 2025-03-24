package com.hdsolartech.business.controller;

import java.util.List;

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
import com.hdsolartech.business.domain.vo.HdProjectProgressExtendInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectProgressExtendInfoBo;
import com.hdsolartech.business.service.IHdProjectProgressExtendInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目进度扩展信息
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectProgressExtendInfo")
public class HdProjectProgressExtendInfoController extends BaseController {

    private final IHdProjectProgressExtendInfoService hdProjectProgressExtendInfoService;

    /**
     * 查询项目进度扩展信息列表
     */
    @SaCheckPermission("business:projectProgressExtendInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdProjectProgressExtendInfoVo> list(HdProjectProgressExtendInfoBo bo, PageQuery pageQuery) {
        return hdProjectProgressExtendInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目进度扩展信息列表
     */
    @SaCheckPermission("business:projectProgressExtendInfo:export")
    @Log(title = "项目进度扩展信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectProgressExtendInfoBo bo, HttpServletResponse response) {
        List<HdProjectProgressExtendInfoVo> list = hdProjectProgressExtendInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目进度扩展信息", HdProjectProgressExtendInfoVo.class, response);
    }

    /**
     * 获取项目进度扩展信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:projectProgressExtendInfo:query")
    @GetMapping("/{id}")
    public R<HdProjectProgressExtendInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectProgressExtendInfoService.queryById(id));
    }

    /**
     * 新增项目进度扩展信息
     */
    @SaCheckPermission("business:projectProgressExtendInfo:add")
    @Log(title = "项目进度扩展信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectProgressExtendInfoBo bo) {
        return toAjax(hdProjectProgressExtendInfoService.insertByBo(bo));
    }

    /**
     * 修改项目进度扩展信息
     */
    @SaCheckPermission("business:projectProgressExtendInfo:edit")
    @Log(title = "项目进度扩展信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectProgressExtendInfoBo bo) {
        return toAjax(hdProjectProgressExtendInfoService.updateByBo(bo));
    }

    /**
     * 删除项目进度扩展信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectProgressExtendInfo:remove")
    @Log(title = "项目进度扩展信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectProgressExtendInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
