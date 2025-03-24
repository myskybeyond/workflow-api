package com.hdsolartech.business.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.service.InventoryService;
import com.hdsolartech.yongyou.service.IU9Service;
import com.hdsolartech.yongyou.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ArrivalServiceImpl
 * @Description 库存实现impl
 * @Author zyf
 * @create 2024/5/13 16:46
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryServiceImpl implements InventoryService {

    /**
     * u9service
     */
    private final IU9Service u9service;

    /**
     * 查询库存分页数据
     * @param inVo
     * @param pageQuery
     * @return
     */
    public TableDataInfo<CommonInventory>  queryPageList(CommonInventoryInVo inVo, PageQuery pageQuery) {
        try{
            //设置分页大小
            inVo.setSize(pageQuery.getPageSize());
            //设置当前页数
            inVo.setCurrent(pageQuery.getPageNum());

            // --后续需要根据项目信息查询文件中的 料号信息 组织为 ","分隔的料号信息串


            //查询到货分页信息
            BasePageOutVo<CommonInventory> out  = u9service.getInventoryPageList( inVo );
            //解析到货信息
            if( out == null){
                log.error("查询库存信息为空");
                return null;
            }
            IPage<CommonInventory> page = new Page<>(out.getCurrent(), out.getSize(), out.getTotal());
            page.setRecords(out.getRecords());
            return TableDataInfo.build(page);
        }catch(Exception e){
            log.error("查询库存信息列表出现异常",e);
        }
        return null;
    }

    /**
     * 查询其它库存列表信息
     * @param inVo
     * @return
     */
    @Override
    public List<OtherInventory> getOtherInventoryList(OtherInventoryInVo inVo) {
        try{
            if( inVo == null  || StrUtil.isEmpty(inVo.getPartNo())){
                log.error("调用其他项目通用库存信息 参数传入为空");
                return null;
            }
            return u9service.getOtherInventoryList(inVo);
        }catch(Exception e){
            log.error("查询其他项目库存可用量出现异常",e);
        }
        return null;
    }



    /**
     * 查询库存分页数据
     * @param inVo
     * @param pageQuery
     * @return
     */
    public TableDataInfo<CommonInventory>  queryProPageList(CommonInventoryInVo inVo, PageQuery pageQuery) {
        try{
            //设置分页大小
            inVo.setSize(pageQuery.getPageSize());
            //设置当前页数
            inVo.setCurrent(pageQuery.getPageNum());

            // --后续需要根据项目信息查询文件中的 料号信息 组织为 ","分隔的料号信息串


            //查询到货分页信息
            BasePageOutVo<CommonInventory> out  = u9service.getProInventoryPageList( inVo );
            //解析到货信息
            if( out == null){
                log.error("查询库存信息为空");
                return null;
            }
            IPage<CommonInventory> page = new Page<>(out.getCurrent(), out.getSize(), out.getTotal());
            page.setRecords(out.getRecords());
            return TableDataInfo.build(page);
        }catch(Exception e){
            log.error("查询库存信息列表出现异常",e);
        }
        return null;
    }

}


