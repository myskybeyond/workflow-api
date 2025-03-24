package com.hdsolartech.business.controller;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.hdsolartech.business.domain.vo.HdProjectFlwRelationVo;
import com.hdsolartech.business.domain.bo.HdProjectFlwRelationBo;
import com.hdsolartech.business.service.IHdProjectFlwRelationService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目流程关系
 *
 * @author Lion Li
 * @date 2024-06-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/projectFlwRelation")
public class HdProjectFlwRelationController extends BaseController {

    private final IHdProjectFlwRelationService hdProjectFlwRelationService;
    /**
     * 查询项目流程关系列表
     */
    @SaCheckPermission("system:user:leader")
    @GetMapping("/tracking")
    public R tracking(HdProjectFlwRelationBo bo, PageQuery pageQuery) {
        return  R.ok(hdProjectFlwRelationService.queryTrackingPageList(bo, pageQuery));
    }
    /**
     * 查询项目流程关系列表
     */
    @SaCheckPermission("system:projectFlwRelation:list")
    @GetMapping("/list")
    public TableDataInfo<HdProjectFlwRelationVo> list(HdProjectFlwRelationBo bo, PageQuery pageQuery) {
        return hdProjectFlwRelationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目流程关系列表
     */
    @SaCheckPermission("system:projectFlwRelation:export")
    @Log(title = "项目流程关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectFlwRelationBo bo, HttpServletResponse response) {
        List<HdProjectFlwRelationVo> list = hdProjectFlwRelationService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目流程关系", HdProjectFlwRelationVo.class, response);
    }

    /**
     * 获取项目流程关系详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:projectFlwRelation:query")
    @GetMapping("/{id}")
    public R<HdProjectFlwRelationVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectFlwRelationService.queryById(id));
    }

    /**
     * 新增项目流程关系
     */
    @SaCheckPermission("system:projectFlwRelation:add")
    @Log(title = "项目流程关系", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectFlwRelationBo bo) {
        return toAjax(hdProjectFlwRelationService.insertByBo(bo));
    }

    /**
     * 修改项目流程关系
     */
    @SaCheckPermission("system:projectFlwRelation:edit")
    @Log(title = "项目流程关系", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectFlwRelationBo bo) {
        return toAjax(hdProjectFlwRelationService.updateByBo(bo));
    }

    /**
     * 删除项目流程关系
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:projectFlwRelation:remove")
    @Log(title = "项目流程关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectFlwRelationService.deleteWithValidByIds(List.of(ids), true));
    }
}
