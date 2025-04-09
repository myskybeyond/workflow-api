package com.myskybeyond.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.business.domain.HdTemplateInfo;
import com.myskybeyond.business.domain.bo.HdTemplateInfoBo;
import com.myskybeyond.business.domain.vo.HdTemplateInfoVo;
import com.myskybeyond.business.mapper.HdTemplateInfoMapper;
import com.myskybeyond.business.service.IHdTemplateInfoService;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 项目资料模板Service业务层处理
 *
 * @author Lion Li
 * @date 2024-05-24
 */
@RequiredArgsConstructor
@Service
public class HdTemplateInfoServiceImpl implements IHdTemplateInfoService {

    private final HdTemplateInfoMapper baseMapper;

    /**
     * 查询项目资料模板
     */
    @Override
    public HdTemplateInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目资料模板列表
     */
    @Override
    public TableDataInfo<HdTemplateInfoVo> queryPageList(HdTemplateInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdTemplateInfo> lqw = buildQueryWrapper(bo);
        Page<HdTemplateInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目资料模板列表
     */
    @Override
    public List<HdTemplateInfoVo> queryList(HdTemplateInfoBo bo) {
        LambdaQueryWrapper<HdTemplateInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdTemplateInfo> buildQueryWrapper(HdTemplateInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdTemplateInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdTemplateInfo::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), HdTemplateInfo::getCategory, bo.getCategory());
        lqw.eq(bo.getStatus() != null, HdTemplateInfo::getStatus, bo.getStatus());
        lqw.eq(bo.getType() != null, HdTemplateInfo::getType, bo.getType());
        lqw.orderByDesc(HdTemplateInfo::getUpdateTime);
        return lqw;
    }

    /**
     * 新增项目资料模板
     */
    @Override
    public Boolean insertByBo(HdTemplateInfoBo bo) {
        HdTemplateInfo add = MapstructUtils.convert(bo, HdTemplateInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目资料模板
     */
    @Override
    public Boolean updateByBo(HdTemplateInfoBo bo) {
        HdTemplateInfo update = MapstructUtils.convert(bo, HdTemplateInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdTemplateInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目资料模板
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public int updateStatus(Long id, Long status) {
        return baseMapper.update(null,
            new LambdaUpdateWrapper<HdTemplateInfo>()
                .set(HdTemplateInfo::getStatus, status)
                .eq(HdTemplateInfo::getId, id));
    }
}
