package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdCategoryInfo;
import com.hdsolartech.business.domain.vo.HdCategoryInfoVo;
import com.hdsolartech.business.domain.bo.HdCategoryInfoBo;

import java.util.Collection;
import java.util.List;

/**
 * 系统类别信息Service接口
 *
 * @author Lion Li
 * @date 2024-05-28
 */
public interface IHdCategoryInfoService {

    /**
     * 查询系统类别信息
     */
    HdCategoryInfoVo queryById(Long id);


    /**
     * 查询系统类别信息列表
     */
    List<HdCategoryInfoVo> queryList(HdCategoryInfoBo bo);



    /**
     * 查询分类列表，增加数量信息
     */
    List<HdCategoryInfoVo> queryNumList(HdCategoryInfoBo bo);

    /**
     * 新增系统类别信息
     */
    Boolean insertByBo(HdCategoryInfoBo bo);

    /**
     * 修改系统类别信息
     */
    Boolean updateByBo(HdCategoryInfoBo bo);

    /**
     * 校验并批量删除系统类别信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 是否存在分类子节点
     *
     * @param id 分类id
     * @return 结果
     */
    boolean hasChildById(Long id);


    /**
     * 查询分类是否存在构件信息
     *
     * @param id 分类ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkExistComponent(Long id);



    /**
     * 根据料号查询分类
     */
    HdCategoryInfoVo queryByCode(String code);

    /**
     * 查询分类列表，增加数量信息
     */
    List<HdCategoryInfoVo> queryFileNumList(HdCategoryInfoBo bo);

    /**
     * 查询分类列表，增加数量信息
     */
    List<HdCategoryInfoVo> queryMyFileNumList(HdCategoryInfoBo bo);
}
