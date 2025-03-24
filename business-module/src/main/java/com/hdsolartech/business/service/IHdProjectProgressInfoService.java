package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdProjectProgressInfo;
import com.hdsolartech.business.domain.vo.HdProjectProgressInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectProgressInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目进度信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-19
 */
public interface IHdProjectProgressInfoService {

    /**
     * 查询项目进度信息
     */
    HdProjectProgressInfoVo queryById(Long id);

    /**
     * 查询项目进度信息列表
     */
    TableDataInfo<HdProjectProgressInfoVo> queryPageList(HdProjectProgressInfoBo bo, PageQuery pageQuery);

    /**
     * 查询项目进度信息列表
     */
    List<HdProjectProgressInfoVo> queryList(HdProjectProgressInfoBo bo);

    /**
     * 新增项目进度信息
     */
    Boolean insertByBo(HdProjectProgressInfoBo bo);

    /**
     * 修改项目进度信息
     */
    Boolean updateByBo(HdProjectProgressInfoBo bo);

    /**
     * 校验并批量删除项目进度信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 同步方法
     * @param bo
     * @return
     */
    Boolean sync(HdProjectProgressInfoBo bo);

    /**
     * 查询所有的进度信息列表
     * @param bo
     * @return
     */
    List<HdProjectProgressInfoVo>  getAllList(HdProjectProgressInfoBo bo);
}
