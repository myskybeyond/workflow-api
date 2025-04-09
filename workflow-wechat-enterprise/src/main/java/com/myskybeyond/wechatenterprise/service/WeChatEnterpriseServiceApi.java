package com.myskybeyond.wechatenterprise.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.social.config.properties.SocialProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.myskybeyond.wechatenterprise.constant.Constants.WECHAT_ENTERPRISE;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 企业微信工作通知消息相关api封装
 * @date 2025/1/17
 */
@Slf4j
@Service
public class WeChatEnterpriseServiceApi extends AbstractWechatEnterpriseService{
    private static final String ASYNC_SEND_REQUEST_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send";
    private static final String GET_CALLBACK_IP_URL = " https://qyapi.weixin.qq.com/cgi-bin/getcallbackip";
    //设置应用在用户工作台展示的数据
    private static final String SET_DASHBOARD_DATA_URL = " https://qyapi.weixin.qq.com/cgi-bin/agent/set_workbench_data";

    private static final int RETRY_TIMES = 3;
    private static final int RETRY_DELAY = 3 * 1000;
    private static final int API_CALL_AFTER_WAIT_MILE_SECOND = 300;



    private final SocialProperties socialProperties;

    public WeChatEnterpriseServiceApi(SocialProperties socialProperties) {
        super(socialProperties, WECHAT_ENTERPRISE);
        this.socialProperties = socialProperties;
    }

    /**
     * 发送消息的方法
     *
     * @param toUserIdList 接收消息的用户ID列表，用逗号分隔
     * @param content      消息内容
     * @return taskId
     * @throws ServiceException 如果调用API时发生错误
     */
    public String sendMessageCallWechatApi(String toUserIdList, String content, String source) throws HttpException {
        log.info("开始调用企业微信开发平台消息通知接口,参数: {} {} ", toUserIdList, content);
        if (StrUtil.isEmpty(toUserIdList) || StrUtil.isEmpty(content)) {
            log.error("接口参数为空,请检查");
            throw new ServiceException("接口参数为空,请检查");
        }
        socialLoginConfigProperties = socialProperties.getType().get(source);
        String accessToken = getAccessToken();
        String url = ASYNC_SEND_REQUEST_URL + "?access_token=" + accessToken;
        JSONObject params = getParams(toUserIdList, content);
        log.info("params: {}", params);
        String response = HttpUtil.post(url, params.toString());
        log.debug("企业微信发送消息请求的结果: {}", response);
        JSONObject json = JSONUtil.parseObj(response);
        log.info("调用企业微信开发平台消息通知接口完成,接口返回: {}", json);
        validateApiResult(ASYNC_SEND_REQUEST_URL, json.getStr("errcode"));
        // 不合法的userid，接口返回的统一为小写
        String invaliduser = json.getStr("invaliduser");
        if(StrUtil.isNotEmpty(invaliduser)){
            log.error("发送消息接口返回的不合法的userid： {}",invaliduser);
        }
        return invaliduser;
    }

