package com.hdsolartech.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdProjectFlwRelation;
import com.hdsolartech.business.domain.vo.HdProjectFlwRelationVo;
import com.hdsolartech.business.domain.bo.HdProjectFlwRelationBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 项目流程关系Service接口
 *
 * @author Lion Li
 * @date 2024-06-24
 */
public interface IHdProjectFlwRelationService {
    Page<Map> queryTrackingPageList(HdProjectFlwRelationBo bo, PageQuery pageQuery);
    /**
     * 查询项目流程关系
     */
    HdProjectFlwRelationVo queryById(Long id);

    /**
     * 查询项目流程关系列表
     */
    TableDataInfo<HdProjectFlwRelationVo> queryPageList(HdProjectFlwRelationBo bo, PageQuery pageQuery);

    /**
     * 查询项目流程关系列表
     */
    List<HdProjectFlwRelationVo> queryList(HdProjectFlwRelationBo bo);

    /**
     * 新增项目流程关系
     */
    Boolean insertByBo(HdProjectFlwRelationBo bo);

    /**
     * 修改项目流程关系
     */
    Boolean updateByBo(HdProjectFlwRelationBo bo);

    /**
     * 校验并批量删除项目流程关系信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
