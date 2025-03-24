package com.hdsolartech.business.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.hdsolartech.business.service.InventoryService;
import com.hdsolartech.yongyou.vo.CommonInventory;
import com.hdsolartech.yongyou.vo.CommonInventoryInVo;
import com.hdsolartech.yongyou.vo.OtherInventory;
import com.hdsolartech.yongyou.vo.OtherInventoryInVo;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 到货controller
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/inventory")
public class InventoryController extends BaseController {

    /**
     * 到货service
     */
    private final InventoryService inventoryService;

    /**
     * 查询到货信息分页信息
     */
    @SaCheckPermission("business:inventory:list")
    @GetMapping("/list")
    public TableDataInfo<CommonInventory> list(CommonInventoryInVo inVo, PageQuery pageQuery) {
        return inventoryService.queryPageList(inVo, pageQuery);
    }

    /**
     * 查询到货信息分页信息
     */
    @SaCheckPermission("business:inventory:detail")
    @GetMapping("/others")
    public R<List<OtherInventory>> otherDetails (OtherInventoryInVo inVo) {
        List<OtherInventory> list =  inventoryService.getOtherInventoryList(inVo);
        return R.ok(list);
    }

    @SaCheckLogin()
    @GetMapping("/proList")
    public TableDataInfo<CommonInventory> getProlist(CommonInventoryInVo inVo, PageQuery pageQuery) {
        return inventoryService.queryProPageList(inVo, pageQuery);
    }

}
