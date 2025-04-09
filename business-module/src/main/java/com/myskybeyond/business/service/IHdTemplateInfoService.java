package com.myskybeyond.business.service;

import com.myskybeyond.business.domain.bo.HdTemplateInfoBo;
import com.myskybeyond.business.domain.vo.HdTemplateInfoVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 项目资料模板Service接口
 *
 * @author Lion Li
 * @date 2024-05-24
 */
public interface IHdTemplateInfoService {

    /**
     * 查询项目资料模板
     */
    HdTemplateInfoVo queryById(Long id);

    /**
     * 查询项目资料模板列表
     */
    TableDataInfo<HdTemplateInfoVo> queryPageList(HdTemplateInfoBo bo, PageQuery pageQuery);

    /**
     * 查询项目资料模板列表
     */
    List<HdTemplateInfoVo> queryList(HdTemplateInfoBo bo);

    /**
     * 新增项目资料模板
     */
    Boolean insertByBo(HdTemplateInfoBo bo);

    /**
     * 修改项目资料模板
     */
    Boolean updateByBo(HdTemplateInfoBo bo);

    /**
     * 校验并批量删除项目资料模板信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 修改状态
     */
    int updateStatus(Long id, Long status);
}
