package com.myskybeyond.web.config;

import com.myskybeyond.business.domain.bo.HdAppInfoBo;
import com.myskybeyond.business.domain.vo.HdAppInfoVo;
import com.myskybeyond.business.service.IHdAppInfoService;
import com.myskybeyond.web.enums.SocialEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.social.config.properties.SocialLoginConfigProperties;
import org.dromara.common.social.config.properties.SocialProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 初始化应用管理配置信息
 * @date 2024/6/4
 */
@Component
@Data
@Slf4j
public class SocialPropertiesInit {
    private final SocialProperties socialProperties;
    private final IHdAppInfoService hdAppInfoService;

    public SocialPropertiesInit(SocialProperties socialProperties,
                                IHdAppInfoService hdAppInfoService) {
        this.hdAppInfoService = hdAppInfoService;
        this.socialProperties = socialProperties;
        queryFromDatabase();
        socialProperties.getType().forEach((k,v) -> {
            log.info("应用类型: {} 客户端编号: {} 配置初始化", k, v.getClientId());
        });
    }

    public void queryFromDatabase() {
        HdAppInfoBo hdAppInfo = new HdAppInfoBo();
        hdAppInfo.setStatus(0L);
        List<HdAppInfoVo> hdAppInfoVos = hdAppInfoService.queryList(hdAppInfo);
        Map<String, SocialLoginConfigProperties> type = new HashMap<>(hdAppInfoVos.size());
        for (HdAppInfoVo hdAppInfoVo : hdAppInfoVos) {
            SocialLoginConfigProperties socialLoginConfigProperties = construct(hdAppInfoVo);
            type.put(hdAppInfoVo.getAppCode().toUpperCase(),socialLoginConfigProperties);
        }
        socialProperties.setType(type);
    }

    private SocialLoginConfigProperties construct(HdAppInfoVo hdAppInfoVo) {
        SocialLoginConfigProperties socialLoginConfigProperties = new SocialLoginConfigProperties();
        socialLoginConfigProperties.setAgentId(hdAppInfoVo.getAgentId());
        socialLoginConfigProperties.setClientId(hdAppInfoVo.getAppKey());
        socialLoginConfigProperties.setClientSecret(hdAppInfoVo.getAppSecret());
        String address = socialProperties.getAddress();
        if(SocialEnum.WECHAT_ENTERPRISE.getCode().equals(hdAppInfoVo.getAppType())){
            address = socialProperties.getDomain();
            log.info("企业微信回调地址使用域名: {}", address);
        }
        String redirectUrl = address + "/social-callback" +
            "%3Fsource%3D" +
            hdAppInfoVo.getAppCode().toUpperCase();
        socialLoginConfigProperties.setRedirectUri(redirectUrl + "%26{params}");
        return socialLoginConfigProperties;
    }

    public void updateSocialProperties() {
        queryFromDatabase();
    }
}
