package com.myskybeyond.business.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.myskybeyond.business.domain.bo.HdSmsTemplateInfoBo;
import com.myskybeyond.business.domain.bo.TestMsgSendBo;
import com.myskybeyond.business.domain.vo.HdSmsTemplateInfoVo;
import com.myskybeyond.business.service.IHdSmsTemplateInfoService;
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
 * 消息模板信息
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/smsTemplateInfo")
public class HdSmsTemplateInfoController extends BaseController {

    private final IHdSmsTemplateInfoService hdSmsTemplateInfoService;
//    private final IFlowMessageService flowMessageService;

    /**
     * 查询消息模板信息列表
     */
    @SaCheckPermission("business:smsTemplateInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdSmsTemplateInfoVo> list(HdSmsTemplateInfoBo bo, PageQuery pageQuery) {
        return hdSmsTemplateInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出消息模板信息列表
     */
    @SaCheckPermission("business:smsTemplateInfo:export")
    @Log(title = "消息模板信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdSmsTemplateInfoBo bo, HttpServletResponse response) {
        List<HdSmsTemplateInfoVo> list = hdSmsTemplateInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "消息模板信息", HdSmsTemplateInfoVo.class, response);
    }

    /**
     * 获取消息模板信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:smsTemplateInfo:query")
    @GetMapping("/{id}")
    public R<HdSmsTemplateInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                          @PathVariable Long id) {
        return R.ok(hdSmsTemplateInfoService.queryById(id));
    }

    /**
     * 新增消息模板信息
     */
    @SaCheckPermission("business:smsTemplateInfo:add")
    @Log(title = "消息模板信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdSmsTemplateInfoBo bo) {
        return toAjax(hdSmsTemplateInfoService.insertByBo(bo));
    }

    /**
     * 修改消息模板信息
     */
    @SaCheckPermission("business:smsTemplateInfo:edit")
    @Log(title = "消息模板信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdSmsTemplateInfoBo bo) {
        return toAjax(hdSmsTemplateInfoService.updateByBo(bo));
    }

    /**
     * 删除消息模板信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:smsTemplateInfo:remove")
    @Log(title = "消息模板信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdSmsTemplateInfoService.deleteWithValidByIds(List.of(ids), true));
    }

    /**
     * 消息模板启用停用
     */
    @SaCheckPermission("business:smsTemplateInfo:changeState")
    @Log(title = "消息模板启用停用", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping("/changeState")
    public R<Void> changeState(@RequestBody HdSmsTemplateInfoBo bo) {
        boolean result = hdSmsTemplateInfoService.changeStatus(bo.getId(), bo.getStatus());
        return toAjax(result);
    }

    /**
     * 修改消息模板信息
     */
    @SaCheckPermission("business:smsTemplateInfo:test")
    @Log(title = "测试发送消息模板", businessType = BusinessType.OTHER)
    @RepeatSubmit()
    @PutMapping("/testSend")
    public R<Void> testSend(@Validated @RequestBody TestMsgSendBo bo) {
        hdSmsTemplateInfoService.testSend(bo);
        return R.ok();
    }
}
