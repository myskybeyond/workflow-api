package com.hdsolartech.business.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dev33.satoken.annotation.SaMode;
import com.hdsolartech.business.domain.HdProjectAuthority;
import com.hdsolartech.business.domain.vo.HdProjectInfoVo;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.dromara.common.satoken.utils.LoginHelper;
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
import com.hdsolartech.business.domain.vo.HdProjectAuthorityVo;
import com.hdsolartech.business.domain.bo.HdProjectAuthorityBo;
import com.hdsolartech.business.service.IHdProjectAuthorityService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目权限信息
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectAuthority")
public class HdProjectAuthorityController extends BaseController {

    private final IHdProjectAuthorityService hdProjectAuthorityService;

    /**
     * 查询项目权限信息列表
     */
    @SaCheckPermission("business:projectAuthority:list")
    @GetMapping("/list")
    public TableDataInfo<HdProjectAuthorityVo> list(HdProjectAuthorityBo bo, PageQuery pageQuery) {
        return hdProjectAuthorityService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出项目权限信息列表
     */
    @SaCheckPermission("business:projectAuthority:export")
    @Log(title = "项目权限信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectAuthorityBo bo, HttpServletResponse response) {
        List<HdProjectAuthorityVo> list = hdProjectAuthorityService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目权限信息", HdProjectAuthorityVo.class, response);
    }

    /**
     * 获取项目权限信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:projectAuthority:query")
    @GetMapping("/{id}")
    public R<HdProjectAuthorityVo> getInfo(@NotNull(message = "主键不能为空")
                                           @PathVariable Long id) {
        return R.ok(hdProjectAuthorityService.queryById(id));
    }

    /**
     * 新增项目权限信息
     */
    @SaCheckPermission("business:projectAuthority:add")
    @Log(title = "项目权限信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectAuthorityBo bo) {
        return toAjax(hdProjectAuthorityService.insertByBo(bo));
    }

    /**
     * 修改项目权限信息
     */
    @SaCheckPermission("business:projectAuthority:edit")
    @Log(title = "项目权限信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectAuthorityBo bo) {
        return toAjax(hdProjectAuthorityService.updateByBo(bo));
    }

    /**
     * 删除项目权限信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectAuthority:remove")
    @Log(title = "项目权限信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectAuthorityService.deleteWithValidByIds(List.of(ids), true));
    }

    @SaCheckPermission(value = {"system:user:everyman", "system:user:leader"}, mode = SaMode.OR)
    @GetMapping("/myProject")
    public R<Map> getMyProjectList() {
        List<HdProjectInfoVo> hdProjectAuthorityVos = hdProjectAuthorityService.getMyProjectList(LoginHelper.getUserId());

        HdProjectAuthorityBo hpab = new HdProjectAuthorityBo();
        hpab.setUserId(LoginHelper.getUserId());
        int total = hdProjectAuthorityService.queryList(hpab).size();
        Map<String,Object> data = new HashMap<>();
        data.put("rows",hdProjectAuthorityVos);
        data.put("total",total);
        return R.ok(data);
    }
}
