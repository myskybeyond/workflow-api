package com.myskybeyond.business.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.myskybeyond.business.domain.bo.HdSmsInfoBo;
import com.myskybeyond.business.domain.vo.HdSmsInfoVo;
import com.myskybeyond.business.service.IHdSmsInfoService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 我的消息
 * @date 2024/6/28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/smsInfo")
public class MyHdSmsInfoController {

    private final IHdSmsInfoService hdSmsInfoService;

    /**
     * 查询消息历史记录信息列表
     */
    @SaCheckPermission(value = {"business:mysmg:query", "system:user:leader", "system:user:everyman"}, mode = SaMode.OR)
    @GetMapping("/list/my")
    public TableDataInfo<HdSmsInfoVo> list(HdSmsInfoBo bo, PageQuery pageQuery) {
        return hdSmsInfoService.queryPageList(bo, pageQuery);
    }
}
