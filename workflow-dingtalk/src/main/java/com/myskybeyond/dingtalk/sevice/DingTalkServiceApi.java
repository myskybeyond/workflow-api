package com.myskybeyond.dingtalk.sevice;

import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendprogressRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationGetsendresultRequest;
import com.dingtalk.api.request.OapiUserGetbyunionidRequest;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendprogressResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationGetsendresultResponse;
import com.dingtalk.api.response.OapiUserGetbyunionidResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.social.config.properties.SocialProperties;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 钉钉工作通知消息相关api封装
 *
 * @author Lijt
 */
@Slf4j
@Service
public class DingTalkServiceApi extends AbstractDingTalkService {


    private static final String ASYNC_SEND_REQUEST_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";
    private static final String USERID_GET_BY_UNIONID_URL = "https://oapi.dingtalk.com/topapi/user/getbyunionid";
    private static final String GET_SEND_RESULT_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult";
    private static final String GET_SEND_PROGRESS_URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendprogress";

    private static final int RETRY_TIMES = 3;
    private static final int RETRY_DELAY = 3 * 1000;
    private static final int API_CALL_AFTER_WAIT_MILE_SECOND = 300;

    private final SocialProperties socialProperties;

    /**
     * 钉钉
     */
    public final static String DING_TALK = "DINGTALK";

    public DingTalkServiceApi(SocialProperties socialProperties) {
        super(socialProperties, DING_TALK);
        this.socialProperties = socialProperties;
    }

    public String getUseridGetByUnionId(String unionId, String source) {
        log.info("开始调用钉钉开发平台[根据unionId获取用户userid接口],参数: {}", unionId);
        if (StrUtil.isEmpty(unionId)) {
            log.error("unionId不能为空");
            throw new ServiceException("unionId不能为空");
        }
        try {
            socialLoginConfigProperties = socialProperties.getType().get(source);
            DingTalkClient client = new DefaultDingTalkClient(USERID_GET_BY_UNIONID_URL);
            OapiUserGetbyunionidRequest req = new OapiUserGetbyunionidRequest();
            req.setUnionid(unionId);
            String accessToken = getAccessToken();
            OapiUserGetbyunionidResponse rsp = client.execute(req, accessToken);
            log.info("调用钉钉开发平台[根据unionId获取用户userid接口]完成,接口返回: {}", rsp.getBody());
            validateApiResult(rsp);
            return rsp.getResult().getUserid();
        } catch (ApiException e) {
            log.error("调用钉钉开发平台[根据unionId获取用户userid接口]失败: {}", e.getMessage());
            throw new ServiceException("调用钉钉开发平台[根据unionId获取用户userid接口]失败");
        }
    }

    /**
     * 发送消息的方法
     *
     * @param toUserIdList 接收消息的用户ID列表，用逗号分隔
     * @param content      消息内容
     * @return taskId
     * @throws ServiceException 如果调用API时发生错误
     */
    public Long sendMessageCallAlipayApi(String toUserIdList, String content, String source) throws ApiException {
        log.info("开始调用钉钉开发平台异步消息通知接口,参数: {} {} ", toUserIdList, content);
        if (StrUtil.isEmpty(toUserIdList) || StrUtil.isEmpty(content)) {
            log.error("接口参数为空,请检查");
            throw new ServiceException("接口参数为空,请检查");
        }
        socialLoginConfigProperties = socialProperties.getType().get(source);
        DingTalkClient client = new DefaultDingTalkClient(ASYNC_SEND_REQUEST_URL);
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setAgentId(Long.valueOf(socialLoginConfigProperties.getAgentId()));
        request.setUseridList(toUserIdList);
        request.setDeptIdList(null);
        request.setToAllUser(false);

        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        msg.setMsgtype("text");
        msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
        msg.getText().setContent(content);
        request.setMsg(msg);

        request.setMsg(msg);
        String accessToken = getAccessToken();
        OapiMessageCorpconversationAsyncsendV2Response rsp = client.execute(request, accessToken);
        log.info("调用钉钉开发平台异步消息通知接口完成,接口返回: {}", rsp.getBody());
        validateApiResult(rsp);
        return rsp.getTaskId();
    }

