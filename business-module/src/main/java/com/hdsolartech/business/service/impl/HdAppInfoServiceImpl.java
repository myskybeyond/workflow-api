package com.hdsolartech.business.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdAppInfo;
import com.hdsolartech.business.domain.bo.HdAppInfoBo;
import com.hdsolartech.business.domain.vo.HdAppInfoVo;
import com.hdsolartech.business.mapper.HdAppInfoMapper;
import com.hdsolartech.business.service.IHdAppInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 应用信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class HdAppInfoServiceImpl implements IHdAppInfoService {

    private final HdAppInfoMapper baseMapper;

    /**
     * 查询应用信息
     */
    @Override
    public HdAppInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询应用信息列表
     */
    @Override
    public TableDataInfo<HdAppInfoVo> queryPageList(HdAppInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdAppInfo> lqw = buildQueryWrapper(bo);
        Page<HdAppInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询应用信息列表
     */
    @Override
    public List<HdAppInfoVo> queryList(HdAppInfoBo bo) {
        LambdaQueryWrapper<HdAppInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdAppInfo> buildQueryWrapper(HdAppInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdAppInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdAppInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getAppType()), HdAppInfo::getAppType, bo.getAppType());
        lqw.eq(StringUtils.isNotBlank(bo.getAppKey()), HdAppInfo::getAppKey, bo.getAppKey());
        lqw.eq(StringUtils.isNotBlank(bo.getAppSecret()), HdAppInfo::getAppSecret, bo.getAppSecret());
        lqw.eq(StringUtils.isNotBlank(bo.getAgentId()), HdAppInfo::getAgentId, bo.getAgentId());
        lqw.eq(bo.getStatus() != null, HdAppInfo::getStatus, bo.getStatus());
        lqw.orderByAsc(HdAppInfo::getSort);
        lqw.orderByDesc(HdAppInfo::getUpdateTime);
        return lqw;
    }

    /**
     * 新增应用信息
     */
    @Override
    public Boolean insertByBo(HdAppInfoBo bo) {
        HdAppInfo add = MapstructUtils.convert(bo, HdAppInfo.class);
        if(validEntityBeforeSave(add)) {
            boolean flag = baseMapper.insert(add) > 0;
            if (flag) {
                bo.setId(add.getId());
            }
            return flag;
        }else {
            return false;
        }
    }

    /**
     * 修改应用信息
     */
    @Override
    public Boolean updateByBo(HdAppInfoBo bo) {
        HdAppInfo update = MapstructUtils.convert(bo, HdAppInfo.class);
        if(validEntityBeforeSave(update)){
            return baseMapper.updateById(update) > 0;
        }
        return false;

    }

    /**
     * 保存前的数据校验
     */
    private boolean validEntityBeforeSave(HdAppInfo form) {
        // name + appType不允许重复
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<HdAppInfo>()
            .eq(HdAppInfo::getName, form.getName()).eq(HdAppInfo::getAppType, form.getAppType())
            .ne(ObjectUtil.isNotNull(form.getId()), HdAppInfo::getId, form.getId()));
        return !exist;
    }

    /**
     * 批量删除应用信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Boolean changeStatus(Long id, Long status) {
        if(ObjectUtil.isEmpty(id) || ObjectUtil.isEmpty(status)) {
            log.error("应用管理->修改状态失败,参数为空");
            throw new RuntimeException("请求参数为空");
        }
        HdAppInfo hdAppInfoVo = baseMapper.selectById(id);
        hdAppInfoVo.setStatus(status);
        int effectRows = baseMapper.updateById(hdAppInfoVo);
        return effectRows > 0;
    }
}