    private JSONObject getParams(String toUserIdList, String content) {
        JSONObject params = new JSONObject();
        //多个接收者用‘|’分隔，最多支持1000个
        params.put("touser", toUserIdList.replaceAll(",","|"));
        params.put("msgtype","text");
        params.put("agentid", socialLoginConfigProperties.getAgentId());
        JSONObject text = new JSONObject();
        text.put("content", content);
        params.put("text", text);
        //表示是否是保密消息，0表示可对外分享，1表示不能分享且内容显示水印，默认为0
        params.put("safe", 0);
        //表示是否开启id转译，0表示否，1表示是，默认0。
        params.put("enable_id_trans", 0);
        //表示是否开启重复消息检查，0表示否，1表示是，默认0
        params.put("enable_duplicate_check", 0);
        return params;
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
                String invaliduser = sendMessageCallWechatApi(toUserIdList, content, source);
                TimeUnit.MILLISECONDS.sleep(API_CALL_AFTER_WAIT_MILE_SECOND);
                if (StrUtil.isNotEmpty(invaliduser)) {
                    log.info("存在发送失败的通知消息,不合法用户id:{}, 开始第{}重试", invaliduser, ++executeTime);
                    //因企业微信返回用户id为小写，需要转换
                    String[] invalidUsers = invaliduser.split("|");
                    String[] toUsers = toUserIdList.split(",");
                    for (String invalidUser : invalidUsers) {
                        for (String toUser : toUsers) {
                            if (invalidUser.equals(toUser.toLowerCase())) {
                                invalidUser = toUser;
                            }
                        }
                    }
                    toUserIdList = String.join("|",invalidUsers);
                    log.info("不合法用户id(企业微信统一返回为小写):{} 转换为用户id: {}", invaliduser,toUserIdList);
                } else {
                    sendResult = true;
                    return sendResult;
                }
            } catch (InterruptedException e) {
                log.error("工作消息发送发生异常,异常消息: {}", e.getMessage());
                ++executeTime;
            }
        }
        return sendResult;
    }

    /**
     * 获取企业微信回调IP段
     * @param source
     * @return
     */
    public List<String> getCallbackIp(String source) {
        log.info("开始调用获取企业微信回调IP段接口");
        socialLoginConfigProperties = socialProperties.getType().get(source);
        String accessToken = getAccessToken();
        String url = GET_CALLBACK_IP_URL + "?access_token=" + accessToken;
        String response = HttpUtil.get(url);
        log.info("获取企业微信回调IP段接口的结果: {}", response);
        JSONObject json = JSONUtil.parseObj(response);
        validateApiResult(GET_CALLBACK_IP_URL, json.getStr("errcode"));
        return json.getJSONArray("ip_list").toList(String.class);
    }

    /**
     * 设置应用在用户工作台展示的数据
     * @param source
     * @param userId 企业微信用户id
     * @param data 业务数据
     */
    public void setDashboardDate(String source, String userId, Map<String,Object> data){
        if(StrUtil.isEmpty(userId) || data == null){
            log.error("设置应用在用户工作台展示的数据接口userid参数为空,请检查");
            throw new ServiceException("接口参数为空,请检查");
        }
        socialLoginConfigProperties = socialProperties.getType().get(source);
        data.put("agentid",socialLoginConfigProperties.getAgentId());
        data.put("userId",userId);
        String accessToken = getAccessToken();
        String url = SET_DASHBOARD_DATA_URL + "?access_token=" + accessToken;

        String paramStr = "{" +
            "    \"agentid\":{agentid}," +
            "    \"userid\":\"{userId}\"," +
            "    \"type\":\"keydata\"," +
            "    \"keydata\":{" +
            "      \"items\":[" +
            "        {" +
            "            \"key\":\"待办\"," +
            "            \"data\":\"{todoTotal}\"," +
            "            \"jump_url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=ww927b6a6dadb1457f&redirect_uri=http%3A%2F%2Fsales-order.hdsolartech.cn%3A14720%2Fcallback%3Fsource%3DWECHAT_ENTERPRISE-HANGXIN%26jump_url%3Dwork%2Ftodo&response_type=code&scope=snsapi_base&state=STATE&agentid=1000004#wechat_redirect\"" +
            "        }," +
            "        {" +
            "            \"key\":\"我的抄送\"," +
            "            \"data\":\"{copyTotal}\"," +
            "            \"jump_url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=ww927b6a6dadb1457f&redirect_uri=http%3A%2F%2Fsales-order.hdsolartech.cn%3A14720%2Fcallback%3Fsource%3DWECHAT_ENTERPRISE-HANGXIN%26jump_url%3Dwork%2Fcopy&response_type=code&scope=snsapi_base&state=STATE&agentid=1000004#wechat_redirect\"" +
            "        }," +
            "        {" +
            "            \"key\":\"我的申请\"," +
            "            \"data\":\"{ownTotal}\"," +
            "            \"jump_url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=ww927b6a6dadb1457f&redirect_uri=http%3A%2F%2Fsales-order.hdsolartech.cn%3A14720%2Fcallback%3Fsource%3DWECHAT_ENTERPRISE-HANGXIN%26jump_url%3Dwork%2Fown&response_type=code&scope=snsapi_base&state=STATE&agentid=1000004#wechat_redirect\"" +
            "        }," +
            "        {" +
            "            \"key\":\"我的办理\"," +
            "            \"data\":\"{finishedTotal}\"," +
            "            \"jump_url\":\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=ww927b6a6dadb1457f&redirect_uri=http%3A%2F%2Fsales-order.hdsolartech.cn%3A14720%2Fcallback%3Fsource%3DWECHAT_ENTERPRISE-HANGXIN%26jump_url%3Dwork%2Ffinished&response_type=code&scope=snsapi_base&state=STATE&agentid=1000004#wechat_redirect\"" +
            "        }" +
            "    ]" +
            "    }," +
            "    \"replace_user_data\":true" +
            "}";
        paramStr = StrUtil.format(paramStr, data);
        log.info("设置应用在用户工作台展示的数据的请求参数: {}", paramStr);
        String response = HttpUtil.post(url, paramStr);
        log.info("设置应用在用户工作台展示的数据接口的结果: {}", response);
        JSONObject json = JSONUtil.parseObj(response);
        validateApiResult(SET_DASHBOARD_DATA_URL, json.getStr("errcode"));
    }
}
