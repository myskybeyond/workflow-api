package org.dromara.system.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.system.domain.bo.SysRoleBo;
import org.dromara.system.domain.bo.SysSocialBo;
import org.dromara.system.domain.vo.SysRoleVo;
import org.dromara.system.domain.vo.SysSocialVo;

import java.util.List;

/**
 * 社会化关系Service接口
 *
 * @author thiszhc
 */
public interface ISysSocialService {


    /**
     * 查询社会化关系
     */
    SysSocialVo queryById(String id);

    /**
     * 查询社会化关系列表
     */
    List<SysSocialVo> queryList();

    /**
     * 查询社会化关系列表
     */
    List<SysSocialVo> queryListByUserId(Long userId);

    /**
     * 新增授权关系
     */
    Boolean insertByBo(SysSocialBo bo);

    /**
     * 更新社会化关系
     */
    Boolean updateByBo(SysSocialBo bo);

    /**
     * 删除社会化关系信息
     */
    Boolean deleteWithValidById(Long id);


    /**
     * 根据 authId 查询 SysSocial 表和 SysUser 表，返回 SysSocialAuthResult 映射的对象
     * @param authId 认证ID
     * @return SysSocial
     */
    List<SysSocialVo> selectByAuthId(String authId);

    /**
     * 分页查询
     * @param bo
     * @param pageQuery
     * @return
     */
    TableDataInfo<SysSocialVo> selectPageSocialList(SysSocialBo bo, PageQuery pageQuery);

    List<SysSocialVo> selectSocialList(SysSocialBo bo);


}
