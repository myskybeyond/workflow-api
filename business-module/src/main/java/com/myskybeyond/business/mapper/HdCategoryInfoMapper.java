package com.myskybeyond.business.mapper;

import com.myskybeyond.business.domain.HdCategoryInfo;
import com.myskybeyond.business.domain.bo.HdCategoryInfoBo;
import com.myskybeyond.business.domain.vo.HdCategoryInfoVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 系统类别信息Mapper接口
 *
 * @author Lion Li
 * @date 2024-05-28
 */
public interface HdCategoryInfoMapper extends BaseMapperPlus<HdCategoryInfo, HdCategoryInfoVo> {

    /**
     * 查询类别信息增加数量查询
     * @param bo
     * @return
     */
    List<HdCategoryInfoVo> queryNumList(@Param("CateBo") HdCategoryInfoBo bo);

    /**
     * 查询类别信息增加数量查询
     * @param bo
     * @return
     */
    List<HdCategoryInfoVo> queryFileNumList(@Param("CateBo") HdCategoryInfoBo bo);

    /**
     * 查询类别信息增加数量查询
     * @param bo
     * @return
     */
    List<HdCategoryInfoVo> queryMyFileNumList(@Param("CateBo") HdCategoryInfoBo bo);

}
