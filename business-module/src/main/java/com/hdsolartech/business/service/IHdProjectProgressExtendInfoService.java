package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.bo.HdProjectProgressExtendInfoBo;
import com.hdsolartech.business.domain.vo.HdProjectProgressExtendInfoVo;
import com.hdsolartech.business.domain.vo.HdProjectProgressExtendVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 项目进度扩展信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-29
 */
public interface IHdProjectProgressExtendInfoService {

    /**
     * 查询项目进度扩展信息
     */
    HdProjectProgressExtendInfoVo queryById(Long id);

    /**
     * 查询项目进度扩展信息列表
     */
    TableDataInfo<HdProjectProgressExtendInfoVo> queryPageList(HdProjectProgressExtendInfoBo bo, PageQuery pageQuery);

    /**
     * 查询项目进度扩展信息列表
     */
    List<HdProjectProgressExtendInfoVo> queryList(HdProjectProgressExtendInfoBo bo);

    /**
     * 新增项目进度扩展信息
     */
    Boolean insertByBo(HdProjectProgressExtendInfoBo bo);

    /**
     * 修改项目进度扩展信息
     */
    Boolean updateByBo(HdProjectProgressExtendInfoBo bo);

    /**
     * 校验并批量删除项目进度扩展信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 查询项目进度扩展信息
     */
    HdProjectProgressExtendInfoVo queryByBo(HdProjectProgressExtendInfoBo bo);

    /**
     * 获取所有的进度信息
     * @return
     */
    List<HdProjectProgressExtendVo>  getAllProgressList();
}
