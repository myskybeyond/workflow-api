package org.dromara.system.service;

import org.dromara.system.domain.HdUserWf;
import org.dromara.system.domain.vo.HdUserWfVo;
import org.dromara.system.domain.bo.HdUserWfBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.List;

/**
 * 用户于流程关联
Service接口
 *
 * @author Lion Li
 * @date 2024-06-14
 */
public interface IHdUserWfService {

    /**
     * 查询用户于流程关联

     */
    HdUserWfVo queryById(Long userId);

    /**
     * 查询用户于流程关联
列表
     */
    TableDataInfo<HdUserWfVo> queryPageList(HdUserWfBo bo, PageQuery pageQuery);

    /**
     * 查询用户于流程关联
列表
     */
    List<HdUserWfVo> queryList(HdUserWfBo bo);

    /**
     * 新增用户于流程关联

     */
    Boolean insertByBo(HdUserWfBo bo);

    /**
     * 修改用户于流程关联

     */
    Boolean updateByBo(HdUserWfBo bo);

    /**
     * 校验并批量删除用户于流程关联
信息
     */
    Boolean deleteWithValidByIds(String ids, Boolean isValid);
}
