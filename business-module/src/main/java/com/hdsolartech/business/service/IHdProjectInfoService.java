package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdProjectInfo;
import com.hdsolartech.business.domain.vo.HdProjectInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface IHdProjectInfoService {

    /**
     * 查询项目信息
     */
    HdProjectInfoVo queryById(Long id);

    /**
     * 查询项目信息列表
     */
    TableDataInfo<HdProjectInfoVo> queryPageList(HdProjectInfoBo bo, PageQuery pageQuery);

    /**
     * 查询项目信息列表
     */
    List<HdProjectInfoVo> queryList(HdProjectInfoBo bo);

    /**
     * 新增项目信息
     */
    Boolean insertByBo(HdProjectInfoBo bo);

    /**
     * 修改项目信息
     */
    Boolean updateByBo(HdProjectInfoBo bo);

    /**
     * 校验并批量删除项目信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取未完成的项目
     * @return
     */
    List<Long>  undoneList();


    /**
     * 查询项目信息列表
     */
    List<HdProjectInfoVo> queryMyList(HdProjectInfoBo bo);

}
