package com.hdsolartech.business.service;

import com.hdsolartech.business.domain.HdProjectFileInfo;
import com.hdsolartech.business.domain.vo.HdProjectFileInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectFileInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目文件信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface IHdProjectFileInfoService {

    /**
     * 查询项目文件信息
     */
    HdProjectFileInfoVo queryById(Long id);

    /**
     * 查询项目文件信息列表
     */
    TableDataInfo<HdProjectFileInfoVo> queryPageList(HdProjectFileInfoBo bo, PageQuery pageQuery);

    /**
     * 查询项目文件信息列表
     */
    List<HdProjectFileInfoVo> queryList(HdProjectFileInfoBo bo);

    /**
     * 新增项目文件信息
     */
    Long insertByBo(HdProjectFileInfoBo bo);

    /**
     * 修改项目文件信息
     */
    Boolean updateByBo(HdProjectFileInfoBo bo);

    /**
     * 校验并批量删除项目文件信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);



    void updateFileInfo(String relaFileInfo ,Long projectId ,Long orderId ,Long userId);


    void handleOnlineFiles(List<String> onlineFiles,Long projectId ,Long orderId ,Long userId );

    /**
     * 查询项目文件信息列表
     */
    TableDataInfo<HdProjectFileInfoVo> myList(HdProjectFileInfoBo bo, PageQuery pageQuery);

    /**
     * 查询项目文件信息列表
     */
    TableDataInfo<HdProjectFileInfoVo> getListByTaskId(HdProjectFileInfoBo bo, PageQuery pageQuery);

}
