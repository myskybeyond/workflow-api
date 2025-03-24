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
import com.hdsolartech.business.domain.vo.HdProjectDeliveryInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectDeliveryInfoBo;
import com.hdsolartech.business.service.IHdProjectDeliveryInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目发货信息
 *
 * @author Lion Li
 * @date 2024-06-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectDeliveryInfo")
public class HdProjectDeliveryInfoController extends BaseController {

    private final IHdProjectDeliveryInfoService hdProjectDeliveryInfoService;

    /**
     * 查询项目发货信息列表
     */
    @SaCheckPermission("business:projectDeliveryInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdProjectDeliveryInfoVo> list(HdProjectDeliveryInfoBo bo, PageQuery pageQuery) {
        return hdProjectDeliveryInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目发货信息列表
     */
    @SaCheckPermission("business:projectDeliveryInfo:export")
    @Log(title = "项目发货信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectDeliveryInfoBo bo, HttpServletResponse response) {
        List<HdProjectDeliveryInfoVo> list = hdProjectDeliveryInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目发货信息", HdProjectDeliveryInfoVo.class, response);
    }

    /**
     * 获取项目发货信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:projectDeliveryInfo:query")
    @GetMapping("/{id}")
    public R<HdProjectDeliveryInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectDeliveryInfoService.queryById(id));
    }

    /**
     * 新增项目发货信息
     */
    @SaCheckPermission("business:projectDeliveryInfo:add")
    @Log(title = "项目发货信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectDeliveryInfoBo bo) {
        return toAjax(hdProjectDeliveryInfoService.insertByBo(bo));
    }

    /**
     * 修改项目发货信息
     */
    @SaCheckPermission("business:projectDeliveryInfo:edit")
    @Log(title = "项目发货信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectDeliveryInfoBo bo) {
        return toAjax(hdProjectDeliveryInfoService.updateByBo(bo));
    }

    /**
     * 删除项目发货信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectDeliveryInfo:remove")
    @Log(title = "项目发货信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectDeliveryInfoService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 同步流程
     */
    @PostMapping("/sync")
    public R<Void> sync(@RequestBody HdProjectDeliveryInfoBo bo) {
        return toAjax(hdProjectDeliveryInfoService.sync(bo));
    }
}
