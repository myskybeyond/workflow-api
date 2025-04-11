package com.myskybeyond.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.myskybeyond.business.domain.HdProjectCommentInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 评论信息vo
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdProjectCommentInfo.class)
public class CommentInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
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
     * 评论用户id
     */
    private Long uid;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者
     */
    private Long createBy;

    private Reply reply;

     @Data
     public static class Reply extends  Page<CommentInfoVo>{

     }


     public  CommentInfoVo(HdProjectCommentInfoVo inVo){
         this.setId(inVo.getId());
         this.setOrderId(inVo.getOrderId());
         this.setProjectId(inVo.getProjectId());
         this.setContent(inVo.getContent());
         this.setCreateTime(inVo.getCreateTime());
         this.setCreateBy(inVo.getCreateBy());
         this.setUid(inVo.getCreateBy());
     }


}
