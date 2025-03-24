package com.hdsolartech.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdCategoryInfo;
import com.hdsolartech.business.domain.HdComponentInfo;
import com.hdsolartech.business.domain.bo.HdComponentInfoBo;
import com.hdsolartech.business.domain.vo.ComponentColumnVo;
import com.hdsolartech.business.domain.vo.HdComponentInfoVo;
import com.hdsolartech.business.mapper.HdCategoryInfoMapper;
import com.hdsolartech.business.mapper.HdComponentInfoMapper;
import com.hdsolartech.business.service.IHdComponentInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.ConfigService;
import org.dromara.common.core.service.DictService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.helper.DataBaseHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 构件信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-05-29
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class HdComponentInfoServiceImpl implements IHdComponentInfoService {

    private final HdComponentInfoMapper baseMapper;

    private final HdCategoryInfoMapper categoryInfoMapper;

    /**
     * 查询构件信息
     */
    @Override
    public HdComponentInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询构件信息列表
     */
    @Override
    public List<HdComponentInfoVo> queryList(HdComponentInfoBo bo) {
        LambdaQueryWrapper<HdComponentInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    /**
     * 分页查询组件信息
     * @param bo
     * @param pageQuery
     * @return
     */
    @Override
    public TableDataInfo<HdComponentInfoVo> queryPageList(HdComponentInfoBo bo, PageQuery pageQuery){
        LambdaQueryWrapper<HdComponentInfo> lqw = buildQueryWrapper(bo);
        Page<HdComponentInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }




    private LambdaQueryWrapper<HdComponentInfo> buildQueryWrapper(HdComponentInfoBo bo) {
        LambdaQueryWrapper<HdComponentInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), HdComponentInfo::getCategory, bo.getCategory());

        lqw.eq(bo.getParentId() != null, HdComponentInfo::getParentId, bo.getParentId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdComponentInfo::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getPartNo()), HdComponentInfo::getPartNo, bo.getPartNo());
        lqw.like(StringUtils.isNotBlank(bo.getType()), HdComponentInfo::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getDrgNo()), HdComponentInfo::getDrgNo, bo.getDrgNo());
        lqw.like(StringUtils.isNotBlank(bo.getMaterial()), HdComponentInfo::getMaterial, bo.getMaterial());
        lqw.like(StringUtils.isNotBlank(bo.getSpec()), HdComponentInfo::getSpec, bo.getSpec());
        lqw.eq(StringUtils.isNotBlank(bo.getTheoreticalSize()), HdComponentInfo::getTheoreticalSize, bo.getTheoreticalSize());
        lqw.eq(StringUtils.isNotBlank(bo.getMaterialSize()), HdComponentInfo::getMaterialSize, bo.getMaterialSize());
        lqw.eq(bo.getNum() != null, HdComponentInfo::getNum, bo.getNum());
        lqw.eq(StringUtils.isNotBlank(bo.getUnit()), HdComponentInfo::getUnit, bo.getUnit());
        lqw.eq(bo.getLength() != null, HdComponentInfo::getLength, bo.getLength());
        lqw.eq(bo.getMeterWeight() != null, HdComponentInfo::getMeterWeight, bo.getMeterWeight());
        lqw.eq(bo.getWeight() != null, HdComponentInfo::getWeight, bo.getWeight());
        lqw.eq(StringUtils.isNotBlank(bo.getMouldNo()), HdComponentInfo::getMouldNo, bo.getMouldNo());
        lqw.eq(StringUtils.isNotBlank(bo.getStripSize()), HdComponentInfo::getStripSize, bo.getStripSize());
        lqw.eq(StringUtils.isNotBlank(bo.getSeamWidth()), HdComponentInfo::getSeamWidth, bo.getSeamWidth());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), HdComponentInfo::getDescription, bo.getDescription());
        lqw.eq(StringUtils.isNotBlank(bo.getFinishPartNo()), HdComponentInfo::getFinishPartNo, bo.getFinishPartNo());
        lqw.eq(StringUtils.isNotBlank(bo.getBlackPartNo()), HdComponentInfo::getBlackPartNo, bo.getBlackPartNo());
        lqw.eq(StringUtils.isNotBlank(bo.getOutPartNo()), HdComponentInfo::getOutPartNo, bo.getOutPartNo());
        lqw.eq(StringUtils.isNotBlank(bo.getManufactureProcess()), HdComponentInfo::getManufactureProcess, bo.getManufactureProcess());
        lqw.eq(bo.getStatus() != null, HdComponentInfo::getStatus, bo.getStatus());
        lqw.eq(bo.getSort() != null, HdComponentInfo::getSort, bo.getSort());
        lqw.and(bo.getCategoryId() != null,
            query -> {
                List<HdCategoryInfo> categoryList = categoryInfoMapper.selectList(new LambdaQueryWrapper<HdCategoryInfo>()
                    .select(HdCategoryInfo::getId)
                    .apply(DataBaseHelper.findInSet(bo.getCategoryId(), "ancestors")));
                List<Long> ids = StreamUtils.toList(categoryList, HdCategoryInfo::getId);
                ids.add(bo.getCategoryId());
                query.in(HdComponentInfo::getCategoryId, ids);
            });
        return lqw;
    }



    /**
     * 查询导出构件信息列表
     */
    @Override
    public List<HdComponentInfoVo> queryExportList(HdComponentInfoBo bo){
        //查询是否存在分类信息
        if(bo.getCategoryId() != null){
            List<HdCategoryInfo> categoryList = categoryInfoMapper.selectList(new LambdaQueryWrapper<HdCategoryInfo>()
                .select(HdCategoryInfo::getId)
                .apply(DataBaseHelper.findInSet(bo.getCategoryId(), "ancestors")));
            List<Long> ids = StreamUtils.toList(categoryList, HdCategoryInfo::getId);
            ids.add(bo.getCategoryId());
            //in 查询
            bo.setCategoryIds(ids);
        }

       return baseMapper.queryExportList(bo);
    }


    /**
     * 新增构件信息
     */
    @Override
    public Boolean insertByBo(HdComponentInfoBo bo) {
        HdComponentInfo add = MapstructUtils.convert(bo, HdComponentInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改构件信息
     */
    @Override
    public Boolean updateByBo(HdComponentInfoBo bo) {
        HdComponentInfo update = MapstructUtils.convert(bo, HdComponentInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdComponentInfo entity){
        //TODO 做一些数据校验,如唯一约束
//        HdComponentInfoVo info = this.queryByName(entity.getName());
//        if(info != null && info.getId() != entity.getId()){
//            log.error("存在重复名称的组件信息");
//            throw new ServiceException("存在重复名称的组件信息");
//        }
    }

    /**
     * 批量删除构件信息
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
            new LambdaUpdateWrapper<HdComponentInfo>()
                .set(HdComponentInfo::getStatus, status)
                .eq(HdComponentInfo::getId, id));
    }

    @Override
    public HdComponentInfoVo queryByName(String name){
        //查询父级的分类信息
        HdComponentInfo info = baseMapper.selectOne(Wrappers.<HdComponentInfo>query().lambda().eq(HdComponentInfo::getName,name).orderByDesc(HdComponentInfo::getId).last("  limit 1"));
        if(info == null){
            return null;
        }
        return  MapstructUtils.convert(info, HdComponentInfoVo.class);
    }


    /**
     * 根据id查询需要插入的信息
     * @param ids
     * @return
     */
    @Override
    public List<String> getInsList( Long[] ids){
        List<HdComponentInfoVo> list = baseMapper.selectVoBatchIds(List.of(ids),HdComponentInfoVo.class);
        //集合是否为空
        if( CollUtil.isNotEmpty(list) ){
          //获取系统配置信息
          String config = SpringUtils.getBean(ConfigService.class).getConfigValue(UserConstants.CONFIG_COMPONENT_INS_COLUMNS);
          if(StrUtil.isEmpty(config)){
              log.error("未查询到配置项为[business.component.ins.columns]的参数信息");
              return null;
          }
          //解析结构 获取参数配置
          List<ComponentColumnVo>  columnVos = JsonUtils.parseArray(config, ComponentColumnVo.class);
          if(CollUtil.isEmpty(columnVos)){
              log.error("转化配置项为[business.component.ins.columns]的参数为空");
              return null;
          }
          List<String> res  =  list.stream().map( obj -> convertColumns(columnVos,obj)).collect(Collectors.toList());
          return res;
        }else{
            //未查询到数据
            log.error("未查询到strList数据");
            return null;
        }
    }

    public String convertColumns(List<ComponentColumnVo> columnVos , HdComponentInfoVo info){

       //根据info中的cantegory 查询 ComponentColumnVo 信息
        ComponentColumnVo columnVo = columnVos.stream().filter(vo -> Objects.equals(vo.getCategory(),info.getCategory())).findAny().orElse(null);
        if(columnVo == null){
            log.error("根据类别未获取到配置信息");
            return null;
        }
        //组织列信息
        String[] columns = columnVo.getColumn().split(StringUtils.SEPARATOR);
        //查询反射信息
        List<String> res = new ArrayList<>();
        for( int i= 0; i< columns.length ; i++){
            //获取结果信息
            Object  obj = ReflectUtil.getFieldValue(info,columns[i]);
            if(columns[i].equals("manufactureProcess")){
                String  dictValue = SpringUtils.getBean(DictService.class).getDictValue("mfg_process", obj+"", StringUtils.SEPARATOR);
                res.add(dictValue);
                continue;
            }
            res.add((obj != null) ? obj.toString() : "");
        }
        return  String.join(StringUtils.SEPARATOR,res);
    }





}
