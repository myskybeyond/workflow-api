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
import com.hdsolartech.business.domain.vo.HdProjectInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectInfoBo;
import com.hdsolartech.business.service.IHdProjectInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目信息
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectInfo")
public class HdProjectInfoController extends BaseController {

    private final IHdProjectInfoService hdProjectInfoService;

    /**
     * 查询项目信息列表
     */
    @SaCheckPermission("business:projectInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdProjectInfoVo> list(HdProjectInfoBo bo, PageQuery pageQuery) {
        return hdProjectInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目信息列表
     */
    @SaCheckPermission("business:projectInfo:export")
    @Log(title = "项目信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectInfoBo bo, HttpServletResponse response) {
        List<HdProjectInfoVo> list = hdProjectInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目信息", HdProjectInfoVo.class, response);
    }

    /**
     * 获取项目信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:projectInfo:query")
    @GetMapping("/{id}")
    public R<HdProjectInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectInfoService.queryById(id));
    }

    /**
     * 新增项目信息
     */
    @SaCheckPermission("business:projectInfo:add")
    @Log(title = "项目信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectInfoBo bo) {
        return toAjax(hdProjectInfoService.insertByBo(bo));
    }

    /**
     * 修改项目信息
     */
    @SaCheckPermission("business:projectInfo:edit")
    @Log(title = "项目信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectInfoBo bo) {
        return toAjax(hdProjectInfoService.updateByBo(bo));
    }

    /**
     * 删除项目信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectInfo:remove")
    @Log(title = "项目信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectInfoService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 查询项目信息列表
     */
   // @SaCheckPermission(value = {"business:projectInfo:list", "system:user:leader"}, mode = SaMode.OR)
    @GetMapping("/list/all")
    public R<List<HdProjectInfoVo>> listAll(HdProjectInfoBo bo) {
       return R.ok( hdProjectInfoService.queryList(bo));
    }

    /**
     * 查询项目信息列表
     */
    @GetMapping("/myProjectList")
    public R<List<HdProjectInfoVo>> getMyProjectList(HdProjectInfoBo bo) {
        return R.ok( hdProjectInfoService.queryMyList(bo));
    }


}
