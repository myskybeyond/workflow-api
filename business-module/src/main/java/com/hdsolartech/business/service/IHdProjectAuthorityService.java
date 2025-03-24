package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdProjectAuthority;
import com.hdsolartech.business.domain.vo.HdProjectAuthorityVo;
import com.hdsolartech.business.domain.bo.HdProjectAuthorityBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目权限信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface IHdProjectAuthorityService {

    /**
     * 查询项目权限信息
     */
    HdProjectAuthorityVo queryById(Long id);

    /**
     * 查询项目权限信息列表
     */
    TableDataInfo<HdProjectAuthorityVo> queryPageList(HdProjectAuthorityBo bo, PageQuery pageQuery);

    /**
     * 查询项目权限信息列表
     */
    List<HdProjectAuthorityVo> queryList(HdProjectAuthorityBo bo);

    /**
     * 新增项目权限信息
     */
    Boolean insertByBo(HdProjectAuthorityBo bo);

    /**
     * 修改项目权限信息
     */
    Boolean updateByBo(HdProjectAuthorityBo bo);

    /**
     * 校验并批量删除项目权限信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 项目权限信息
     *
     * @param bo
     * @return
     */
    HdProjectAuthorityVo queryByBo(HdProjectAuthorityBo bo);

    /**
     * 查询我的项目列表
     *
     * @param userId
     * @return
     */
    List getMyProjectList(Long userId);
}
