package com.myskybeyond.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.business.domain.HdProjectFlwRelation;
import com.myskybeyond.business.domain.bo.HdProjectFlwRelationBo;
import com.myskybeyond.business.domain.vo.HdProjectFlwRelationVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.Map;

/**
 * 项目流程关系Mapper接口
 *
 * @author Lion Li
 * @date 2024-06-24
 */
public interface HdProjectFlwRelationMapper extends BaseMapperPlus<HdProjectFlwRelation, HdProjectFlwRelationVo> {
    Page<Map> queryTracking(@Param("param") HdProjectFlwRelationBo param, @Param("page") Page page, @Param("userId")Long userId);
}
