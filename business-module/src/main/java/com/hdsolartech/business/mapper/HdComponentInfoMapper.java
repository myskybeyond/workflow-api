package com.hdsolartech.business.mapper;

import com.hdsolartech.business.domain.HdComponentInfo;
import com.hdsolartech.business.domain.bo.HdComponentInfoBo;
import com.hdsolartech.business.domain.vo.HdComponentInfoVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 构件信息Mapper接口
 *
 * @author Lion Li
 * @date 2024-05-29
 */
public interface HdComponentInfoMapper extends BaseMapperPlus<HdComponentInfo, HdComponentInfoVo> {

    /**
     * 查询导出列表
     * @param bo
     * @return
     */
    List<HdComponentInfoVo> queryExportList(@Param("bo") HdComponentInfoBo bo);


    String   getConfigValue(@Param("key") String key);

}
