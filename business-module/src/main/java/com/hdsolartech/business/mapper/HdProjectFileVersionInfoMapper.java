package com.hdsolartech.business.mapper;

import com.hdsolartech.business.domain.HdProjectFileVersionInfo;
import com.hdsolartech.business.domain.vo.HdProjectFileVersionInfoVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

/**
 * 项目文件版本信息Mapper接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface HdProjectFileVersionInfoMapper extends BaseMapperPlus<HdProjectFileVersionInfo, HdProjectFileVersionInfoVo> {


    void updateSyncStatus(@Param("projectFileId") Long projectFileId ,  @Param("version") Long version , @Param("sync") Long sync);

}
