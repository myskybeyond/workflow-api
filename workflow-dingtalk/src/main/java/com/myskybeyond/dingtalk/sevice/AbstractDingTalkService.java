package com.myskybeyond.dingtalk.sevice;

import cn.hutool.core.util.StrUtil;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoResponse;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.social.config.properties.SocialLoginConfigProperties;
import org.dromara.common.social.config.properties.SocialProperties;

import java.time.Duration;

/**
 * @author MySkyBeyond
 */
@Slf4j
public abstract class AbstractDingTalkService {

    public static final int EXPIRE_TIME_SECONDS = 7200;

    public static final String ACCESS_TOKEN_REQUEST_URL = "https://oapi.dingtalk.com/gettoken";

    public static final String DING_TALK_API_SUCCESS_CODE = "0";

    public SocialLoginConfigProperties socialLoginConfigProperties;

    protected AbstractDingTalkService(SocialProperties socialProperties,String source) {
        this.socialLoginConfigProperties = socialProperties.getType().get(source);
    }

    public void validateApiResult(TaobaoResponse response) throws ApiException {
        if(!DING_TALK_API_SUCCESS_CODE.equals(response.getErrorCode())) {
            log.error("钉钉接口: {} 调用失败，错误码：{}", response.getRequestUrl(), response.getErrorCode());
            throw new ApiException("钉钉接口调用失败");
        }
    }

    /**
     * 获取accessToken
     *
     * @return accessToken
     */
    public String getAccessToken() {
        try {
            String accessTokenKey = constructAccessTokenKey(socialLoginConfigProperties.getClientId());
            if (RedisUtils.hasKey(accessTokenKey)) {
                return RedisUtils.getCacheObject(accessTokenKey);
            }
            DingTalkClient client = new DefaultDingTalkClient(ACCESS_TOKEN_REQUEST_URL);
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(socialLoginConfigProperties.getClientId());
            request.setAppsecret(socialLoginConfigProperties.getClientSecret());
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            validateApiResult(response);
            log.debug("应用clientId: {} agentId: {} accessToken: {}", socialLoginConfigProperties.getClientId(), socialLoginConfigProperties.getAgentId(), response.getAccessToken());
            cacheAccessToken(response.getAccessToken());
            return response.getAccessToken();
        } catch (ApiException ex) {
            log.error("获取accessToken失败: {}", ex.getMessage());
            throw new ServiceException("钉钉开发平台获取accessToken失败");
        }
    }

    private void cacheAccessToken(String accessToken) {
        String accessTokenKey = constructAccessTokenKey(socialLoginConfigProperties.getClientId());
        log.info("缓存钉钉开发平台的access_token, key: {} value: {} 过期时间(s): {}", accessTokenKey, accessToken, EXPIRE_TIME_SECONDS);
        RedisUtils.setCacheObject(accessTokenKey, accessToken, Duration.ofSeconds(EXPIRE_TIME_SECONDS));
    }

    private String constructAccessTokenKey (String clientId) {
        return StrUtil.format(GlobalConstants.GLOBAL_REDIS_KEY + "{}_dingtalk_access_token",clientId);
    }
}
