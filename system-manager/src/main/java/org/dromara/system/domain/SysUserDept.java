package org.dromara.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户和部门关联 sys_user_dept
 *
 * @author Lion Li
 */

@Data
@TableName("sys_user_dept")
public class SysUserDept {

    /**
     * 用户ID
     */
    @TableId(type = IdType.INPUT)
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

}
