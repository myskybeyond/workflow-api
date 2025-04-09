package com.myskybeyond.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.myskybeyond.business.domain.HdCategoryInfo;
import com.myskybeyond.business.domain.HdComponentInfo;
import com.myskybeyond.business.domain.bo.HdCategoryInfoBo;
import com.myskybeyond.business.domain.vo.HdCategoryInfoVo;
import com.myskybeyond.business.mapper.HdCategoryInfoMapper;
import com.myskybeyond.business.mapper.HdComponentInfoMapper;
import com.myskybeyond.business.service.IHdCategoryInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统类别信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-05-28
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class HdCategoryInfoServiceImpl implements IHdCategoryInfoService {

    private final HdCategoryInfoMapper baseMapper;


    private final HdComponentInfoMapper componentInfoMapper;

    /**
     * 查询系统类别信息
     */
    @Override
    public HdCategoryInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }




    /**
     * 查询系统类别信息列表
     */
    @Override
    public List<HdCategoryInfoVo> queryList(HdCategoryInfoBo bo) {
        LambdaQueryWrapper<HdCategoryInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdCategoryInfo> buildQueryWrapper(HdCategoryInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdCategoryInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), HdCategoryInfo::getCategory, bo.getCategory());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdCategoryInfo::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getCode()), HdCategoryInfo::getCode, bo.getCode());
        lqw.orderByAsc(HdCategoryInfo::getSort).orderByDesc(HdCategoryInfo::getUpdateTime);
        return lqw;
    }

    /**
     * 新增系统类别信息
     */
    @Override
    public Boolean insertByBo(HdCategoryInfoBo bo) {
        HdCategoryInfo add = MapstructUtils.convert(bo, HdCategoryInfo.class);
        validEntityBeforeSave(add);
        //查询父级的分类信息
        HdCategoryInfo info = baseMapper.selectById(bo.getParentId());
        //分类信息
        if(info != null){
            add.setAncestors(info.getAncestors() + StringUtils.SEPARATOR + info.getId());;
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改系统类别信息
     */
    @Override
    public Boolean updateByBo(HdCategoryInfoBo bo) {
        HdCategoryInfo update = MapstructUtils.convert(bo, HdCategoryInfo.class);
        validEntityBeforeSave(update);
        //查询分类信息
        HdCategoryInfo oldCate = baseMapper.selectById(update.getId());
        if (!oldCate.getParentId().equals(update.getParentId())) {
            HdCategoryInfo info = baseMapper.selectById(update.getParentId());
            String newAncestors = info.getAncestors() + StringUtils.SEPARATOR + info.getId();
            update.setAncestors(newAncestors);
        }
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdCategoryInfo entity){
        //TODO 做一些数据校验,如唯一约束
        HdCategoryInfoVo info = this.queryByCodeAndCategory(entity.getCode(),entity.getCategory());
        if(info != null && info.getId().longValue() != entity.getId().longValue()){
            log.error("存在重复编号的分类信息");
            throw new ServiceException("存在重复编号的分类信息");
        }
    }

    /**
     * 批量删除系统类别信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 是否存在分类子节点
     *
     * @param id 分类id
     * @return 结果
     */
    @Override
    public boolean hasChildById(Long id){
        return baseMapper.exists(new LambdaQueryWrapper<HdCategoryInfo>()
            .eq(HdCategoryInfo::getParentId, id));
    }


    /**
     * 查询分类是否存在构件信息
     *
     * @param id 分类ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public  boolean checkExistComponent(Long id){
        return componentInfoMapper.exists(new LambdaQueryWrapper<HdComponentInfo>()
            .eq(HdComponentInfo::getCategoryId, id));
    }


    /**
     * 查询分类列表，增加数量信息
     */
    @Override
    public List<HdCategoryInfoVo> queryNumList(HdCategoryInfoBo bo){
        return baseMapper.queryNumList(bo);
    }



    /**
     * 根据料号查询分类
     */
    @Override
    public HdCategoryInfoVo queryByCode(String code){
        //查询父级的分类信息
        HdCategoryInfo info = baseMapper.selectOne(Wrappers.<HdCategoryInfo>query().lambda().eq(HdCategoryInfo::getCode,code));
        if(info == null){
            return null;
        }
        return  MapstructUtils.convert(info, HdCategoryInfoVo.class);

    }

    /**
     * 根据code 与分类查询数据
     */
    public HdCategoryInfoVo queryByCodeAndCategory(String code , String category){
        //查询父级的分类信息
        HdCategoryInfo info = baseMapper.selectOne(Wrappers.<HdCategoryInfo>query().lambda().eq(HdCategoryInfo::getCode,code).eq(HdCategoryInfo::getCategory,category));
        if(info == null){
            return null;
        }
        return  MapstructUtils.convert(info, HdCategoryInfoVo.class);

    }


    /**
     * 查询分类列表，增加数量信息
     */
    @Override
    public List<HdCategoryInfoVo> queryFileNumList(HdCategoryInfoBo bo){
        return baseMapper.queryFileNumList(bo);
    }

    /**
     * 查询分类列表，增加数量信息
     */
    @Override
    public List<HdCategoryInfoVo> queryMyFileNumList(HdCategoryInfoBo bo){
        //设置当前用户id 参数
        bo.setUpdateBy(LoginHelper.getUserId());
        return baseMapper.queryMyFileNumList(bo);
    }

}
