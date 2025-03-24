package com.hdsolartech.business.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.hdsolartech.business.common.constant.SmsInfoConstants;
import com.hdsolartech.business.domain.vo.CommentInfoVo;
import com.hdsolartech.business.domain.vo.HdOrderInfoVo;
import com.hdsolartech.business.domain.vo.HdProjectInfoVo;
import com.hdsolartech.business.mapper.HdOrderInfoMapper;
import com.hdsolartech.business.mapper.HdProjectInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.service.DingTalkService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.satoken.utils.LoginHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import com.hdsolartech.business.domain.bo.HdProjectCommentInfoBo;
import com.hdsolartech.business.domain.vo.HdProjectCommentInfoVo;
import com.hdsolartech.business.domain.HdProjectCommentInfo;
import com.hdsolartech.business.mapper.HdProjectCommentInfoMapper;
import com.hdsolartech.business.service.IHdProjectCommentInfoService;

import java.util.*;

/**
 * 项目评论信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HdProjectCommentInfoServiceImpl implements IHdProjectCommentInfoService {

    private final HdProjectCommentInfoMapper baseMapper;

    /**
     * 订单mapper
     */
    private final HdOrderInfoMapper orderInfoMapper;

    /**
     * 订单mapper
     */
    private final HdProjectInfoMapper projectInfoMapper;



    /**
     * 查询项目评论信息
     */
    @Override
    public HdProjectCommentInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目评论信息列表
     */
    @Override
    public TableDataInfo<HdProjectCommentInfoVo> queryPageList(HdProjectCommentInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectCommentInfo> lqw = buildQueryWrapper(bo);
        Page<HdProjectCommentInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目评论信息列表
     */
    @Override
    public List<HdProjectCommentInfoVo> queryList(HdProjectCommentInfoBo bo) {
        LambdaQueryWrapper<HdProjectCommentInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectCommentInfo> buildQueryWrapper(HdProjectCommentInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectCommentInfo> lqw = Wrappers.lambdaQuery();

        lqw.eq(bo.getProjectId() != null, HdProjectCommentInfo::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdProjectCommentInfo::getOrderId, bo.getOrderId());
        lqw.eq(bo.getType() != null, HdProjectCommentInfo::getType, bo.getType());
        lqw.eq(bo.getParentId() != null, HdProjectCommentInfo::getParentId, bo.getParentId());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), HdProjectCommentInfo::getContent, bo.getContent());
        lqw.eq(bo.getReplyNum() != null, HdProjectCommentInfo::getReplyNum, bo.getReplyNum());
        return lqw;
    }

    /**
     * 新增项目评论信息
     */
    @Override
    public Boolean insertByBo(HdProjectCommentInfoBo bo) {
        HdProjectCommentInfo add = MapstructUtils.convert(bo, HdProjectCommentInfo.class);
        validEntityBeforeSave(add);
        if(CollUtil.isNotEmpty(bo.getMentionList())){
            // {"projectName":"测试项目名称","projectId":1,"orderName":"测试销售订单名称","orderId":1}
            Map<String,Object> params = new HashMap<>();
            if(add.getProjectId() != null){
                HdProjectInfoVo pro = projectInfoMapper.selectVoById(add.getProjectId());
                if( pro != null){
                    params.put(SmsInfoConstants.PROJECT_ID,add.getProjectId()+StringUtils.SEPARATOR+pro.getName());
                }
            }
            if(add.getOrderId() != null){
                HdOrderInfoVo orderInfoVo = orderInfoMapper.selectVoById(add.getOrderId());
                if( orderInfoVo != null){
                    params.put(SmsInfoConstants.ORDER_ID,add.getOrderId() +StringUtils.SEPARATOR+orderInfoVo.getName());
                }
            }
            //调用消息发送接口
            for( int i = 0; i< bo.getMentionList().size() ; i++){
                SpringUtils.getBean(DingTalkService.class).send(bo.getMentionList().get(i).toString(),parseHtmlContent(bo.getContent()), LoginHelper.getUserId().toString(),params);
            }
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目评论信息
     */
    @Override
    public Boolean updateByBo(HdProjectCommentInfoBo bo) {
        HdProjectCommentInfo update = MapstructUtils.convert(bo, HdProjectCommentInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectCommentInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目评论信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

      /**
       * 查询项目评论信息列表
       */
      @Override
      public TableDataInfo<CommentInfoVo> queryCommentPageList(HdProjectCommentInfoBo bo, PageQuery pageQuery){
          //先查询分页返回的commentInfoVo
          LambdaQueryWrapper<HdProjectCommentInfo> lqw = buildCommentQuery(bo);
          Page<HdProjectCommentInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
          if( result.getTotal() <= 0){
              //直接构造空null 返回
              List<CommentInfoVo> res =new ArrayList<>();
              return TableDataInfo.build(res);
          }else {
              //存在分页数据 开始构造
              List<HdProjectCommentInfoVo>  commentList = result.getRecords();
              List<CommentInfoVo> commentListR = new ArrayList<>();
              for( int i = 0 ; i <commentList.size() ; i++){
                  CommentInfoVo vo = new CommentInfoVo(commentList.get(i));
                  PageQuery query = new PageQuery();
                  query.setPageNum(1);
                  query.setPageSize(5);
                  Page<CommentInfoVo> page = this.replyPage(vo.getId(),query);
                  vo.setReplyNum(page.getTotal());
                  CommentInfoVo.Reply reply = new CommentInfoVo.Reply();
                  BeanUtils.copyProperties(page,reply);
                  vo.setReply(reply);
                  commentListR.add(vo);
              }
              Page<CommentInfoVo> page = new  Page(pageQuery.getPageNum(), pageQuery.getPageSize(), result.getTotal());
              page.setRecords(commentListR);
              return TableDataInfo.build(page);
          }
      }
      @Override
    public Page<CommentInfoVo> replyPage( Long parentId , PageQuery pageQuery){
          LambdaQueryWrapper<HdProjectCommentInfo> lqw = Wrappers.lambdaQuery();
          lqw.eq( HdProjectCommentInfo::getParentId, parentId);
          lqw.orderByDesc(HdProjectCommentInfo::getCreateTime);
          Page<HdProjectCommentInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
          Page<CommentInfoVo> page = new  Page(pageQuery.getPageNum(), pageQuery.getPageSize(), result.getTotal());
         if ( result.getTotal() <= 0){
              //直接构造空null 返回
              return page;
          }else {
             //查询回复数据
             List<HdProjectCommentInfoVo>  commentList = result.getRecords();
             List<CommentInfoVo> list = new ArrayList<>();
             for( int i = 0 ; i <commentList.size() ; i++){
                 CommentInfoVo vo = new CommentInfoVo(commentList.get(i));
                 //查询回复数
                 Long reply = baseMapper.selectCount( Wrappers.<HdProjectCommentInfo>query().lambda().eq(HdProjectCommentInfo::getParentId,vo.getId()));
                 vo.setReplyNum(reply);
                 list.add(vo);
             }
             page.setRecords(list);

         }
          return page;
    }


    private LambdaQueryWrapper<HdProjectCommentInfo> buildCommentQuery(HdProjectCommentInfoBo bo) {
        LambdaQueryWrapper<HdProjectCommentInfo> lqw = Wrappers.lambdaQuery();
        if(bo.getOrderId() != null){
            //查询销售订单详情
            lqw.eq(bo.getOrderId() != null, HdProjectCommentInfo::getOrderId, bo.getOrderId());
        }else {
            lqw.eq(bo.getProjectId() != null, HdProjectCommentInfo::getProjectId, bo.getProjectId());
            //销售订单id 为null
            lqw.isNull(HdProjectCommentInfo::getOrderId);
        }
        lqw.isNull( HdProjectCommentInfo::getParentId);
        lqw.orderByDesc(HdProjectCommentInfo::getCreateTime);
        return lqw;
    }


    public String parseHtmlContent(String html){
        try{
          if(StrUtil.isEmpty(html)){
              return "";
          }
          //转换图片为普通文本
          Document doc = Jsoup.parse(html);
          Elements images = doc.select("img");
          for (Element image : images) {
              String altText = image.attr("alt");
              image.replaceWith(new Element("text").text(altText));
          }
          //去除表情
        //              Elements elements = doc.select("*");
        //              for (Element element : elements) {
        //                  String text = element.text();
        //                  if (text.contains("[")) {
        //                      String cleanedText = text.replaceAll("\\[.*?\\]", "");
        //                      element.text(cleanedText);
        //                  }
        //              }
          return doc.text();
        }catch(Exception e){
         log.error("转换html为普通的content出现异常",e);
         return "";
        }
    }

}
