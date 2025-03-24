package com.hdsolartech.business.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.hdsolartech.business.domain.bo.HdComponentInfoBo;
import com.hdsolartech.business.domain.bo.HdTemplateInfoBo;
import com.hdsolartech.business.domain.vo.HdComponentInfoVo;
import com.hdsolartech.business.listener.ComponentImportListener;
import com.hdsolartech.business.service.IHdComponentInfoService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.core.ExcelResult;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 构件信息
 *
 * @author Lion Li
 * @date 2024-05-29
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/componentInfo")
public class HdComponentInfoController extends BaseController {

    private final IHdComponentInfoService hdComponentInfoService;

    /**
     * 查询构件信息列表(树)
     */
    @SaCheckPermission("business:componentInfo:list")
    @GetMapping("/list")
    public R<List<HdComponentInfoVo>> list(HdComponentInfoBo bo) {
        List<HdComponentInfoVo> list = hdComponentInfoService.queryList(bo);
        return R.ok(list);
    }


    /**
     * 查询构件信息列表(分页)
     */
//    @SaCheckPermission("business:componentInfo:list")
    @GetMapping("/page")
    public TableDataInfo<HdComponentInfoVo> page(HdComponentInfoBo bo, PageQuery pageQuery) {
        return hdComponentInfoService.queryPageList(bo, pageQuery);
    }



    /**
     * 获取构件信息详细信息
     *
     * @param id 主键
     */
//    @SaCheckPermission("business:componentInfo:query")
    @GetMapping("/{id}")
    public R<HdComponentInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdComponentInfoService.queryById(id));
    }

    /**
     * 新增构件信息
     */
    @SaCheckPermission("business:componentInfo:add")
    @Log(title = "构件信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdComponentInfoBo bo) {
        return toAjax(hdComponentInfoService.insertByBo(bo));
    }

    /**
     * 修改构件信息
     */
    @SaCheckPermission("business:componentInfo:edit")
    @Log(title = "构件信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdComponentInfoBo bo) {
        return toAjax(hdComponentInfoService.updateByBo(bo));
    }

    /**
     * 删除构件信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:componentInfo:remove")
    @Log(title = "构件信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {

        return toAjax(hdComponentInfoService.deleteWithValidByIds(List.of(ids), true));
    }


    /**
     * 状态修改
     */
    @SaCheckPermission("business:componentInfo:edit")
    @Log(title = "构件信息", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public R<Void> changeStatus(@RequestBody HdTemplateInfoBo bo) {
        return toAjax(hdComponentInfoService.updateStatus(bo.getId(), bo.getStatus()));
    }

    /**
     * 获取导入模板
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil.exportExcel(new ArrayList<>(), "组件模板", HdComponentInfoVo.class, response);
    }


    /**
     * 导出构件信息列表
     */
    @SaCheckPermission("business:componentInfo:export")
    @Log(title = "构件信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdComponentInfoBo bo, HttpServletResponse response) {
        List<HdComponentInfoVo> list = hdComponentInfoService.queryExportList(bo);
        ExcelUtil.exportExcel(list, "构件信息", HdComponentInfoVo.class, response);
    }

    /**
     * 导入数据
     */
    @Log(title = "构件信息", businessType = BusinessType.IMPORT)
    @SaCheckPermission("business:componentInfo:edit")
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file, String category) throws Exception {
        ExcelResult<HdComponentInfoVo> result = ExcelUtil.importExcel(file.getInputStream(), HdComponentInfoVo.class, new ComponentImportListener(category));
        return R.ok(result.getAnalysis());
    }
    /**
     * 获取需要插入的构件信息详细内容
     * @param ids 主键
     */
//    @SaCheckPermission("business:componentInfo:query")
    @GetMapping("/ins/{ids}")
    public R<List<String>>  getInsList(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids){
        List<String>  list =hdComponentInfoService.getInsList(ids);
        return R.ok(list);
    }








}
