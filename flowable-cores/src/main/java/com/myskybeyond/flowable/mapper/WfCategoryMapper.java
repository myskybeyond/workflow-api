package com.myskybeyond.flowable.mapper;

import com.myskybeyond.flowable.domain.WfCategory;
import com.myskybeyond.flowable.domain.vo.DataGroupByCategoryVo;
import com.myskybeyond.flowable.domain.vo.WfCategoryVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 流程分类Mapper接口
 *
 * @author KonBAI
 * @date 2022-01-15
 */
public interface WfCategoryMapper extends BaseMapperPlus<WfCategory, WfCategoryVo> {

    List<DataGroupByCategoryVo> totalModalGroupByCategory();

    List<DataGroupByCategoryVo> totalDeploymentGroupByCategory();

    List<DataGroupByCategoryVo> totalToDoTaskGroupByCategory(@Param("userId") String userId);

    List<DataGroupByCategoryVo> totalMyProcessGroupByCategory(@Param("userId") String userId);

    List<WfCategory> queryConnectByPiror(@Param("startId") Long startId);

    List<DataGroupByCategoryVo> totalExecutor(@Param("executeSql") String executeSql,@Param("params") Object... params);

    List<String> queryIdsByCategory(@Param("tableName") String tableName, @Param("category") List<String> category);

}
