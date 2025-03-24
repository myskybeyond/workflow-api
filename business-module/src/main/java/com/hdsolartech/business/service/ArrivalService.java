package com.hdsolartech.business.service;


import com.hdsolartech.yongyou.vo.Arrival;
import com.hdsolartech.yongyou.vo.ArrivalInVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * @ClassName ArrivalService
 * @Description TODO
 * @Author zyf
 * @create 2024/5/13 16:46
 * @Version 1.0
 */
public interface ArrivalService {


    TableDataInfo<Arrival> queryPageList(ArrivalInVo inVo, PageQuery pageQuery) ;
}
