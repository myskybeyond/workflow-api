package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.bo.HdComponentInfoBo;
import com.hdsolartech.business.domain.vo.HdComponentInfoVo;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 构件信息Service接口
 *
 * @author Lion Li
 * @date 2024-05-29
 */
public interface IHdComponentInfoService {

    /**
     * 查询构件信息
     */
    HdComponentInfoVo queryById(Long id);


    /**
     * 查询构件信息列表
     */
    List<HdComponentInfoVo> queryList(HdComponentInfoBo bo);


    /**
     * 分页查询组件信息
     * @param bo
     * @param pageQuery
     * @return
     */
    TableDataInfo<HdComponentInfoVo> queryPageList(HdComponentInfoBo bo, PageQuery pageQuery);


    /**
     * 查询构件信息列表
     */
    List<HdComponentInfoVo> queryExportList(HdComponentInfoBo bo);

    /**
     * 新增构件信息
     */
    Boolean insertByBo(HdComponentInfoBo bo);

    /**
     * 修改构件信息
     */
    Boolean updateByBo(HdComponentInfoBo bo);

    /**
     * 校验并批量删除构件信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 修改状态
     */
    int updateStatus(Long id, Long status);


    /**
     * 查询构件信息
     */
    HdComponentInfoVo queryByName(String name);

    /**
     * 根据id查询需要插入的信息
     * @param ids
     * @return
     */
    List<String> getInsList( Long[] ids);


}
