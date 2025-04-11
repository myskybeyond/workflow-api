package com.myskybeyond.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 项目进度扩展信息对象 hd_project_progress_extend_info
 *
 * @author Lion Li
 * @date 2024-06-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_project_progress_extend_info")
public class HdProjectProgressExtendInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 关联进度ID
     */
    private Long projectProgressId;

    /**
     * 到货进度
     */
    private String arrival;

    /**
     * 生产进度
     */
    private String production;

    /**
     * 镀锌进度 黑件
     */
    private String black;

    /**
     * 镀锌进度 白件
     */
    private String white;

    /**
     * 发货进度
     */
    private String delivery;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;


}
