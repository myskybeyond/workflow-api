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
import com.hdsolartech.business.domain.vo.HdProjectProgressStatisticsVo;
import com.hdsolartech.business.domain.bo.HdProjectProgressStatisticsBo;
import com.hdsolartech.business.service.IHdProjectProgressStatisticsService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目进度统计信息
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectProgressStatistics")
public class HdProjectProgressStatisticsController extends BaseController {

    private final IHdProjectProgressStatisticsService hdProjectProgressStatisticsService;

    /**
     * 查询项目进度统计信息列表
     */
    @SaCheckPermission("business:projectProgressStatistics:list")
    @GetMapping("/list")
    public TableDataInfo<HdProjectProgressStatisticsVo> list(HdProjectProgressStatisticsBo bo, PageQuery pageQuery) {
        return hdProjectProgressStatisticsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目进度统计信息列表
     */
    @SaCheckPermission("business:projectProgressStatistics:export")
    @Log(title = "项目进度统计信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectProgressStatisticsBo bo, HttpServletResponse response) {
        List<HdProjectProgressStatisticsVo> list = hdProjectProgressStatisticsService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目进度统计信息", HdProjectProgressStatisticsVo.class, response);
    }

    /**
     * 获取项目进度统计信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:projectProgressStatistics:query")
    @GetMapping("/{id}")
    public R<HdProjectProgressStatisticsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectProgressStatisticsService.queryById(id));
    }

    /**
     * 新增项目进度统计信息
     */
    @SaCheckPermission("business:projectProgressStatistics:add")
    @Log(title = "项目进度统计信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectProgressStatisticsBo bo) {
        return toAjax(hdProjectProgressStatisticsService.insertByBo(bo));
    }

    /**
     * 修改项目进度统计信息
     */
    @SaCheckPermission("business:projectProgressStatistics:edit")
    @Log(title = "项目进度统计信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectProgressStatisticsBo bo) {
        return toAjax(hdProjectProgressStatisticsService.updateByBo(bo));
    }

    /**
     * 删除项目进度统计信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectProgressStatistics:remove")
    @Log(title = "项目进度统计信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectProgressStatisticsService.deleteWithValidByIds(List.of(ids), true));
    }
}
