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
import com.hdsolartech.business.domain.vo.HdQuotationStatisticsVo;
import com.hdsolartech.business.domain.bo.HdQuotationStatisticsBo;
import com.hdsolartech.business.service.IHdQuotationStatisticsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目报价统计信息
 *
 * @author Lion Li
 * @date 2024-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/quotationStatistics")
public class HdQuotationStatisticsController extends BaseController {

    private final IHdQuotationStatisticsService hdQuotationStatisticsService;

    /**
     * 查询项目报价统计信息列表
     */
    @SaCheckPermission("business:quotationStatistics:list")
    @GetMapping("/list")
    public TableDataInfo<HdQuotationStatisticsVo> list(HdQuotationStatisticsBo bo, PageQuery pageQuery) {
        return hdQuotationStatisticsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目报价统计信息列表
     */
    @SaCheckPermission("business:quotationStatistics:export")
    @Log(title = "项目报价统计信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdQuotationStatisticsBo bo, HttpServletResponse response) {
        List<HdQuotationStatisticsVo> list = hdQuotationStatisticsService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目报价统计信息", HdQuotationStatisticsVo.class, response);
    }

    /**
     * 获取项目报价统计信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:quotationStatistics:query")
    @GetMapping("/{id}")
    public R<HdQuotationStatisticsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdQuotationStatisticsService.queryById(id));
    }

    /**
     * 新增项目报价统计信息
     */
    @SaCheckPermission("business:quotationStatistics:add")
    @Log(title = "项目报价统计信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdQuotationStatisticsBo bo) {
        return toAjax(hdQuotationStatisticsService.insertByBo(bo));
    }

    /**
     * 修改项目报价统计信息
     */
    @SaCheckPermission("business:quotationStatistics:edit")
    @Log(title = "项目报价统计信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdQuotationStatisticsBo bo) {
        return toAjax(hdQuotationStatisticsService.updateByBo(bo));
    }

    /**
     * 删除项目报价统计信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:quotationStatistics:remove")
    @Log(title = "项目报价统计信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdQuotationStatisticsService.deleteWithValidByIds(List.of(ids), true));
    }
}
