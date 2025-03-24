package com.hdsolartech.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.domain.HdProjectCommentInfo;
import com.hdsolartech.business.domain.vo.CommentInfoVo;
import com.hdsolartech.business.domain.vo.HdProjectCommentInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectCommentInfoBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 项目评论信息Service接口
 *
 * @author Lion Li
 * @date 2024-06-03
 */
public interface IHdProjectCommentInfoService {

    /**
     * 查询项目评论信息
     */
    HdProjectCommentInfoVo queryById(Long id);

    /**
     * 查询项目评论信息列表
     */
    TableDataInfo<HdProjectCommentInfoVo> queryPageList(HdProjectCommentInfoBo bo, PageQuery pageQuery);

    /**
     * 查询项目评论信息列表
     */
    List<HdProjectCommentInfoVo> queryList(HdProjectCommentInfoBo bo);

    /**
     * 新增项目评论信息
     */
    Boolean insertByBo(HdProjectCommentInfoBo bo);

    /**
     * 修改项目评论信息
     */
    Boolean updateByBo(HdProjectCommentInfoBo bo);

    /**
     * 校验并批量删除项目评论信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询项目评论信息列表
     */
    TableDataInfo<CommentInfoVo> queryCommentPageList(HdProjectCommentInfoBo bo, PageQuery pageQuery);

    /**
     * 查询回复内容
     * @param pageQuery
     * @param parentId
     * @return
     */
    Page<CommentInfoVo> replyPage( Long parentId,PageQuery pageQuery);

}
