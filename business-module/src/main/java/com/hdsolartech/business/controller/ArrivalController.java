package com.hdsolartech.business.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.hdsolartech.business.service.ArrivalService;
import com.hdsolartech.yongyou.vo.Arrival;
import com.hdsolartech.yongyou.vo.ArrivalInVo;
import lombok.RequiredArgsConstructor;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 到货controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/arrival")
public class ArrivalController extends BaseController {

    /**
     * 到货service
     */
    private final ArrivalService arrivalService;

    /**
     * 查询到货信息分页信息
     */
    @SaCheckPermission("business:arrival:list")
    @GetMapping("/list")
    public TableDataInfo<Arrival> list(ArrivalInVo inVo, PageQuery pageQuery) {
        return arrivalService.queryPageList(inVo, pageQuery);
    }


}
