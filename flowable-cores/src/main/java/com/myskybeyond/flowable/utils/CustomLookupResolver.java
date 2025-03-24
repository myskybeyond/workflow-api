package com.myskybeyond.flowable.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrLookup;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author MySkyBeyond
 * @version 1.0
 * 自定义Commons lang3中字符串替换规则
 * @date 2024/6/25
 */
@Slf4j
public class CustomLookupResolver extends StrLookup<Object> {

    private final Map<String, Object> valuesMap;

    public CustomLookupResolver(Map<String, Object> valuesMap) {
        this.valuesMap = valuesMap;
    }
    @Override
    public String lookup(String key) {
        String[] keys = key.split("\\.");
        Object value = valuesMap;
        for (String k : keys) {
            if (value instanceof Map) {
                value = ((Map<?, ?>) value).get(k);
            } else {
                try {
                    Field field = value.getClass().getDeclaredField(k);
                    field.setAccessible(true);
                    value = field.get(value);
                } catch (Exception e) {
                    log.error("获取属性: {} 发生异常", k);
                }
            }
            if (value == null) {
                return null;
            }
        }
        return value.toString();
    }
}
