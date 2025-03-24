package com.hdsolartech.business.service.impl;

import com.hdsolartech.business.domain.HdProjectProgressInfo;
import com.hdsolartech.business.domain.vo.HdOrderInfoVo;
import com.hdsolartech.business.domain.vo.HdProjectFileInfoVo;
import com.hdsolartech.business.mapper.HdOrderInfoMapper;
import com.hdsolartech.business.mapper.HdProjectFileInfoMapper;
import com.hdsolartech.business.mapper.HdProjectFileVersionInfoMapper;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.service.ConfigService;
import org.dromara.common.core.service.ProcessService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hdsolartech.business.domain.bo.HdProjectDeliveryInfoBo;
import com.hdsolartech.business.domain.vo.HdProjectDeliveryInfoVo;
import com.hdsolartech.business.domain.HdProjectDeliveryInfo;
import com.hdsolartech.business.mapper.HdProjectDeliveryInfoMapper;
import com.hdsolartech.business.service.IHdProjectDeliveryInfoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 项目发货信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-19
 */
@RequiredArgsConstructor
@Service
public class HdProjectDeliveryInfoServiceImpl implements IHdProjectDeliveryInfoService {

    private final HdProjectDeliveryInfoMapper baseMapper;

    /**
     * 订单mapper
     */
    private final   HdOrderInfoMapper orderInfoMapper;


    /**
     * 项目文件mapper
     */
    private final HdProjectFileInfoMapper projectFileInfoMapper;


    /**
     * 项目mapper
     */
    private final HdProjectFileVersionInfoMapper projectFileVersionInfoMapper;




    /**
     * 查询项目发货信息
     */
    @Override
    public HdProjectDeliveryInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目发货信息列表
     */
    @Override
    public TableDataInfo<HdProjectDeliveryInfoVo> queryPageList(HdProjectDeliveryInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectDeliveryInfo> lqw = buildQueryWrapper(bo);
        Page<HdProjectDeliveryInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目发货信息列表
     */
    @Override
    public List<HdProjectDeliveryInfoVo> queryList(HdProjectDeliveryInfoBo bo) {
        LambdaQueryWrapper<HdProjectDeliveryInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectDeliveryInfo> buildQueryWrapper(HdProjectDeliveryInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectDeliveryInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdProjectDeliveryInfo::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdProjectDeliveryInfo::getOrderId, bo.getOrderId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdProjectDeliveryInfo::getName, bo.getName());
        lqw.eq(bo.getIsSync() != null, HdProjectDeliveryInfo::getIsSync, bo.getIsSync());
        lqw.eq(bo.getProjectFileId() != null, HdProjectDeliveryInfo::getProjectFileId, bo.getProjectFileId());
        lqw.eq(bo.getCreateBy() != null, HdProjectDeliveryInfo::getCreateBy, bo.getCreateBy());
        lqw.eq(bo.getCreateTime() != null, HdProjectDeliveryInfo::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateBy() != null, HdProjectDeliveryInfo::getUpdateBy, bo.getUpdateBy());
        lqw.eq(bo.getUpdateTime() != null, HdProjectDeliveryInfo::getUpdateTime, bo.getUpdateTime());
        lqw.between(params.get("beginUpdateTime") != null && params.get("endUpdateTime") != null,
            HdProjectDeliveryInfo::getUpdateTime ,params.get("beginUpdateTime"), params.get("endUpdateTime"));
        return lqw;
    }

    /**
     * 新增项目发货信息
     */
    @Override
    public Boolean insertByBo(HdProjectDeliveryInfoBo bo) {
        HdProjectDeliveryInfo add = MapstructUtils.convert(bo, HdProjectDeliveryInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目发货信息
     */
    @Override
    public Boolean updateByBo(HdProjectDeliveryInfoBo bo) {
        HdProjectDeliveryInfo update = MapstructUtils.convert(bo, HdProjectDeliveryInfo.class);
        validEntityBeforeSave(update);
        Boolean flag = baseMapper.updateById(update) > 0;
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectDeliveryInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目发货信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 同步发送流程
     */
    @Override
    public Boolean sync(HdProjectDeliveryInfoBo bo) {
        //查询流程key
        String modelKey = SpringUtils.getBean(ConfigService.class).getConfigValue(UserConstants.CONFIG_FLOWABLE_DELIVERY_PROGRESS);
        //组织同步流程参数
        Map<String, Object> variables = new HashMap<>();
        HdProjectDeliveryInfoVo deliveryInfoVo = baseMapper.selectVoById(bo.getId());
        if(deliveryInfoVo == null){
            return Boolean.FALSE;
        }
        //订单信息
        HdOrderInfoVo orderInfoVo = orderInfoMapper.selectVoById(deliveryInfoVo.getOrderId());
        if(orderInfoVo == null){
            return Boolean.FALSE;
        }
        variables.put(UserConstants.ORDER_ID_KEY,orderInfoVo.getId()+StringUtils.SEPARATOR+orderInfoVo.getName());
        variables.put(UserConstants.REMARK_KEY,deliveryInfoVo.getRemark());
        HdProjectFileInfoVo projectFileInfoVo =  projectFileInfoMapper.selectVoById(deliveryInfoVo.getProjectFileId());
        if(projectFileInfoVo == null){
            return Boolean.FALSE;
        }
        //查询
        //组织发货进度文件信息
        variables.put(UserConstants.ONLINE_COMMON_FILE_ID_KEY,projectFileInfoVo.getOnlineFileId()+StringUtils.SEPARATOR+deliveryInfoVo.getName()+StringUtils.SEPARATOR+deliveryInfoVo.getVersion()+StringUtils.SEPARATOR+deliveryInfoVo.getRemark());
        Boolean flag = SpringUtils.getBean(ProcessService.class).startProcessByModelKey(modelKey,variables);
        if( !flag ){
            return Boolean.FALSE;
        }
        //修改文件同步状态
        projectFileVersionInfoMapper.updateSyncStatus(deliveryInfoVo.getProjectFileId(),deliveryInfoVo.getVersion(),UserConstants.SYNC_STATUS_1);
        //修改发货同步状态
        bo.setIsSync(UserConstants.SYNC_STATUS_1);
        HdProjectDeliveryInfo update = MapstructUtils.convert(bo, HdProjectDeliveryInfo.class);
        baseMapper.updateById(update);
        return Boolean.TRUE;
    }
}
