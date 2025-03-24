package com.hdsolartech.business.job;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.hdsolartech.business.domain.HdProjectProgressStatistics;
import com.hdsolartech.business.domain.vo.HdProjectProgressExtendVo;
import com.hdsolartech.business.service.IHdOrderInfoService;
import com.hdsolartech.business.service.IHdProjectProgressExtendInfoService;
import com.hdsolartech.business.service.IHdProjectProgressStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName StatisticsScheduledTask
 * @Description 统计定时任务处理
 * @Author zyf
 * @create 2024/7/1 11:18
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class StatisticsScheduledTask {

    /**
     * 进度扩展信息
     */
    private final IHdProjectProgressExtendInfoService extendInfoService;

    /**
     * 项目进度统计service
     */
    private final IHdProjectProgressStatisticsService hdProjectProgressStatisticsService;

    /**
     * orderInfo Service
     */
    private final IHdOrderInfoService hdOrderInfoService;
    /**
     * 进度定时任务
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void progressTask(){
        try{
            //进度定时任务
            List<HdProjectProgressExtendVo> extendVos = extendInfoService.getAllProgressList();
            if(CollUtil.isNotEmpty(extendVos)){
                //创建进度信息
                List<HdProjectProgressStatistics> statisticsList = new ArrayList<>();
                //订单列表
                List<Long> orderList = hdOrderInfoService.undoneList();
                if(CollUtil.isEmpty(orderList)){
                    //订单信息为空 直接返回
                    return;
                }
                List<HdProjectProgressStatistics>  staList = new ArrayList<>();
                //处理统计信息
                for(HdProjectProgressExtendVo extendVo : extendVos){
                    if(orderList.contains(extendVo.getOrderId())){
                      //将数据放入统计信息
                        //扩展信息
                        HdProjectProgressStatistics staVo = new HdProjectProgressStatistics();
                        staVo.setProjectId(extendVo.getProjectId());
                        staVo.setOrderId(extendVo.getOrderId());
                        staVo.setProjectProgressId(extendVo.getProjectProgressId());
                        staVo.setArrival(extendVo.getArrival());
                        staVo.setProduction(extendVo.getProduction());
                        staVo.setBlack(extendVo.getBlack());
                        staVo.setWhite(extendVo.getWhite());
                        staVo.setDelivery(extendVo.getDelivery());
                        staVo.setOrderDate(extendVo.getOrderDate());
                        staVo.setDeliverStartDate(extendVo.getDeliverStartDate());
                        staVo.setDeliverEndDate(extendVo.getDeliverEndDate());
                        staVo.setStatisticsDate(DateUtil.yesterday());
                        staList.add(staVo);
                    }
                }
                if(CollUtil.isNotEmpty(staList)){
                    hdProjectProgressStatisticsService.saveBatch(staList);
                }
            }
        }catch(Exception e){
            log.error("进度定时任务统计出现异常",e);
        }
    }

}
