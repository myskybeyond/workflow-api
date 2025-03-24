package com.hdsolartech.business.mapper;

import com.hdsolartech.business.domain.HdProjectAuthority;
import com.hdsolartech.business.domain.vo.HdProjectAuthorityVo;
import com.hdsolartech.business.domain.vo.HdProjectInfoVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 项目权限信息Mapper接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface HdProjectAuthorityMapper extends BaseMapperPlus<HdProjectAuthority, HdProjectAuthorityVo> {

    List<HdProjectInfoVo> queryProjectListByUserId(@Param("userId") Long userId);

}
