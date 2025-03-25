package org.dromara.common.core.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/7/12
 */
@Data
@AllArgsConstructor
public class UserSocial {
    private String source;
    private String userId;
    private String socialUserId;
    private String nickName;

}