    /**
     *
     * @param toUserIdList 发送用户
     * @param content 发送内容
     * @param source 钉钉组织
     * @return
     */
    public boolean sendMessage(String toUserIdList, String content, String source) {
        boolean sendResult = false;
        int executeTime = 0;
        while (executeTime < RETRY_TIMES) {
            try {
                Long taskId = sendMessageCallAlipayApi(toUserIdList, content, source);
                TimeUnit.MILLISECONDS.sleep(API_CALL_AFTER_WAIT_MILE_SECOND);
                String failedUserIdsStr = getSendResultCallAlipayApi(taskId, source);
                if (StrUtil.isNotEmpty(failedUserIdsStr)) {
                    log.info("存在发送失败的通知消息,失败用户id:{}, 开始第{}重试", failedUserIdsStr, ++executeTime);
                    toUserIdList = failedUserIdsStr;
                } else {
                    sendResult = true;
                    return sendResult;
                }
            } catch (InterruptedException | ApiException e) {
                log.error("工作消息发送发生异常,异常消息: {}", e.getMessage());
                ++executeTime;
            }
        }
        return sendResult;
    }

    /**
     * 查询工作通知消息的发送结果
     *
     * @param taskId 发送工作消息接口返回的任务Id
     * @param source 多个钉钉组织,指定发送哪一个
     * @return 发送失败的用户id，多个以英文逗号分割
     */
    public String getSendResultCallAlipayApi(Long taskId,String source) throws ApiException {
        log.debug("开始调用获取工作通知消息的发送结果接口,参数: task_id: {} ", taskId);
        if (taskId == null) {
            throw new ServiceException("获取工作通知消息的发送结果接口参数校验失败,taskId不能为空");
        }
        socialLoginConfigProperties = socialProperties.getType().get(source);
        DingTalkClient client = new DefaultDingTalkClient(GET_SEND_RESULT_URL);
        OapiMessageCorpconversationGetsendresultRequest req = new OapiMessageCorpconversationGetsendresultRequest();
        req.setAgentId(Long.valueOf(socialLoginConfigProperties.getAgentId()));
        req.setTaskId(taskId);
        String accessToken = getAccessToken();
        OapiMessageCorpconversationGetsendresultResponse rsp = client.execute(req, accessToken);
        log.info("获取工作通知消息的发送结果接口返回结果: {}", rsp.getBody());
        validateApiResult(rsp);
        if(Objects.nonNull(rsp.getSendResult()) && Objects.nonNull(rsp.getSendResult().getFailedUserIdList()) && !rsp.getSendResult().getFailedUserIdList().isEmpty()) {
            String failedUserIdsStr = String.join(",", rsp.getSendResult().getFailedUserIdList());
            log.warn("发送失败的用户id: {}", failedUserIdsStr);
            return failedUserIdsStr;
        }else {
            return null;
        }
        /**
         * {
         *     "errcode": 0,
         *     "send_result": {
         *         "failed_user_id_list": [],
         *         "forbidden_list": [],
         *         "invalid_dept_id_list": [],
         *         "invalid_user_id_list": [],
         *         "read_user_id_list": [
         *             "manager4220"
         *         ],
         *         "unread_user_id_list": []
         *     },
         *     "request_id": "6pcvwp6jcows"
         * }
         */
    }

    /**
     * 查询工作消息通知发送进度
     *
     * @param taskId 发送工作消息接口返回的任务Id
     */
    public OapiMessageCorpconversationGetsendprogressResponse getSendProgressCallAlipayApi(Long taskId, String source) {
        log.info("开始调用获取工作通知消息的发送进度接口,参数: task_id: {} ", taskId);
        if (taskId == null) {
            throw new ServiceException("获取工作通知消息的发送进度接口参数校验失败,taskId不能为空");
        }
        socialLoginConfigProperties = socialProperties.getType().get(source);
        try {
            DingTalkClient client = new DefaultDingTalkClient(GET_SEND_PROGRESS_URL);
            OapiMessageCorpconversationGetsendprogressRequest req = new OapiMessageCorpconversationGetsendprogressRequest();
            req.setAgentId(Long.valueOf(socialLoginConfigProperties.getAgentId()));
            req.setTaskId(taskId);
            String accessToken = getAccessToken();
            OapiMessageCorpconversationGetsendprogressResponse rsp = client.execute(req, accessToken);
            log.info("获取工作通知消息的发送进度接口返回结果: {}", rsp.getBody());
            validateApiResult(rsp);
            return rsp;
            /**
             * {
             *     "errcode": 0,
             *     "errmsg":"ok",
             *     "progress": {
             *         "progress_in_percent": 100,
             *         "status": 2  (0-未处理 1-处理中 2-处理完毕)
             *     },
             *     "request_id": "5y2znla8tw3d"
             * }
             */
        } catch (ApiException e) {
            log.error("调用获取工作通知消息的发送进度接口失败: {}", e.getMessage());
            throw new ServiceException("获取工作通知消息的发送进度接口失败");
        }
    }
}
