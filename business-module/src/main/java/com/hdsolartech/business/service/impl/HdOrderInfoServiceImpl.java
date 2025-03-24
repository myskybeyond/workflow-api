package com.hdsolartech.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdOrderAuthority;
import com.hdsolartech.business.domain.HdOrderInfo;
import com.hdsolartech.business.domain.HdProjectInfo;
import com.hdsolartech.business.domain.bo.HdOrderAuthorityBo;
import com.hdsolartech.business.domain.bo.HdOrderInfoBo;
import com.hdsolartech.business.domain.bo.HdProjectAuthorityBo;
import com.hdsolartech.business.domain.vo.HdOrderAuthorityVo;
import com.hdsolartech.business.domain.vo.HdOrderInfoVo;
import com.hdsolartech.business.domain.vo.HdProjectAuthorityVo;
import com.hdsolartech.business.mapper.HdOrderInfoMapper;
import com.hdsolartech.business.mapper.HdProjectInfoMapper;
import com.hdsolartech.business.service.IHdOrderAuthorityService;
import com.hdsolartech.business.service.IHdOrderInfoService;
import com.hdsolartech.business.service.IHdProjectFileInfoService;
import com.hdsolartech.business.service.IHdProjectOperLogService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.service.OrderService;
import org.dromara.common.core.utils.DateUtils;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 销售订单信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@RequiredArgsConstructor
@Service
public class HdOrderInfoServiceImpl implements IHdOrderInfoService, OrderService {

    private final HdOrderInfoMapper baseMapper;

    private final HdProjectInfoMapper projectInfoMapper;

    /**
     * 项目文件service
     */
    private final IHdProjectFileInfoService projectFileInfoService;


    /**
     * 项目日志service
     */
    private final IHdProjectOperLogService projectOperLogService;


    private final IHdOrderAuthorityService orderAuthorityService;

    @Override
    public Map queryIndexInfo() {
        long userId = LoginHelper.getUserId();
        Date firstM = DateUtils.getFirstDayOfMonth();
        Date firstW = DateUtils.getFirstDayOfWeek();
        Date today = new Date();
        //全部订单
        Long allOrderNumber = baseMapper.selectCount(new LambdaQueryWrapper<HdOrderInfo>().eq(BaseEntity::getCreateBy, userId));
        //本月订单
        Long monthOrderNumber = baseMapper.selectCount(new LambdaQueryWrapper<HdOrderInfo>().eq(BaseEntity::getCreateBy, userId).between(BaseEntity::getCreateTime,firstM,today));

        //全部项目
        Long allProjectNumber = projectInfoMapper.selectCount(new LambdaQueryWrapper<HdProjectInfo>().eq(BaseEntity::getCreateBy, userId));
        //本周项目
        Long weekProjectNumber = projectInfoMapper.selectCount(new LambdaQueryWrapper<HdProjectInfo>().eq(BaseEntity::getCreateBy, userId).between(BaseEntity::getCreateTime,firstW,today));

        //本月完成报价任务数量
        Integer offerNumber = baseMapper.countOffer(userId, firstM, today);
        //本月完成下单任务数量
        Integer orderNumber = baseMapper.countOrder(userId, firstM, today);
        Map<String,Object> result = new HashMap<>();
        result.put("allOrderNumber",allOrderNumber);
        result.put("monthOrderNumber",monthOrderNumber);
        result.put("allProjectNumber",allProjectNumber);
        result.put("weekProjectNumber",weekProjectNumber);
        result.put("offerNumber",offerNumber);
        result.put("orderNumber",orderNumber);
        return result;
    }

