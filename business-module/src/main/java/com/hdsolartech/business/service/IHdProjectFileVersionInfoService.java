package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdProjectFileVersionInfo;
import com.hdsolartech.business.domain.vo.HdProjectFileVersionInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectFileVersionInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目文件版本信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface IHdProjectFileVersionInfoService {

    /**
     * 查询项目文件版本信息
     */
    HdProjectFileVersionInfoVo queryById(Long id);

    /**
     * 查询项目文件版本信息列表
     */
    TableDataInfo<HdProjectFileVersionInfoVo> queryPageList(HdProjectFileVersionInfoBo bo, PageQuery pageQuery);

    /**
     * 查询项目文件版本信息列表
     */
    List<HdProjectFileVersionInfoVo> queryList(HdProjectFileVersionInfoBo bo);

    /**
     * 新增项目文件版本信息
     */
    Boolean insertByBo(HdProjectFileVersionInfoBo bo);

    /**
     * 修改项目文件版本信息
     */
    Boolean updateByBo(HdProjectFileVersionInfoBo bo);

    /**
     * 校验并批量删除项目文件版本信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
