package com.myskybeyond.wechatenterprise.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.GlobalConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.redis.utils.RedisUtils;
import org.dromara.common.social.config.properties.SocialLoginConfigProperties;
import org.dromara.common.social.config.properties.SocialProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 封装企业微信接口抽象类
 * @date 2025/1/17
 */
@Slf4j
public class AbstractWechatEnterpriseService {
    public static final int EXPIRE_TIME_SECONDS = 7200;
    public static final String ACCESS_TOKEN_REQUEST_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={corpid}&corpsecret={corpsecret}";
    public static final String WECHAT_ENTERPRISE_API_SUCCESS_CODE = "0";
    public SocialLoginConfigProperties socialLoginConfigProperties;

    protected AbstractWechatEnterpriseService(SocialProperties socialProperties, String source) {
        this.socialLoginConfigProperties = socialProperties.getType().get(source);
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
            Map<String, Object> params = new HashMap<>();
            params.put("corpid", socialLoginConfigProperties.getClientId());
            params.put("corpsecret", socialLoginConfigProperties.getClientSecret());
            String url = StrUtil.format(ACCESS_TOKEN_REQUEST_URL, params);
            String response = HttpUtil.get(url);
            log.info("获取accessToken请求的结果: {}", response);
            JSONObject json = JSONUtil.parseObj(response);
            validateApiResult(ACCESS_TOKEN_REQUEST_URL, json.getStr("errcode"));
            String accessToken = json.getStr("access_token");
            log.debug("应用clientId: {} agentId: {} accessToken: {}", socialLoginConfigProperties.getClientId(), socialLoginConfigProperties.getAgentId(), accessToken);
            cacheAccessToken(accessToken);
            return accessToken;
        } catch (HttpException | ServiceException ex) {
            log.error("获取accessToken失败: {}", ex.getMessage());
            throw new ServiceException("企业微信开发平台获取accessToken失败");
        }
    }

    private String constructAccessTokenKey(String clientId) {
        return StrUtil.format(GlobalConstants.GLOBAL_REDIS_KEY + "{}_qywx_access_token", clientId);
    }

    public void validateApiResult(String url, String errCode) throws ServiceException {
        if (!WECHAT_ENTERPRISE_API_SUCCESS_CODE.equals(errCode)) {
            log.error("企业微信接口: {} 调用失败，错误码：{}", url, errCode);
            throw new ServiceException("企业微信接口调用失败");
        }
    }

    private void cacheAccessToken(String accessToken) {
        String accessTokenKey = constructAccessTokenKey(socialLoginConfigProperties.getClientId());
        log.info("缓存企业微信开发平台的access_token, key: {} value: {} 过期时间(s): {}", accessTokenKey, accessToken, EXPIRE_TIME_SECONDS);
        RedisUtils.setCacheObject(accessTokenKey, accessToken, Duration.ofSeconds(EXPIRE_TIME_SECONDS));
    }

}
