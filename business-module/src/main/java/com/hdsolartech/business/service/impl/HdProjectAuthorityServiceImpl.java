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
import com.hdsolartech.business.domain.bo.HdProjectAuthorityBo;
import com.hdsolartech.business.domain.vo.HdProjectAuthorityVo;
import com.hdsolartech.business.domain.HdProjectAuthority;
import com.hdsolartech.business.mapper.HdProjectAuthorityMapper;
import com.hdsolartech.business.service.IHdProjectAuthorityService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 项目权限信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@RequiredArgsConstructor
@Service
public class HdProjectAuthorityServiceImpl implements IHdProjectAuthorityService {

    private final HdProjectAuthorityMapper baseMapper;

    /**
     * 查询项目权限信息
     */
    @Override
    public HdProjectAuthorityVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目权限信息列表
     */
    @Override
    public TableDataInfo<HdProjectAuthorityVo> queryPageList(HdProjectAuthorityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectAuthority> lqw = buildQueryWrapper(bo);
        Page<HdProjectAuthorityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目权限信息列表
     */
    @Override
    public List<HdProjectAuthorityVo> queryList(HdProjectAuthorityBo bo) {
        LambdaQueryWrapper<HdProjectAuthority> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectAuthority> buildQueryWrapper(HdProjectAuthorityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectAuthority> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdProjectAuthority::getProjectId, bo.getProjectId());
        lqw.eq(bo.getUserId() != null, HdProjectAuthority::getUserId, bo.getUserId());
        return lqw;
    }

    /**
     * 新增项目权限信息
     */
    @Override
    public Boolean insertByBo(HdProjectAuthorityBo bo) {
        HdProjectAuthority add = MapstructUtils.convert(bo, HdProjectAuthority.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目权限信息
     */
    @Override
    public Boolean updateByBo(HdProjectAuthorityBo bo) {
        HdProjectAuthority update = MapstructUtils.convert(bo, HdProjectAuthority.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectAuthority entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目权限信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 项目权限信息
     * @param bo
     * @return
     */
    @Override
    public HdProjectAuthorityVo queryByBo(HdProjectAuthorityBo bo){
        LambdaQueryWrapper<HdProjectAuthority> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdProjectAuthority::getProjectId, bo.getProjectId());
        lqw.eq(bo.getUserId() != null, HdProjectAuthority::getUserId, bo.getUserId());
        HdProjectAuthorityVo projectAuthorityVo = baseMapper.selectVoOne(lqw);
        return projectAuthorityVo;

    }

    public List getMyProjectList(Long userId){
        return baseMapper.queryProjectListByUserId(userId);
    }
}
