package org.dromara.common.core.service;


public interface ProgressService {

    /**
     * 根据进度id查询进度名称
     * @param progressId
     * @return
     */
    String selectNameByProgressId(Long progressId);


}
