package org.dromara.system.utils;

import cn.hutool.core.util.StrUtil;
import org.dromara.common.redis.utils.RedisUtils;

import static org.dromara.common.core.constant.UserConstants.KEY_FORCE_RESET_PASS;

/**
 * @author MySkyBeyond
 * @version 1.0
 * Brief description of the class.
 * @date 2024/7/2
 */
public class NewUserUtils {


    /**
     *  设置强制修改密码标记
     * @param userId 用户主键
     * @param value true|false
     */
    public static void setForceResetPassValue(Long userId, boolean value) {
        String redisKey = StrUtil.format(KEY_FORCE_RESET_PASS, userId);
        if(value) {
            RedisUtils.setCacheObject(redisKey, true);
        }else {
            RedisUtils.deleteObject(redisKey);
        }
    }

    public static Boolean getForceResetPassValue(Long userId) {
        String redisKey = StrUtil.format(KEY_FORCE_RESET_PASS, userId);
        return RedisUtils.getCacheObject(redisKey);
    }
}