    /**
     * 查询销售订单信息
     */
    @Override
    public HdOrderInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询销售订单信息列表
     */
    @Override
    public TableDataInfo<HdOrderInfoVo> queryPageList(HdOrderInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdOrderInfo> lqw = buildQueryWrapperByPermission(bo);
        if(lqw == null){
            //直接返回空数据
            return TableDataInfo.build(pageQuery.build());
        }
        Page<HdOrderInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询销售订单信息列表
     */
    @Override
    public List<HdOrderInfoVo> queryList(HdOrderInfoBo bo) {
        LambdaQueryWrapper<HdOrderInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdOrderInfo> buildQueryWrapperByPermission(HdOrderInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdOrderInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getOrderNo()), HdOrderInfo::getOrderNo, bo.getOrderNo());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdOrderInfo::getName, bo.getName());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            HdOrderInfo::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));

        if (StrUtil.isNotEmpty(bo.getProjectName())) {
            //项目名称
            List<HdProjectInfo> list = projectInfoMapper.selectList(Wrappers.<HdProjectInfo>query().lambda().like(HdProjectInfo::getName, bo.getProjectName()));
            if (CollUtil.isNotEmpty(list)) {
                List<Long> res = list.stream().map(O -> O.getId()).collect(Collectors.toList());
                lqw.in(HdOrderInfo::getProjectId, res);
            } else {
                lqw.isNull(HdOrderInfo::getProjectId);
            }
        }
        if (StrUtil.isNotEmpty(bo.getProjectNo())) {
            //项目编号
            List<HdProjectInfo> list = projectInfoMapper.selectList(Wrappers.<HdProjectInfo>query().lambda().like(HdProjectInfo::getName, bo.getProjectNo()));
            if (CollUtil.isNotEmpty(list)) {
                List<Long> res = list.stream().map(O -> O.getId()).collect(Collectors.toList());
                lqw.in(HdOrderInfo::getProjectId, res);
            } else {
                lqw.isNull(HdOrderInfo::getProjectId);
            }
        }
        if (bo.getProjectId() != null) {
            lqw.eq(HdOrderInfo::getProjectId, bo.getProjectId());
        }

        if(ArrayUtil.isNotEmpty(bo.getStatusArr())){
            lqw.in(HdOrderInfo::getStatus, Arrays.asList(bo.getStatusArr()));
        }
        lqw.eq(bo.getStatus() != null,HdOrderInfo::getStatus,bo.getStatus());

        lqw.orderByDesc(HdOrderInfo::getId);
        //admin  数据全部返回
        if( LoginHelper.isSuperAdmin()){
            return lqw;
        }
        //具有某个角色的用户 数据全部返回
        if(CollUtil.isNotEmpty(LoginHelper.getLoginUser().getRoles())){
            List<String>  roleKeys = LoginHelper.getLoginUser().getRoles().stream().map(o->o.getRoleKey()).collect(Collectors.toList());
            if(roleKeys.contains(UserConstants.PROJECT_ORDER_DATA_ROLE_KEY)){
                return lqw;
            }
        }
        HdOrderAuthorityBo authorityBo  = new HdOrderAuthorityBo();
        authorityBo.setUserId(LoginHelper.getUserId());
        //数据需要通过项目权限信息进行过滤
        List<HdOrderAuthorityVo>
            orderAuthorityVos = orderAuthorityService.queryList(authorityBo);
        if(CollUtil.isEmpty(orderAuthorityVos)){
            //没有项目查询权限
            return null;
        }
        lqw.in(HdOrderInfo::getId, orderAuthorityVos.stream().map(HdOrderAuthorityVo::getOrderId).collect(Collectors.toList()));
        return lqw;
    }

    private LambdaQueryWrapper<HdOrderInfo> buildQueryWrapper(HdOrderInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdOrderInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getOrderNo()), HdOrderInfo::getOrderNo, bo.getOrderNo());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdOrderInfo::getName, bo.getName());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            HdOrderInfo::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));

        if (StrUtil.isNotEmpty(bo.getProjectName())) {
            //项目名称
            List<HdProjectInfo> list = projectInfoMapper.selectList(Wrappers.<HdProjectInfo>query().lambda().like(HdProjectInfo::getName, bo.getProjectName()));
            if (CollUtil.isNotEmpty(list)) {
                List<Long> res = list.stream().map(O -> O.getId()).collect(Collectors.toList());
                lqw.in(HdOrderInfo::getProjectId, res);
            } else {
                lqw.isNull(HdOrderInfo::getProjectId);
            }
        }
        if (StrUtil.isNotEmpty(bo.getProjectNo())) {
            //项目编号
            List<HdProjectInfo> list = projectInfoMapper.selectList(Wrappers.<HdProjectInfo>query().lambda().like(HdProjectInfo::getName, bo.getProjectNo()));
            if (CollUtil.isNotEmpty(list)) {
                List<Long> res = list.stream().map(O -> O.getId()).collect(Collectors.toList());
                lqw.in(HdOrderInfo::getProjectId, res);
            } else {
                lqw.isNull(HdOrderInfo::getProjectId);
            }
        }
        if (bo.getProjectId() != null) {
            lqw.eq(HdOrderInfo::getProjectId, bo.getProjectId());
        }

        if(ArrayUtil.isNotEmpty(bo.getStatusArr())){
            lqw.in(HdOrderInfo::getStatus, Arrays.asList(bo.getStatusArr()));
        }
        lqw.eq(bo.getStatus() != null,HdOrderInfo::getStatus,bo.getStatus());
        return lqw;
    }

    /**
     * 新增销售订单信息
     */
    @Override
    public Boolean insertByBo(HdOrderInfoBo bo) {
        HdOrderInfo add = MapstructUtils.convert(bo, HdOrderInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            //保存文件信息
            projectFileInfoService.updateFileInfo(add.getRelaFileInfo(), add.getProjectId(), add.getId(), null);
            //判断是否存在文件信息 保存项目和文件的信息
            projectOperLogService.recordLog(UserConstants.PROJECT_OPER_LOG_TYPE_2, add.getProjectId(), add.getId(), UserConstants.PROJECT_OPERATE_TYPE_11, UserConstants.PROJECT_OPERATE_RESULT_SUCCESS, "");
            HdOrderAuthorityBo authBo = new HdOrderAuthorityBo();
            authBo.setUserId(LoginHelper.getUserId());
            authBo.setProjectId(add.getProjectId());
            authBo.setOrderId(add.getId());
            orderAuthorityService.insertByBo(authBo);
            if (LoginHelper.getUserId().longValue() != bo.getManager().longValue()) {
                HdOrderAuthorityBo authBo1 = new HdOrderAuthorityBo();
                authBo1.setUserId(bo.getManager());
                authBo1.setProjectId(add.getProjectId());
                authBo1.setOrderId(add.getId());
                orderAuthorityService.insertByBo(authBo1);
            }
        }
        return flag;
    }

    /**
     * 修改销售订单信息
     */
    @Override
    public Boolean updateByBo(HdOrderInfoBo bo) {
        HdOrderInfo update = MapstructUtils.convert(bo, HdOrderInfo.class);
        validEntityBeforeSave(update);
        Boolean flag = baseMapper.updateById(update) > 0;

        projectFileInfoService.updateFileInfo(update.getRelaFileInfo(), update.getProjectId(), update.getId(), null);
        //判断是否存在文件信息 保存项目和文件的信息
        projectOperLogService.recordLog(UserConstants.PROJECT_OPER_LOG_TYPE_2, update.getProjectId(), update.getId(), UserConstants.PROJECT_OPERATE_TYPE_12, UserConstants.PROJECT_OPERATE_RESULT_SUCCESS, "");

        HdOrderAuthorityBo queryBo = new HdOrderAuthorityBo();
        queryBo.setProjectId(bo.getId());
        queryBo.setOrderId(update.getId());
        queryBo.setUserId(bo.getManager());
        HdOrderAuthorityVo authorityVo = orderAuthorityService.queryByBo(queryBo);
        if (authorityVo == null) {
            //插入负责人权限信息
            HdOrderAuthorityBo orderBo = new HdOrderAuthorityBo();
            orderBo.setProjectId(bo.getProjectId());
            orderBo.setOrderId(bo.getId());
            //设置登录用户
            orderBo.setUserId(bo.getManager());
            //创建项目权限bo
            orderAuthorityService.insertByBo(orderBo);
        }
        return flag;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdOrderInfo entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除销售订单信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 修改销售订单进度信息
     */
    @Override
    public Boolean progress(HdOrderInfoBo bo) {
        HdOrderInfo update = MapstructUtils.convert(bo, HdOrderInfo.class);
        Boolean flag = baseMapper.updateById(update) > 0;
        //判断是否存在文件信息 保存项目和文件的信息
        projectOperLogService.recordLog(UserConstants.PROJECT_OPER_LOG_TYPE_2, update.getProjectId(), update.getId(), UserConstants.PROJECT_OPERATE_TYPE_14, UserConstants.PROJECT_OPERATE_RESULT_SUCCESS, "");
        return flag;
    }

    /**
     * 获取未完成的订单
     * @return
     */
    @Override
    public List<Long>  undoneList(){
        //非完成和非作废的订单
        List<HdOrderInfo> orderList = baseMapper.selectList(Wrappers.<HdOrderInfo>query().lambda().select(HdOrderInfo::getId).ne(HdOrderInfo::getStatus,UserConstants.ORDER_STATUS_COMPLETED).ne(HdOrderInfo::getStatus,UserConstants.ORDER_STATUS_CANCEL));
        if(CollUtil.isEmpty(orderList)){
            return null;
        }
        return orderList.stream().map(order->order.getId()).collect(Collectors.toList());
    }

    @Override
    public String selectNameByOrderId(Long orderId) {

        HdOrderInfoVo orderInfoVo =   baseMapper.selectVoById(orderId);
        if( orderInfoVo !=null){
            return orderInfoVo.getName();
        } else {
            return "";
        }
    }

    /**
     * 查询销售订单信息列表
     */
    @Override
    public List<HdOrderInfoVo> queryMyList(HdOrderInfoBo bo){
        LambdaQueryWrapper<HdOrderInfo> lqw = buildQueryWrapperByPermission(bo);
        return baseMapper.selectVoList(lqw);
    }
}
