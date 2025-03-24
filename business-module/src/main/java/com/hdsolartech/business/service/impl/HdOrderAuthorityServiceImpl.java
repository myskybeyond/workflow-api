package com.hdsolartech.business.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hdsolartech.business.domain.bo.HdOrderAuthorityBo;
import com.hdsolartech.business.domain.vo.HdOrderAuthorityVo;
import com.hdsolartech.business.domain.HdOrderAuthority;
import com.hdsolartech.business.mapper.HdOrderAuthorityMapper;
import com.hdsolartech.business.service.IHdOrderAuthorityService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 订单权限信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@RequiredArgsConstructor
@Service
public class HdOrderAuthorityServiceImpl implements IHdOrderAuthorityService {

    private final HdOrderAuthorityMapper baseMapper;

    /**
     * 查询订单权限信息
     */
    @Override
    public HdOrderAuthorityVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询订单权限信息列表
     */
    @Override
    public TableDataInfo<HdOrderAuthorityVo> queryPageList(HdOrderAuthorityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdOrderAuthority> lqw = buildQueryWrapper(bo);
        Page<HdOrderAuthorityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询订单权限信息列表
     */
    @Override
    public List<HdOrderAuthorityVo> queryList(HdOrderAuthorityBo bo) {
        LambdaQueryWrapper<HdOrderAuthority> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdOrderAuthority> buildQueryWrapper(HdOrderAuthorityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdOrderAuthority> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdOrderAuthority::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdOrderAuthority::getOrderId, bo.getOrderId());
        lqw.eq(bo.getUserId() != null, HdOrderAuthority::getUserId, bo.getUserId());
        return lqw;
    }

    /**
     * 新增订单权限信息
     */
    @Override
    public Boolean insertByBo(HdOrderAuthorityBo bo) {
        HdOrderAuthority add = MapstructUtils.convert(bo, HdOrderAuthority.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改订单权限信息
     */
    @Override
    public Boolean updateByBo(HdOrderAuthorityBo bo) {
        HdOrderAuthority update = MapstructUtils.convert(bo, HdOrderAuthority.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdOrderAuthority entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除订单权限信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 查询用户权限vo
     * @param bo
     * @return
     */
    @Override
    public  HdOrderAuthorityVo queryByBo(HdOrderAuthorityBo bo ){
        LambdaQueryWrapper<HdOrderAuthority> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdOrderAuthority::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdOrderAuthority::getOrderId, bo.getOrderId());
        lqw.eq(bo.getUserId() != null, HdOrderAuthority::getUserId, bo.getUserId());
        HdOrderAuthorityVo authorityVo = baseMapper.selectVoOne(lqw);
        return authorityVo;
    }
}
