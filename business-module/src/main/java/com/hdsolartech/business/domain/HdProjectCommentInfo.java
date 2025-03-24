package com.hdsolartech.business.domain;

import org.dromara.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 项目评论信息对象 hd_project_comment_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_project_comment_info")
public class HdProjectCommentInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 类型;0:项目评论 1:订单评论
     */
    private Long type;

    /**
     * 父ID;0为顶级
     */
    private Long parentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 回复数
     */
    private Long replyNum;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;


}
