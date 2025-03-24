package com.hdsolartech.business.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.common.util.luckysheet.XlsUtil;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.common.core.service.LuckySheetService;
import org.dromara.common.core.utils.SpringUtils;
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
import com.hdsolartech.business.domain.vo.HdOrderFlwRelationVo;
import com.hdsolartech.business.domain.bo.HdOrderFlwRelationBo;
import com.hdsolartech.business.service.IHdOrderFlwRelationService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 订单流程关系
 *
 * @author Lion Li
 * @date 2024-06-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/orderFlwRelation")
public class HdOrderFlwRelationController extends BaseController {

    private final IHdOrderFlwRelationService hdOrderFlwRelationService;

    /**
     * 下单进度跟踪列表
     */
    @SaCheckPermission("system:user:leader")
    @GetMapping("/list")
    public R list(HdOrderFlwRelationBo bo, PageQuery pageQuery) {
        return R.ok(hdOrderFlwRelationService.queryPageList(bo, pageQuery));
    }

    /**
     * 导出订单流程关系列表
     */
    @SaCheckPermission("system:orderFlwRelation:export")
    @Log(title = "订单流程关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdOrderFlwRelationBo bo, HttpServletResponse response) {
        List<HdOrderFlwRelationVo> list = hdOrderFlwRelationService.queryList(bo);
        ExcelUtil.exportExcel(list, "订单流程关系", HdOrderFlwRelationVo.class, response);
    }

    /**
     * 获取订单流程关系详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:orderFlwRelation:query")
    @GetMapping("/{id}")
    public R<HdOrderFlwRelationVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdOrderFlwRelationService.queryById(id));
    }

    /**
     * 新增订单流程关系
     */
    @SaCheckPermission("system:orderFlwRelation:add")
    @Log(title = "订单流程关系", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdOrderFlwRelationBo bo) {
        return toAjax(hdOrderFlwRelationService.insertByBo(bo));
    }

    /**
     * 修改订单流程关系
     */
    @SaCheckPermission("system:orderFlwRelation:edit")
    @Log(title = "订单流程关系", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdOrderFlwRelationBo bo) {
        return toAjax(hdOrderFlwRelationService.updateByBo(bo));
    }

    /**
     * 删除订单流程关系
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:orderFlwRelation:remove")
    @Log(title = "订单流程关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdOrderFlwRelationService.deleteWithValidByIds(List.of(ids), true));
    }

    @SaIgnore
    @GetMapping("/gen")
    public   R generateExcel(){
        //生成新的excel文件
        List<JSONObject>  list =   SpringUtils.getBean(LuckySheetService.class).getLuckyDataS(1805081658932580354L,1L);


        OutputStream out = null;
        try {
            out = new FileOutputStream("D:/excel/test1.xlsx");
            XlsUtil.exportXlsFile(out,true,list);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return R.ok();
    }


}
