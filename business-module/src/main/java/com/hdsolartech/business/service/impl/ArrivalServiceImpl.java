package com.hdsolartech.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.service.ArrivalService;
import com.hdsolartech.yongyou.service.IU9Service;
import com.hdsolartech.yongyou.vo.Arrival;
import com.hdsolartech.yongyou.vo.ArrivalInVo;
import com.hdsolartech.yongyou.vo.BasePageOutVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

/**
 * @ClassName ArrivalServiceImpl
 * @Description 到货实现impl
 * @Author zyf
 * @create 2024/5/13 16:46
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ArrivalServiceImpl implements ArrivalService {

    /**
     * u9service
     */
    private final IU9Service u9service;

    /**
     * 查询到货分页数据
     * @param inVo
     * @param pageQuery
     * @return
     */
    public TableDataInfo<Arrival>  queryPageList(ArrivalInVo inVo, PageQuery pageQuery) {
        try{
            //设置分页大小
            inVo.setSize(pageQuery.getPageSize());
            //设置当前页数
            inVo.setCurrent(pageQuery.getPageNum());
            //查询到货分页信息
            BasePageOutVo<Arrival> out  = u9service.getArrivalPageList( inVo );
            //解析到货信息
            if( out == null){
                log.error("查询到货信息为空");
                return null;
            }
            IPage<Arrival> page = new Page<>(out.getCurrent(), out.getSize(), out.getTotal());
            page.setRecords(out.getRecords());
            return TableDataInfo.build(page);
        }catch(Exception e){
            log.error("查询到货信息列表出现异常",e);
        }
        return null;
    }

}


