package com.hdsolartech.business.controller;

import java.util.List;

import com.hdsolartech.business.listener.GlobalEventPublisher;
import com.hdsolartech.business.listener.UpdateSocialEvent;
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
import com.hdsolartech.business.domain.vo.HdAppInfoVo;
import com.hdsolartech.business.domain.bo.HdAppInfoBo;
import com.hdsolartech.business.service.IHdAppInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 应用信息
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/appInfo")
public class HdAppInfoController extends BaseController {

    private final IHdAppInfoService hdAppInfoService;
    private final GlobalEventPublisher eventPublisher;

    /**
     * 查询应用信息列表
     */
    @SaCheckPermission("business:appInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdAppInfoVo> list(HdAppInfoBo bo, PageQuery pageQuery) {
        return hdAppInfoService.queryPageList(bo, pageQuery);
    }


    /**
     * 查询应用信息列表
     */
    @SaCheckPermission("business:appInfo:list")
    @GetMapping("/listAll")
    public R<List<HdAppInfoVo>> listAll(HdAppInfoBo bo) {
        List<HdAppInfoVo> list = hdAppInfoService.queryList(bo);
        list.forEach(item -> {
            item.setAppSecret(null);
        });
        return R.ok(list);
    }

    /**
     * 导出应用信息列表
     */
    @SaCheckPermission("business:appInfo:export")
    @Log(title = "应用信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdAppInfoBo bo, HttpServletResponse response) {
        List<HdAppInfoVo> list = hdAppInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "应用信息", HdAppInfoVo.class, response);
    }

    /**
     * 获取应用信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:appInfo:query")
    @GetMapping("/{id}")
    public R<HdAppInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdAppInfoService.queryById(id));
    }

    /**
     * 新增应用信息
     */
    @SaCheckPermission("business:appInfo:add")
    @Log(title = "应用信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdAppInfoBo bo) {
        boolean result = hdAppInfoService.insertByBo(bo);
        publishUpdateApp();
        return toAjax(result);
    }

    /**
     * 修改应用信息
     */
    @SaCheckPermission("business:appInfo:edit")
    @Log(title = "应用信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdAppInfoBo bo) {
        boolean result = hdAppInfoService.updateByBo(bo);
        publishUpdateApp();
        return toAjax(result);
    }

    /**
     * 删除应用信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:appInfo:remove")
    @Log(title = "应用信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        boolean result = hdAppInfoService.deleteWithValidByIds(List.of(ids), true);
        publishUpdateApp();
        return toAjax(result);
    }

    /**
     * 应用启用停用
     */
    @SaCheckPermission("business:appInfo:changeState")
    @Log(title = "应用启用停用", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/changeState")
    public R<Void> changeState(@RequestBody HdAppInfoBo bo) {
        boolean result = hdAppInfoService.changeStatus(bo.getId(), bo.getStatus());
        publishUpdateApp();
        return toAjax(result);
    }

    /**
     * 通知其他module做出动作
     */
    public void publishUpdateApp(){
        UpdateSocialEvent updateSocialEvent = new UpdateSocialEvent(this,"更新app");
        eventPublisher.publishEvent(updateSocialEvent);
    }
}
