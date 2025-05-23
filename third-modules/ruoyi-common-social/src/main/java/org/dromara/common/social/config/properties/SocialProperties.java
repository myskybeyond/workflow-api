package org.dromara.common.social.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Social 配置属性
 *
 * @author thiszhc
 */
@Data
@Component
@ConfigurationProperties(prefix = "justauth")
public class SocialProperties {

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 授权类型
     */
    private Map<String, SocialLoginConfigProperties> type;
    /**
     * 前端外网访问地址
     */
    private String address;
    /**
     * 回调域名
     */
    private String domain;

}
