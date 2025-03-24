package com.hdsolartech.business.controller;

import java.util.List;
import java.util.Map;

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
import com.hdsolartech.business.domain.vo.HdOrderInfoVo;
import com.hdsolartech.business.domain.bo.HdOrderInfoBo;
import com.hdsolartech.business.service.IHdOrderInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 销售订单信息
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/orderInfo")
public class HdOrderInfoController extends BaseController {

    private final IHdOrderInfoService hdOrderInfoService;

    @SaCheckPermission("system:user:leader")
    @GetMapping("/indexInfo")
    public R indexInfo() {
        Map map = hdOrderInfoService.queryIndexInfo();
        return R.ok(map);
    }

    /**
     * 查询销售订单信息列表
     */
    @SaCheckPermission("business:orderInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdOrderInfoVo> list(HdOrderInfoBo bo, PageQuery pageQuery) {
        return hdOrderInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出销售订单信息列表
     */
    @SaCheckPermission("business:orderInfo:export")
    @Log(title = "销售订单信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdOrderInfoBo bo, HttpServletResponse response) {
        List<HdOrderInfoVo> list = hdOrderInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "销售订单信息", HdOrderInfoVo.class, response);
    }

    /**
     * 获取销售订单信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:orderInfo:query")
    @GetMapping("/{id}")
    public R<HdOrderInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(hdOrderInfoService.queryById(id));
    }

    /**
     * 新增销售订单信息
     */
    @SaCheckPermission("business:orderInfo:add")
    @Log(title = "销售订单信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdOrderInfoBo bo) {
        return toAjax(hdOrderInfoService.insertByBo(bo));
    }

    /**
     * 修改销售订单信息
     */
    @SaCheckPermission("business:orderInfo:edit")
    @Log(title = "销售订单信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdOrderInfoBo bo) {
        return toAjax(hdOrderInfoService.updateByBo(bo));
    }

    /**
     * 删除销售订单信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:orderInfo:remove")
    @Log(title = "销售订单信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdOrderInfoService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 获取销售订单列表信息
     *
     * @param bo 订单信息bo
     */
   // @SaCheckPermission(value = {"business:orderInfo:query", "system:user:leader"}, mode = SaMode.OR)
    @GetMapping("/list/all")
    public R<List<HdOrderInfoVo>> getAllList(HdOrderInfoBo bo) {
        List<HdOrderInfoVo> list = hdOrderInfoService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 修改销售订单信息
     */
    @SaCheckPermission("business:orderInfo:edit")
    @Log(title = "销售进度信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/progress")
    public R<Void> progress(@Validated(EditGroup.class) @RequestBody HdOrderInfoBo bo) {
        return toAjax(hdOrderInfoService.progress(bo));
    }

    @GetMapping("/myOrderList")
    public R<List<HdOrderInfoVo>> getMyOrderList(HdOrderInfoBo bo) {
        List<HdOrderInfoVo> list = hdOrderInfoService.queryMyList(bo);
        return R.ok(list);
    }


}
