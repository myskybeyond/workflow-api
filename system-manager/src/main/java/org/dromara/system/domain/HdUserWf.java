package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.util.Date;

/**
 * 用户于流程关联
对象 hd_user_wf
 *
 * @author Lion Li
 * @date 2024-06-14
 */
@Data
@TableName("hd_user_wf")
public class HdUserWf {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 流程id
     */
    private String wfId;

    /**
     * 流程名
     */
    private String wfName;

    /**
     * 流程标识
     */
    private String processKey;

    private Date updateTime;

    private String deploymentId;


}
