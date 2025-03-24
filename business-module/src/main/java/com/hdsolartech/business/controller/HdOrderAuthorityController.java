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
import com.hdsolartech.business.domain.vo.HdOrderAuthorityVo;
import com.hdsolartech.business.domain.bo.HdOrderAuthorityBo;
import com.hdsolartech.business.service.IHdOrderAuthorityService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 订单权限信息
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/orderAuthority")
public class HdOrderAuthorityController extends BaseController {

    private final IHdOrderAuthorityService hdOrderAuthorityService;

    /**
     * 查询订单权限信息列表
     */
    @SaCheckPermission("business:orderAuthority:list")
    @GetMapping("/list")
    public TableDataInfo<HdOrderAuthorityVo> list(HdOrderAuthorityBo bo, PageQuery pageQuery) {
        return hdOrderAuthorityService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出订单权限信息列表
     */
    @SaCheckPermission("business:orderAuthority:export")
    @Log(title = "订单权限信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdOrderAuthorityBo bo, HttpServletResponse response) {
        List<HdOrderAuthorityVo> list = hdOrderAuthorityService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单权限信息", HdOrderAuthorityVo.class, response);
    }

    /**
     * 获取订单权限信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:orderAuthority:query")
    @GetMapping("/{id}")
    public R<HdOrderAuthorityVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdOrderAuthorityService.queryById(id));
    }

    /**
     * 新增订单权限信息
     */
    @SaCheckPermission("business:orderAuthority:add")
    @Log(title = "订单权限信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdOrderAuthorityBo bo) {
        return toAjax(hdOrderAuthorityService.insertByBo(bo));
    }

    /**
     * 修改订单权限信息
     */
    @SaCheckPermission("business:orderAuthority:edit")
    @Log(title = "订单权限信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdOrderAuthorityBo bo) {
        return toAjax(hdOrderAuthorityService.updateByBo(bo));
    }

    /**
     * 删除订单权限信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:orderAuthority:remove")
    @Log(title = "订单权限信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdOrderAuthorityService.deleteWithValidByIds(List.of(ids), true));
    }
}
