package com.myskybeyond.business.domain.vo;

//import org.dromara.system.domain.HdSmsInfo;
//import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
//import com.alibaba.excel.annotation.ExcelProperty;
//import org.dromara.common.excel.annotation.ExcelDictFormat;
//import org.dromara.common.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.myskybeyond.business.domain.HdSmsInfo;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 消息历史记录信息视图对象 hd_sms_info
 *
 * @author Lion Li
 * @date 2024-04-30
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = HdSmsInfo.class)
public class HdSmsInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 消息模板ID
     */
    @ExcelProperty(value = "消息模板ID")
    private Long smsTemplateId;

    /**
     * 模板名称
     */
    @ExcelProperty(value = "模板名称")
    private String name;

    /**
     * 模板编码
     */
    @ExcelProperty(value = "模板编码")
    private String code;

    /**
     * 项目ID
     */
    @ExcelProperty(value = "项目ID")
    private Long projectId;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectName;

    /**
     * 销售订单ID
     */
    @ExcelProperty(value = "销售订单ID")
    private Long orderId;

    /**
     * 销售订单名称
     */
    @ExcelProperty(value = "销售订单名称")
    private String orderName;

    /**
     * 流程定义ID;关联 act_re_procdef 主键
     */
    @ExcelProperty(value = "流程定义ID;关联 act_re_procdef 主键")
    private String procId;

    /**
     * 流程定义名称
     */
    @ExcelProperty(value = "流程定义名称")
    private String procName;

    /**
     * 来源
     */
    @ExcelProperty(value = "来源")
    private String source;

    /**
     * 来源用户ID
     */
    @ExcelProperty(value = "来源用户ID")
    private Long sourceUserId;

    /**
     * 用户ID
     */
    @ExcelProperty(value = "用户ID")
    private Long userId;

    /**
     * 消息内容
     */
    @ExcelProperty(value = "消息内容")
    private String content;

    /**
     * 是否已读;0:未读 1:已读
     */
    @ExcelProperty(value = "是否已读;0:未读 1:已读")
    private Long isRead;

    /**
     * 参数
     */
    @ExcelProperty(value = "参数")
    private String smsParams;

    /**
     * 模板内容
     */
    @ExcelProperty(value = "模板内容")
    private String templateContent;

    private Date createTime;

    private String procNodeName;


}
