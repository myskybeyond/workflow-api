package com.myskybeyond.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.business.domain.HdOrderFlwRelation;
import com.myskybeyond.business.domain.bo.HdOrderFlwRelationBo;
import com.myskybeyond.business.domain.vo.HdOrderFlwRelationVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.Map;

/**
 * 订单流程关系Mapper接口
 *
 * @author Lion Li
 * @date 2024-06-24
 */
public interface HdOrderFlwRelationMapper extends BaseMapperPlus<HdOrderFlwRelation, HdOrderFlwRelationVo> {

//    Page<Map> queryOrderList(@Param("param") HdOrderFlwRelationBo param, @Param("page") Page page,@Param("userId")Long userId);
    Page<Map> queryOrderList(@Param("param") HdOrderFlwRelationBo param, @Param("page") Page page);

}
