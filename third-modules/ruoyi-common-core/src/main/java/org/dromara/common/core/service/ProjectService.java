package org.dromara.common.core.service;


public interface ProjectService {

    /**
     * 根据项目查询项目名称
     * @param projectId
     * @return
     */
    String selectNameByProjectId(Long projectId);


}
