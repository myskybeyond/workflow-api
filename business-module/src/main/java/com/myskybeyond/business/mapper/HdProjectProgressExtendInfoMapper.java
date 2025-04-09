package com.myskybeyond.business.mapper;

import com.myskybeyond.business.domain.HdProjectProgressExtendInfo;
import com.myskybeyond.business.domain.vo.HdProjectProgressExtendInfoVo;
import com.myskybeyond.business.domain.vo.HdProjectProgressExtendVo;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 项目进度扩展信息Mapper接口
 *
 * @author Lion Li
 * @date 2024-06-29
 */
public interface HdProjectProgressExtendInfoMapper extends BaseMapperPlus<HdProjectProgressExtendInfo, HdProjectProgressExtendInfoVo> {

    /**
     * 获取进度列表
     * @return
     */
    List<HdProjectProgressExtendVo> getAllProgressList();
}
