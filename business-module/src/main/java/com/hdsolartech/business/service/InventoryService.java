package com.hdsolartech.business.service;


import com.hdsolartech.yongyou.vo.CommonInventory;
import com.hdsolartech.yongyou.vo.CommonInventoryInVo;
import com.hdsolartech.yongyou.vo.OtherInventory;
import com.hdsolartech.yongyou.vo.OtherInventoryInVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import java.util.List;

/**
 * @ClassName InventoryService
 * @Description TODO
 * @Author zyf
 * @create 2024/5/13 16:46
 * @Version 1.0
 */
public interface InventoryService {


    TableDataInfo<CommonInventory> queryPageList(CommonInventoryInVo inVo, PageQuery pageQuery) ;


    List<OtherInventory> getOtherInventoryList (OtherInventoryInVo inVo);


    TableDataInfo<CommonInventory> queryProPageList(CommonInventoryInVo inVo, PageQuery pageQuery) ;
}
