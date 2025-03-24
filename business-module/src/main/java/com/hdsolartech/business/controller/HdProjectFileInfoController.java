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
import com.hdsolartech.business.domain.vo.HdProjectFileInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectFileInfoBo;
import com.hdsolartech.business.service.IHdProjectFileInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目文件信息
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectFileInfo")
public class HdProjectFileInfoController extends BaseController {

    private final IHdProjectFileInfoService hdProjectFileInfoService;

    /**
     * 查询项目文件信息列表
     */
    @SaCheckPermission(value = {"business:projectFileInfo:list", "business:projectInfo:detail","business:orderInfo:detail"}, mode = SaMode.OR)
    @GetMapping("/list")
    public TableDataInfo<HdProjectFileInfoVo> list(HdProjectFileInfoBo bo, PageQuery pageQuery) {
        return hdProjectFileInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目文件信息列表
     */
    @SaCheckPermission("business:projectFileInfo:export")
    @Log(title = "项目文件信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectFileInfoBo bo, HttpServletResponse response) {
        List<HdProjectFileInfoVo> list = hdProjectFileInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目文件信息", HdProjectFileInfoVo.class, response);
    }

    /**
     * 获取项目文件信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:projectFileInfo:query")
    @GetMapping("/{id}")
    public R<HdProjectFileInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectFileInfoService.queryById(id));
    }

    /**
     * 新增项目文件信息
     */
    @SaCheckPermission("business:projectFileInfo:add")
    @Log(title = "项目文件信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Long> add(@Validated(AddGroup.class) @RequestBody HdProjectFileInfoBo bo) {
        return R.ok(hdProjectFileInfoService.insertByBo(bo));
    }

    /**
     * 修改项目文件信息
     */
    @SaCheckPermission("business:projectFileInfo:edit")
    @Log(title = "项目文件信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectFileInfoBo bo) {
        return toAjax(hdProjectFileInfoService.updateByBo(bo));
    }

    /**
     * 删除项目文件信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectFileInfo:remove")
    @Log(title = "项目文件信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectFileInfoService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 查询项目文件信息列表
     */
    @GetMapping("/myList")
    public TableDataInfo<HdProjectFileInfoVo> myList(HdProjectFileInfoBo bo, PageQuery pageQuery) {
        return hdProjectFileInfoService.myList(bo, pageQuery);
    }


    @GetMapping("/getListByTaskId")
    public TableDataInfo<HdProjectFileInfoVo>  getListByTaskId(HdProjectFileInfoBo bo, PageQuery pageQuery){
        return hdProjectFileInfoService.getListByTaskId(bo, pageQuery);
    }

}
