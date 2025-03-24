package com.hdsolartech.business.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.hdsolartech.business.domain.bo.HdSmsInfoBo;
import com.hdsolartech.business.domain.vo.HdSmsInfoVo;
import com.hdsolartech.business.service.IHdSmsInfoService;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
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
 * 消息历史记录信息
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/smsInfo")
public class HdSmsInfoController extends BaseController {

    private final IHdSmsInfoService hdSmsInfoService;

    /**
     * 查询消息历史记录信息列表
     */
    @SaCheckPermission("business:smsInfo:list")
    @GetMapping("/list")
    public TableDataInfo<HdSmsInfoVo> list(HdSmsInfoBo bo, PageQuery pageQuery) {
        return hdSmsInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取消息历史记录信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission(value = {"business:smsInfo:query","business:mysmg:query", "system:user:leader", "system:user:everyman"}, mode = SaMode.OR)
    @GetMapping("/{id}")
    public R<HdSmsInfoVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(hdSmsInfoService.queryById(id));

    }

    /**
     * 信息设置为已读
     * @param ids 主键
     * @return
     */
    @SaCheckPermission(value = {"business:smsInfo:markRead", "system:user:leader", "system:user:everyman"}, mode = SaMode.OR)
    @Log(title = "消息设置为已读", businessType = BusinessType.UPDATE)
    @PostMapping("/read/{ids}")
    @RepeatSubmit()
    public R<Void> markRead(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdSmsInfoService.markRead(List.of(ids)));
    }

    /**
     * 用户消息全部已读
     */
    @SaCheckPermission(value = {"business:smsInfo:markRead", "system:user:leader", "system:user:everyman"}, mode = SaMode.OR)
    @Log(title = "用户全部消息已读", businessType = BusinessType.UPDATE)
    @PostMapping("/allRead/{id}")
    @RepeatSubmit
    public R<Void> markAllRead(@NotNull(message = "用户不能为空")
                          @PathVariable Long id) {
        return toAjax(hdSmsInfoService.markAllRead(id));
    }
}
