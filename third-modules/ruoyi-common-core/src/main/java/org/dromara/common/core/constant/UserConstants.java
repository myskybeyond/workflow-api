package org.dromara.common.core.constant;

/**
 * 用户常量信息
 *
 * @author ruoyi
 */
public interface UserConstants {

    /**
     * 平台内系统用户的唯一标志
     */
    String SYS_USER = "SYS_USER";

    /**
     * 正常状态
     */
    String NORMAL = "0";

    /**
     * 异常状态
     */
    String EXCEPTION = "1";

    /**
     * 用户正常状态
     */
    String USER_NORMAL = "0";

    /**
     * 用户封禁状态
     */
    String USER_DISABLE = "1";

    /**
     * 角色正常状态
     */
    String ROLE_NORMAL = "0";

    /**
     * 角色封禁状态
     */
    String ROLE_DISABLE = "1";

    /**
     * 部门正常状态
     */
    String DEPT_NORMAL = "0";

    /**
     * 部门停用状态
     */
    String DEPT_DISABLE = "1";

    /**
     * 岗位正常状态
     */
    String POST_NORMAL = "0";

    /**
     * 岗位停用状态
     */
    String POST_DISABLE = "1";

    /**
     * 字典正常状态
     */
    String DICT_NORMAL = "0";

    /**
     * 是否为系统默认（是）
     */
    String YES = "Y";

    /**
     * 是否菜单外链（是）
     */
    String YES_FRAME = "0";

    /**
     * 是否菜单外链（否）
     */
    String NO_FRAME = "1";

    /**
     * 菜单正常状态
     */
    String MENU_NORMAL = "0";

    /**
     * 菜单停用状态
     */
    String MENU_DISABLE = "1";

    /**
     * 菜单类型（目录）
     */
    String TYPE_DIR = "M";

    /**
     * 菜单类型（菜单）
     */
    String TYPE_MENU = "C";

    /**
     * 菜单类型（按钮）
     */
    String TYPE_BUTTON = "F";

    /**
     * Layout组件标识
     */
    String LAYOUT = "Layout";

    /**
     * ParentView组件标识
     */
    String PARENT_VIEW = "ParentView";

    /**
     * InnerLink组件标识
     */
    String INNER_LINK = "InnerLink";

    /**
     * 用户名长度限制
     */
    int USERNAME_MIN_LENGTH = 2;
    int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    int PASSWORD_MIN_LENGTH = 5;
    int PASSWORD_MAX_LENGTH = 20;

    /**
     * 超级管理员ID
     */
    Long SUPER_ADMIN_ID = 1L;

    /**
     * 占位符常量
     */
    String  FOO = "foo";

    /**
     * 配置字段信息
     */
    String CONFIG_COMPONENT_INS_COLUMNS ="business.component.ins.columns";


    /**
     * 项目日志
     */
    Long PROJECT_OPER_LOG_TYPE_1 = 1L;

    /**
     * 销售订单日志
     */
    Long PROJECT_OPER_LOG_TYPE_2 = 2L;


    /**
     * 项目操作类型
     * 项目-创建操作-01
     */
    String PROJECT_OPERATE_TYPE_01 ="01";

    /**
     * 项目操作类型
     * 项目-编辑操作-02
     */
    String PROJECT_OPERATE_TYPE_02 ="02";
    /**
     * 项目操作类型
     * 项目-状态变更操作-03
     */
    String PROJECT_OPERATE_TYPE_03 ="03";

    /**
     * 项目操作类型
     * 项目-文件下载操作-04
     */
    String PROJECT_OPERATE_TYPE_04 ="04";

    /**
     * 项目操作类型
     * 订单-创建-11
     */
    String PROJECT_OPERATE_TYPE_11 ="11";

    /**
     * 项目操作类型
     * 订单-编辑-12
     */
    String PROJECT_OPERATE_TYPE_12 ="12";
    /**
     * 项目操作类型
     * 订单-状态变更-03
     */
    String PROJECT_OPERATE_TYPE_13 ="13";

    /**
     * 项目操作类型
     * 订单-进度更新-14
     */
    String PROJECT_OPERATE_TYPE_14 ="14";

    /**
     * 项目操作类型
     * 订单-文件下载-15
     */
    String PROJECT_OPERATE_TYPE_15 ="15";

    /**
     * 项目操作结果 成功
     */
    Long   PROJECT_OPERATE_RESULT_SUCCESS = 0L;
    /**
     * 项目操作结果 失败
     */
    Long   PROJECT_OPERATE_RESULT_FAIL = 1L;

    /**
     * 文件是否在线 不在线
     */
    Long PROJECT_FILE_ONLINE_0 = 0L;

    /**
     * 文件是否在线 在线
     */
    Long PROJECT_FILE_ONLINE_1 = 1L;


    /**
     * 配置项目进度流程KEY
     */
    String CONFIG_FLOWABLE_PROJECT_PROGRESS="business.flowable.project.progress";

    /**
     * 配置项目发货进度流程KEY
     */
    String CONFIG_FLOWABLE_DELIVERY_PROGRESS="business.flowable.delivery.progress";



    /**
     * 项目id
     */
    public static final String PROJECT_ID_KEY = "projectId";

    /**
     * 订单id
     */
    public static final String ORDER_ID_KEY = "orderId";

    /**
     * 普通文件ids
     */
    public static final String FILES_ID_KEY = "busiFileIds";

    /**
     * 普通图片s
     */
    public static final String IMAGES_ID_KEY = "busiImageIds";

    /**
     * 报价在线文件id
     */
    public static final String ONLINE_QUOTATION_FILE_ID_KEY = "quotation";

    /**
     * 下单在线文件id
     */
    public static final String ONLINE_TECH_ORDER_ID_KEY = "techOrder";

    /**
     * 跟踪细化文件id
     */
    public static final String ONLINE_REFINE_ID_KEY = "refine";

    /**
     * 通用在线文件id
     */
    public static final String ONLINE_COMMON_FILE_ID_KEY = "common";

    /**
     * 订单id
     */
    public static final String REMARK_KEY = "remark";

    /**
     *同步状态 已同步
     */
    public static final Long  SYNC_STATUS_1 =1L;

    /**
     * 项目状态 -已完成
     */
    public static final Long PROJECT_STATUS_COMPLETED = 2L;

    /**
     * 订单状态 -已完成
     */
    public static final Long ORDER_STATUS_COMPLETED = 2L;
    /**
     * 订单状态 -已作废
     */
    public static final Long ORDER_STATUS_CANCEL = 3L;

    /**
     * 是否强制修改密码
     */
    String KEY_FORCE_RESET_PASS = "{}_force_reset_pass";

    /**
     * 在线文件分类前缀
     */
    public static final String FILE_CATEGORY_PREFIX="HDCA";
    /**
     * 项目 订单权限
     */
    public static final String PROJECT_ORDER_DATA_ROLE_KEY="PO";

}
