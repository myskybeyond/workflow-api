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
import com.hdsolartech.business.domain.vo.HdTemplateInfoVo;
import com.hdsolartech.business.domain.bo.HdTemplateInfoBo;
import com.hdsolartech.business.service.IHdTemplateInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目资料模板
 *
 * @author Lion Li
 * @date 2024-05-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/templateInfo")
public class HdTemplateInfoController extends BaseController {

    private final IHdTemplateInfoService hdTemplateInfoService;

    /**
     * 查询项目资料模板列表
     */
//    @SaCheckPermission("business:templateInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdTemplateInfoVo> list(HdTemplateInfoBo bo, PageQuery pageQuery) {
        return hdTemplateInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目资料模板列表
     */
    @SaCheckPermission("business:templateInfo:export")
    @Log(title = "项目资料模板", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdTemplateInfoBo bo, HttpServletResponse response) {
        List<HdTemplateInfoVo> list = hdTemplateInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目资料模板", HdTemplateInfoVo.class, response);
    }

    /**
     * 获取项目资料模板详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:templateInfo:query")
    @GetMapping("/{id}")
    public R<HdTemplateInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdTemplateInfoService.queryById(id));
    }

    /**
     * 新增项目资料模板
     */
    @SaCheckPermission("business:templateInfo:add")
    @Log(title = "项目资料模板", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdTemplateInfoBo bo) {
        return toAjax(hdTemplateInfoService.insertByBo(bo));
    }

    /**
     * 修改项目资料模板
     */
    @SaCheckPermission("business:templateInfo:edit")
    @Log(title = "项目资料模板", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdTemplateInfoBo bo) {
        return toAjax(hdTemplateInfoService.updateByBo(bo));
    }

    /**
     * 删除项目资料模板
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:templateInfo:remove")
    @Log(title = "项目资料模板", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdTemplateInfoService.deleteWithValidByIds(List.of(ids), true));
    }



    /**
     * 状态修改
     */
    @SaCheckPermission("business:templateInfo:edit")
    @Log(title = "项目资料模板", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody HdTemplateInfoBo bo) {
        return toAjax(hdTemplateInfoService.updateStatus(bo.getId(), bo.getStatus()));
    }


}
