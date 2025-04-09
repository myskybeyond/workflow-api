package com.myskybeyond.business.service;

import com.myskybeyond.business.domain.bo.HdAppInfoBo;
import com.myskybeyond.business.domain.vo.HdAppInfoVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 应用信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-04
 */
public interface IHdAppInfoService {

    /**
     * 查询应用信息
     */
    HdAppInfoVo queryById(Long id);

    /**
     * 查询应用信息列表
     */
    TableDataInfo<HdAppInfoVo> queryPageList(HdAppInfoBo bo, PageQuery pageQuery);

    /**
     * 查询应用信息列表
     */
    List<HdAppInfoVo> queryList(HdAppInfoBo bo);

    /**
     * 新增应用信息
     */
    Boolean insertByBo(HdAppInfoBo bo);

    /**
     * 修改应用信息
     */
    Boolean updateByBo(HdAppInfoBo bo);

    /**
     * 校验并批量删除应用信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 修改应用状态
     * @param id 应用id
     * @param status 0-正常 1-停用
     */
    Boolean changeStatus(Long id, Long status);
}
