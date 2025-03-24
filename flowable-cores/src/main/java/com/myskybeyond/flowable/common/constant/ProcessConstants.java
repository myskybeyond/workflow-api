package com.myskybeyond.flowable.common.constant;

/**
 * 流程常量信息
 *
 * @author Xuan xuan
 * @date 2021/4/17 22:46
 */
public class ProcessConstants {

    public static final String SUFFIX = ".bpmn";

    /**
     * 动态数据
     */
    public static final String DATA_TYPE = "dynamic";

    /**
     * 单个审批人
     */
    public static final String USER_TYPE_ASSIGNEE = "assignee";


    /**
     * 候选人
     */
    public static final String USER_TYPE_USERS = "candidateUsers";


    /**
     * 审批组
     */
    public static final String USER_TYPE_ROUPS = "candidateGroups";

    /**
     * 单个审批人
     */
    public static final String PROCESS_APPROVAL = "approval";

    /**
     * 会签人员
     */
    public static final String PROCESS_MULTI_INSTANCE_USER = "userList";

    /**
     * nameapace
     */
    public static final String NAMASPASE = "http://flowable.org/bpmn";

    /**
     * 会签节点
     */
    public static final String PROCESS_MULTI_INSTANCE = "multiInstance";

    /**
     * 自定义属性 dataType
     */
    public static final String PROCESS_CUSTOM_DATA_TYPE = "dataType";

    /**
     * 自定义属性 userType
     */
    public static final String PROCESS_CUSTOM_USER_TYPE = "userType";

    /**
     * 自定义属性 userType
     */
    public static final String PROCESS_CUSTOM_USER_TYPE_ROLES = "ROLES";

    /**
     * 自定义属性 userType
     */
    public static final String PROCESS_CUSTOM_USER_TYPE_USERS = "USERS";


    /**
     * 自定义属性 userType
     */
    public static final String PROCESS_CUSTOM_USER_TYPE_DEPTS = "DEPTS";

    /**
     * 自定义属性 userType
     */
    public static final String PROCESS_CUSTOM_USER_TYPE_INITIATOR = "INITIATOR";

    /**
     * 自定义属性 userType
     */
    public static final String PROCESS_CUSTOM_USER_TYPE_POSTS = "POSTS";



    /**
     * 自定义属性 localScope
     */
    public static final String PROCESS_FORM_LOCAL_SCOPE = "localScope";

    /**
     * 自定义属性 流程状态
     */
    public static final String PROCESS_STATUS_KEY = "processStatus";
    /**
     * 是否人工干预流程
     */
    public static final String PROCESS_STATUS_HANDLE_TO_END = "processStatusHandleToEnd";


    /**
     * 流程跳过
     */
    public static final String FLOWABLE_SKIP_EXPRESSION_ENABLED = "_FLOWABLE_SKIP_EXPRESSION_ENABLED";

    /**
     * 节点通知消息模板id
     */
    public static final String PROCESS_CUSTOM_COMPLETE_NOTICE_IDS = "completeNoticeIds";
    /**
     * 节点抄送得通知消息模板id
     */
    public static final String PROCESS_CUSTOM_COPY_NOTICE_IDS = "copyNoticeIds";
    /**
     * 节点接收得通知消息模板id
     */
    public static final String PROCESS_CUSTOM_RECEIVE_NOTICE_IDS = "receiveNoticeIds";
    /**
     * 通知消息用户id
     */
    public static final String PROCESS_CUSTOM_COMPLETE_CANDIDATE_USERS = "completeCandidateUsers";
    /**
     * 通知消息用户名称
     */
    public static final String PROCESS_CUSTOM_COMPLETE_TEXT = "completeText";
    /**
     * 通知消息接收人类型（多个以&分隔）
     */
    public static final String PROCESS_CUSTOM_COMPLETE_CANDIDATE_USERS_TYPES = "completeCandidateUsersTypes";


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
     * 发货清单
     */
    public static final String ONLINE_DELIVER_ID_KEY = "deliver";

    /**
     * 材料清单
     */
    public static final String ONLINE_BOM_ID_KEY = "bom";

    /**
     * 下单进度跟踪key
     */
    public static final String BUSINESS_FLOW_ORDER_KEY = "business.flowable.order.relation";


    /**
     * 报价进度跟踪key
     */
    public static final String BUSINESS_FLOW_QUOTATION_KEY = "business.flowable.quotation.relation";
    /**
     * 预计完成日期
     */
    public static final String BUSINESS_FLOW_EXPECTED_DATE_KEY = "expectedDate";

    /**
     * 业务flow
     */
    public static final String BUSINESS_FLOW_QUOTATION_STATISTIC_KEY ="business.flowable.quotation.statistic.node";

    /**
     * 抄送key
     */
    public static final String BUSINESS_FLOW_REDIS_CC_KEY="business.flowable.redis.cc:";

    /**
     * 抄送key 缓存时间
     */
    public static final Long BUSINESS_FLOW_REDIS_CC_DURATION= 180L;
}
