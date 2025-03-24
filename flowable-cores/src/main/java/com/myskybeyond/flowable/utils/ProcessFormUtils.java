package com.myskybeyond.flowable.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.myskybeyond.flowable.core.FormConf;
import com.myskybeyond.flowable.core.FormConfOfFormCreate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程表单工具类
 *
 * @author KonBAI
 * @createTime 2022/8/7 17:09
 */
public class ProcessFormUtils {

    private static final String CONFIG = "__config__";
    private static final String MODEL = "__vModel__";
    private static final String GRID_LAYOUT = "FcRow";

    /**
     * 填充表单项内容
     *
     * @param formConf 表单配置信息
     * @param data     表单内容
     */
    public static void fillFormData(FormConf formConf, Map<String, Object> data) {
        for (Map<String, Object> field : formConf.getFields()) {
            recursiveFillField(field, data);
        }
    }

    @SuppressWarnings("unchecked")
    private static void recursiveFillField(final Map<String, Object> field, final Map<String, Object> data) {
        if (!field.containsKey(CONFIG)) {
            return;
        }
        Map<String, Object> configMap = (Map<String, Object>) field.get(CONFIG);
        if (configMap.containsKey("children")) {
            List<Map<String, Object>> childrens = (List<Map<String, Object>>) configMap.get("children");
            for (Map<String, Object> children : childrens) {
                recursiveFillField(children, data);
            }
        }
        String modelKey = Convert.toStr(field.get(MODEL));
        Object value = data.get(modelKey);
        if (value != null) {
            configMap.put("defaultValue", value);
        }
    }

    /**
     * 填充表单项内容
     *
     * @param formConf 表单配置信息
     * @param data     表单内容
     */
    public static void fillFormData(FormConfOfFormCreate formConf, Map<String, Object> data, boolean disabled) {
        for (Map<String, Object> field : formConf.getRule()) {
            recursiveFillField1(field, data, disabled);
        }
    }

    private static void recursiveFillField1(final Map<String, Object> field, final Map<String, Object> data, boolean disabled) {
        // 处理栅格内的表单项
        if (GRID_LAYOUT.equals(field.get("type"))) {
            List<Map<String, Object>> childrens = (List<Map<String, Object>>) field.get("children");
            for (Map<String, Object> children : childrens) {
                List<Map<String, Object>> formAttrs = (List<Map<String, Object>>) children.get("children");
                for (Map<String, Object> formAttr : formAttrs) {
                    recursiveFillField1(formAttr, data, disabled);
                }
            }
        }
        addAttrDisabledAndSetValue(field, data, disabled);
    }

    /**
     * 表单属性设置只读和赋值
     *
     * @param field 扁担项
     * @param data  表单属性值
     */
    private static void addAttrDisabledAndSetValue(Map<String, Object> field, Map<String, Object> data, boolean disabled) {
        Map<String, Object> filedProps = MapUtil.get(field, "props", Map.class);
        if (!ObjectUtil.isNotNull(filedProps)) {
            filedProps = new HashMap<>();
        }
        filedProps.put("disabled", disabled);
        field.put("props", filedProps);
        String modelKey = Convert.toStr(field.get("field"));
        Object value = data.get(modelKey);
        if (value != null) {
            field.put("value", value);
        }
    }
}
