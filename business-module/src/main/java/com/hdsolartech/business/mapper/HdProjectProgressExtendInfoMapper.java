package com.hdsolartech.business.mapper;

import com.hdsolartech.business.domain.HdProjectProgressExtendInfo;
import com.hdsolartech.business.domain.vo.HdProjectProgressExtendInfoVo;
import com.hdsolartech.business.domain.vo.HdProjectProgressExtendVo;
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
