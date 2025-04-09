package com.myskybeyond.business.domain.bo;

import com.myskybeyond.business.domain.HdProjectCommentInfo;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.mybatis.core.domain.BaseEntity;

import java.util.List;

/**
 * 项目评论信息业务对象 hd_project_comment_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = HdProjectCommentInfo.class, reverseConvertGenerate = false)
public class HdProjectCommentInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long projectId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 类型;0:项目评论 1:订单评论
     */
    @NotNull(message = "类型;0:项目评论 1:订单评论不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long type;

    /**
     * 父ID;0为顶级
     */
    private Long parentId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String content;

    /**
     * 回复数
     */
    private Long replyNum;

    /**
     * 提及者 需要发送消息
     */
    private List<Long> mentionList;


}
