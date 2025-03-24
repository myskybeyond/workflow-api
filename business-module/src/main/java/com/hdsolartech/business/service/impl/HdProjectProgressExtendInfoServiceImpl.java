package com.hdsolartech.business.service.impl;

import com.hdsolartech.business.domain.vo.HdProjectProgressExtendVo;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hdsolartech.business.domain.bo.HdProjectProgressExtendInfoBo;
import com.hdsolartech.business.domain.vo.HdProjectProgressExtendInfoVo;
import com.hdsolartech.business.domain.HdProjectProgressExtendInfo;
import com.hdsolartech.business.mapper.HdProjectProgressExtendInfoMapper;
import com.hdsolartech.business.service.IHdProjectProgressExtendInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 项目进度扩展信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@RequiredArgsConstructor
@Service
public class HdProjectProgressExtendInfoServiceImpl implements IHdProjectProgressExtendInfoService {

    private final HdProjectProgressExtendInfoMapper baseMapper;

    /**
     * 查询项目进度扩展信息
     */
    @Override
    public HdProjectProgressExtendInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目进度扩展信息列表
     */
    @Override
    public TableDataInfo<HdProjectProgressExtendInfoVo> queryPageList(HdProjectProgressExtendInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectProgressExtendInfo> lqw = buildQueryWrapper(bo);
        Page<HdProjectProgressExtendInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目进度扩展信息列表
     */
    @Override
    public List<HdProjectProgressExtendInfoVo> queryList(HdProjectProgressExtendInfoBo bo) {
        LambdaQueryWrapper<HdProjectProgressExtendInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectProgressExtendInfo> buildQueryWrapper(HdProjectProgressExtendInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectProgressExtendInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectProgressId() != null, HdProjectProgressExtendInfo::getProjectProgressId, bo.getProjectProgressId());
        lqw.eq(StringUtils.isNotBlank(bo.getArrival()), HdProjectProgressExtendInfo::getArrival, bo.getArrival());
        lqw.eq(StringUtils.isNotBlank(bo.getProduction()), HdProjectProgressExtendInfo::getProduction, bo.getProduction());
        lqw.eq(StringUtils.isNotBlank(bo.getBlack()), HdProjectProgressExtendInfo::getBlack, bo.getBlack());
        lqw.eq(StringUtils.isNotBlank(bo.getWhite()), HdProjectProgressExtendInfo::getWhite, bo.getWhite());
        lqw.eq(StringUtils.isNotBlank(bo.getDelivery()), HdProjectProgressExtendInfo::getDelivery, bo.getDelivery());
        return lqw;
    }

    /**
     * 新增项目进度扩展信息
     */
    @Override
    public Boolean insertByBo(HdProjectProgressExtendInfoBo bo) {
        HdProjectProgressExtendInfo add = MapstructUtils.convert(bo, HdProjectProgressExtendInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目进度扩展信息
     */
    @Override
    public Boolean updateByBo(HdProjectProgressExtendInfoBo bo) {
        HdProjectProgressExtendInfo update = MapstructUtils.convert(bo, HdProjectProgressExtendInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectProgressExtendInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目进度扩展信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 查询项目进度扩展信息
     */
    public HdProjectProgressExtendInfoVo queryByBo(HdProjectProgressExtendInfoBo bo){
        return baseMapper.selectVoOne(Wrappers.<HdProjectProgressExtendInfo>query().lambda().eq(HdProjectProgressExtendInfo::getProjectProgressId,bo.getProjectProgressId()));
    }

    /**
     * 获取所有的进度信息
     * @return
     */
   public  List<HdProjectProgressExtendVo>  getAllProgressList(){
         return baseMapper.getAllProgressList();
   }
}
