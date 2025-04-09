package com.myskybeyond.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.Date;

/**
 * 项目信息对象 hd_project_info
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("hd_project_info")
public class HdProjectInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目编号
     */
    private String projectNo;

    /**
     * 类型;1：一般项目 2:重要项目 3:关键项目
     */
    private String type;

    /**
     * 项目名称
     */
    private String name;

    /**
     * DC总容量(MWp)
     */
    private String dc;

    /**
     * 客户类型;1：总包 2:开发 3:投资 4:同行 5:其它
     */
    private String customerType;

    /**
     * 采购单位
     */
    private String purchaseUnit;

    /**
     * 业主单位
     */
    private String ownerUnit;

    /**
     * 项目地点
     */
    private String location;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 邮箱
     */
    private String contactEmail;

    /**
     * 其它
     */
    private String other;

    /**
     * 支架类型;1：固定 2: 跟踪 3:车棚 4：固定+跟踪 5:固定+车棚 6:跟踪+车棚 7:固定+跟踪+车棚 8:其它
     */
    private String supportType;

    /**
     * 开始日期
     */
    private Date startTime;

    /**
     * 预计结束日期
     */
    private Date endTime;

    /**
     * 项目负责人
     */
    private Long manager;

    /**
     * 项目内容描述
     */
    private String description;

    /**
     * 状态;0:待执行 1:执行中 2:已完成
     */
    private Long status;

    /**
     * 删除标志;0：存在 2：删除
     */
    @TableLogic
    private String delFlag;

    /**
     * 相关文件信息
     */
    private String relaFileInfo;

}
