package com.myskybeyond.flowable.service;

import com.myskybeyond.flowable.core.domain.ProcessQuery;
import com.myskybeyond.flowable.domain.vo.WfDeployVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import java.util.List;

/**
 * @author KonBAI
 * @createTime 2022/6/30 9:03
 */
public interface IWfDeployService {

    TableDataInfo<WfDeployVo> queryPageList(ProcessQuery processQuery, PageQuery pageQuery);

    TableDataInfo<WfDeployVo> queryPublishList(String processKey, PageQuery pageQuery);

    void updateState(String definitionId, String stateCode);

    String queryBpmnXmlById(String definitionId);

    void deleteByIds(List<String> deployIds);
}
