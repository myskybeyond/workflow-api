package org.dromara.common.core.service;

import org.dromara.common.core.domain.vo.OssFileVo;

import java.util.List;

/**
 * 通用 OSS服务
 *
 * @author Lion Li
 */
public interface OssService {

    /**
     * 通过ossId查询对应的url
     *
     * @param ossIds ossId串逗号分隔
     * @return url串逗号分隔
     */
    String selectUrlByIds(String ossIds);


    /**
     * 通过ossIds 查询对应的文件ids
      * @param ossIds
     * @return
     */
    List<OssFileVo> selectFilesByIds(String ossIds);

}
