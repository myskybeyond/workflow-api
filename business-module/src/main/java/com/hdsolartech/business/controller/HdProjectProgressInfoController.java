package com.hdsolartech.business.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.hdsolartech.business.domain.bo.HdProjectProgressInfoBo;
import com.hdsolartech.business.domain.vo.HdProjectProgressInfoVo;
import com.hdsolartech.business.service.IHdProjectProgressInfoService;
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
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目进度信息
 *
 * @author Lion Li
 * @date 2024-06-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectProgressInfo")
public class HdProjectProgressInfoController extends BaseController {

    private final IHdProjectProgressInfoService hdProjectProgressInfoService;

    /**
     * 查询项目进度信息列表
     */
    @SaCheckPermission("business:projectProgressInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdProjectProgressInfoVo> list(HdProjectProgressInfoBo bo, PageQuery pageQuery) {
        return hdProjectProgressInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目进度信息列表
     */
    @SaCheckPermission("business:projectProgressInfo:export")
    @Log(title = "项目进度信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectProgressInfoBo bo, HttpServletResponse response) {
        List<HdProjectProgressInfoVo> list = hdProjectProgressInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目进度信息", HdProjectProgressInfoVo.class, response);
    }

    /**
     * 获取项目进度信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:projectProgressInfo:query")
    @GetMapping("/{id}")
    public R<HdProjectProgressInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectProgressInfoService.queryById(id));
    }

    /**
     * 新增项目进度信息
     */
    @SaCheckPermission("business:projectProgressInfo:add")
    @Log(title = "项目进度信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectProgressInfoBo bo) {
        return toAjax(hdProjectProgressInfoService.insertByBo(bo));
    }

    /**
     * 修改项目进度信息
     */
    @SaCheckPermission("business:projectProgressInfo:edit")
    @Log(title = "项目进度信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectProgressInfoBo bo) {
        return toAjax(hdProjectProgressInfoService.updateByBo(bo));
    }

    /**
     * 删除项目进度信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectProgressInfo:remove")
    @Log(title = "项目进度信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectProgressInfoService.deleteWithValidByIds(List.of(ids), true));
    }


    /**
     * 同步流程
     */
    @PostMapping("/sync")
    public R<Void> sync(@RequestBody HdProjectProgressInfoBo bo) {
        return toAjax(hdProjectProgressInfoService.sync(bo));
    }

    @GetMapping("/list/all")
    public R<List<HdProjectProgressInfoVo>> getAllList(HdProjectProgressInfoBo bo){
        return R.ok( hdProjectProgressInfoService.getAllList(bo));
    }

}
