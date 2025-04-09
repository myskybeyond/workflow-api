package com.myskybeyond.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 应用信息对象 hd_app_info
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_app_info")
public class HdAppInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用类型
     */
    private String appType;

    /**
     * 客户端编号
     */
    private String appKey;

    /**
     * 客户端密钥
     */
    private String appSecret;

    /**
     * AGENT_ID
     */
    private String agentId;

    /**
     * 状态;0:正常 1:停用
     */
    private Long status;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;

    private String appCode;

    private Integer sort;


}
