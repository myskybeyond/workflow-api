package org.dromara.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.CacheNames;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.service.UserService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StreamUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.helper.DataBaseHelper;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.system.domain.*;
import org.dromara.system.domain.bo.SysUserBo;
import org.dromara.system.domain.vo.SysDeptVo;
import org.dromara.system.domain.vo.SysPostVo;
import org.dromara.system.domain.vo.SysRoleVo;
import org.dromara.system.domain.vo.SysUserVo;
import org.dromara.system.mapper.*;
import org.dromara.system.service.ISysUserService;
import org.dromara.system.utils.NewUserUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户 业务层处理
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserServiceImpl implements ISysUserService, UserService {

    private final SysUserMapper baseMapper;
    private final SysDeptMapper deptMapper;
    private final SysRoleMapper roleMapper;
    private final SysPostMapper postMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysUserPostMapper userPostMapper;
    private final SysUserDeptMapper userDeptMapper;

    @Override
    public TableDataInfo<SysUserVo> selectPageUserList(SysUserBo user, PageQuery pageQuery) {
        Page<SysUserVo> page = baseMapper.selectPageUserList(pageQuery.build(), this.buildQueryWrapper(user));
        for (SysUserVo record : page.getRecords()) {
            List<String> deptNames = deptMapper.selectDeptNameListByUserId(record.getUserId());
            record.setDeptName(String.join(", ", deptNames));
        }
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUserVo> selectUserList(SysUserBo user) {
        List<SysUserVo> sysUserVos = baseMapper.selectUserList(this.buildQueryWrapper(user));
        for (SysUserVo record : sysUserVos) {
            List<String> deptNames = deptMapper.selectDeptNameListByUserId(record.getUserId());
            record.setDeptName(String.join(", ", deptNames));
        }
        return sysUserVos;
    }

    private Wrapper<SysUser> buildQueryWrapper(SysUserBo user) {
        Map<String, Object> params = user.getParams();
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
            .eq(ObjectUtil.isNotNull(user.getUserId()), "u.user_id", user.getUserId())
            .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
            .eq(StringUtils.isNotBlank(user.getStatus()), "u.status", user.getStatus())
            .like(StringUtils.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                "u.create_time", params.get("beginTime"), params.get("endTime"))
            .and(ObjectUtil.isNotNull(user.getDeptId()), w -> {
                List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
                    .select(SysDept::getDeptId)
                    .apply(DataBaseHelper.findInSet(user.getDeptId(), "ancestors")));
                List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
                ids.add(user.getDeptId());
                w.in("sud.dept_id", ids);
            }).orderByAsc("u.user_id");
        return wrapper;
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public TableDataInfo<SysUserVo> selectAllocatedList(SysUserBo user, PageQuery pageQuery) {
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
            .eq(ObjectUtil.isNotNull(user.getRoleId()), "r.role_id", user.getRoleId())
            .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
            .eq(StringUtils.isNotBlank(user.getStatus()), "u.status", user.getStatus())
            .like(StringUtils.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber())
            .orderByAsc("u.user_id");
        Page<SysUserVo> page = baseMapper.selectAllocatedList(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public TableDataInfo<SysUserVo> selectUnallocatedList(SysUserBo user, PageQuery pageQuery) {
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(user.getRoleId());
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
            .and(w -> w.ne("r.role_id", user.getRoleId()).or().isNull("r.role_id"))
            .notIn(CollUtil.isNotEmpty(userIds), "u.user_id", userIds)
            .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
            .like(StringUtils.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber())
            .orderByAsc("u.user_id");
        Page<SysUserVo> page = baseMapper.selectUnallocatedList(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUserVo selectUserByUserName(String userName) {
        return baseMapper.selectUserByUserName(userName);
    }

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    @Override
    public SysUserVo selectUserByPhonenumber(String phonenumber) {
        return baseMapper.selectUserByPhonenumber(phonenumber);
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUserVo selectUserById(Long userId) {
        return baseMapper.selectUserById(userId);
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRoleVo> list = roleMapper.selectRolesByUserName(userName);
        if (CollUtil.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return StreamUtils.join(list, SysRoleVo::getRoleName);
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPostVo> list = postMapper.selectPostsByUserName(userName);
        if (CollUtil.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return StreamUtils.join(list, SysPostVo::getPostName);
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean checkUserNameUnique(SysUserBo user) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getUserName, user.getUserName())
            .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        return !exist;
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     */
    @Override
    public boolean checkPhoneUnique(SysUserBo user) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getPhonenumber, user.getPhonenumber())
            .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        return !exist;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     */
    @Override
    public boolean checkEmailUnique(SysUserBo user) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getEmail, user.getEmail())
            .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        return !exist;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param userId 用户ID
     */
    @Override
    public void checkUserAllowed(Long userId) {
        if (ObjectUtil.isNotNull(userId) && LoginHelper.isSuperAdmin(userId)) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    @Override
    public void checkUserDataScope(Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return;
        }
        if (LoginHelper.isSuperAdmin()) {
            return;
        }
        if (ObjectUtil.isNull(baseMapper.selectUserById(userId))) {
            throw new ServiceException("没有权限访问用户数据！");
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUserBo user) {
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        // 新增用户信息
        int rows = baseMapper.insert(sysUser);
        user.setUserId(sysUser.getUserId());
        // 新增用户岗位关联
        insertUserPost(user, false);
        // 新增用户与角色管理
        insertUserRole(user, false);
        // 新增用户与部门管理
        insertUserDept(user, false);
        // 添加强制修改密码标记
        NewUserUtils.setForceResetPassValue(sysUser.getUserId(), true);
        return rows;
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public boolean registerUser(SysUserBo user, String tenantId) {
        user.setCreateBy(user.getUserId());
        user.setUpdateBy(user.getUserId());
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        sysUser.setTenantId(tenantId);
        return baseMapper.insert(sysUser) > 0;
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUser(SysUserBo user) {
        // 新增用户与角色管理
        insertUserRole(user, true);
        // 新增用户与岗位管理
        insertUserPost(user, true);
        // 新增用户与部门管理
        insertUserDept(user, true);
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        // 防止错误更新后导致的数据误删除
        int flag = baseMapper.updateById(sysUser);
        if (flag < 1) {
            throw new ServiceException("修改用户" + user.getUserName() + "信息失败");
        }
        return flag;
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(Long userId, Long[] roleIds) {
        insertUserRole(userId, roleIds, true);
    }

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 帐号状态
     * @return 结果
     */
    @Override
    public int updateUserStatus(Long userId, String status) {
        return baseMapper.update(null,
            new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getStatus, status)
                .eq(SysUser::getUserId, userId));
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUserBo user) {
        return baseMapper.update(null,
            new LambdaUpdateWrapper<SysUser>()
                .set(ObjectUtil.isNotNull(user.getNickName()), SysUser::getNickName, user.getNickName())
                .set(SysUser::getPhonenumber, user.getPhonenumber())
                .set(SysUser::getEmail, user.getEmail())
                .set(SysUser::getSex, user.getSex())
                .eq(SysUser::getUserId, user.getUserId()));
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(Long userId, Long avatar) {
        return baseMapper.update(null,
            new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getAvatar, avatar)
                .eq(SysUser::getUserId, userId)) > 0;
    }

    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(Long userId, String password) {
        int result = baseMapper.update(null,
            new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getPassword, password)
                .eq(SysUser::getUserId, userId));
        // 重置成功后添加强制修改密码标记
        if (result > 0) {
            NewUserUtils.setForceResetPassValue(userId, true);
        }
        return result;
    }

    /**
     * 新增用户角色信息
     *
     * @param user  用户对象
     * @param clear 清除已存在的关联数据
     */
    private void insertUserRole(SysUserBo user, boolean clear) {
        this.insertUserRole(user.getUserId(), user.getRoleIds(), clear);
    }

    /**
     * 新增用户岗位信息
     *
     * @param user  用户对象
     * @param clear 清除已存在的关联数据
     */
    private void insertUserPost(SysUserBo user, boolean clear) {
        Long[] posts = user.getPostIds();
        if (ArrayUtil.isNotEmpty(posts)) {
            if (clear) {
                // 删除用户与岗位关联
                userPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, user.getUserId()));
            }
            // 新增用户与岗位管理
            List<SysUserPost> list = StreamUtils.toList(List.of(posts), postId -> {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                return up;
            });
            userPostMapper.insertBatch(list);
        }
    }

    /**
     * 新增用户部门信息
     *
     * @param user  用户对象
     * @param clear 清除已存在的关联数据
     */
    private void insertUserDept(SysUserBo user, boolean clear) {
        Long[] depts = user.getDeptIds();
        if (ArrayUtil.isNotEmpty(depts)) {
            if (clear) {
                // 删除用户与岗位关联
                userDeptMapper.delete(new LambdaQueryWrapper<SysUserDept>().eq(SysUserDept::getUserId, user.getUserId()));
            }
            // 新增用户与岗位管理
            List<SysUserDept> list = StreamUtils.toList(List.of(depts), postId -> {
                SysUserDept up = new SysUserDept();
                up.setUserId(user.getUserId());
                up.setDeptId(postId);
                return up;
            });
            userDeptMapper.insertBatch(list);
        }
    }

    /**
     * excel导入用户部门
     *
     * @return
     */
    public Long[] getDeptIdsByNames( String deptNames) {
        String[] split = deptNames.split(",");
        List<Long> deptIds = new ArrayList<>();
        for (String deptName : split) {
            List<SysDept> sysDepts = deptMapper.selectList(new LambdaQueryWrapper<SysDept>().eq(SysDept::getDeptName, deptName));
            if (sysDepts.size() > 0) {
                deptIds.add(sysDepts.get(0).getDeptId());
            }
        }
        if (deptIds.size() < 1)
            throw new ThreadException("未找到对应部门" + deptNames);
        return deptIds.toArray(Long[]::new);
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     * @param clear   清除已存在的关联数据
     */
    private void insertUserRole(Long userId, Long[] roleIds, boolean clear) {
        if (ArrayUtil.isNotEmpty(roleIds)) {
            // 判断是否具有此角色的操作权限
            List<SysRoleVo> roles = roleMapper.selectRoleList(new LambdaQueryWrapper<>());
            if (CollUtil.isEmpty(roles)) {
                throw new ServiceException("没有权限访问角色的数据");
            }
            List<Long> roleList = StreamUtils.toList(roles, SysRoleVo::getRoleId);
            if (!LoginHelper.isSuperAdmin(userId)) {
                roleList.remove(UserConstants.SUPER_ADMIN_ID);
            }
            List<Long> canDoRoleList = StreamUtils.filter(List.of(roleIds), roleList::contains);
            if (CollUtil.isEmpty(canDoRoleList)) {
                throw new ServiceException("没有权限访问角色的数据");
            }
            if (clear) {
                // 删除用户与角色关联
                userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
            }
            // 新增用户与角色管理
            List<SysUserRole> list = StreamUtils.toList(canDoRoleList, roleId -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                return ur;
            });
            userRoleMapper.insertBatch(list);
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(Long userId) {
        // 删除用户与角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        // 删除用户与岗位表
        userPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, userId));
        // 防止更新失败导致的数据删除
        int flag = baseMapper.deleteById(userId);
        if (flag < 1) {
            throw new ServiceException("删除用户失败!");
        }
        return flag;
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(Long[] userIds) {
        for (Long userId : userIds) {
            checkUserAllowed(userId);
            checkUserDataScope(userId);
        }
        List<Long> ids = List.of(userIds);
        // 删除用户与角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, ids));
        // 删除用户与岗位表
        userPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().in(SysUserPost::getUserId, ids));
        // 防止更新失败导致的数据删除
        int flag = baseMapper.deleteBatchIds(ids);
        if (flag < 1) {
            throw new ServiceException("删除用户失败!");
        }
        return flag;
    }

    /**
     * 通过部门id查询当前部门所有用户
     *
     * @param deptId
     * @return
     */
    @Override
    public List<SysUserVo> selectUserListByDept(Long deptId) {
        List<SysUserVo> sysUserVos = baseMapper.selectUserListByDeptId(deptId);
        return sysUserVos;
    }

    @Cacheable(cacheNames = CacheNames.SYS_USER_NAME, key = "#userId")
    @Override
    public String selectUserNameById(Long userId) {
        SysUser sysUser = baseMapper.selectOne(new LambdaQueryWrapper<SysUser>()
            .select(SysUser::getUserName).eq(SysUser::getUserId, userId));
        return ObjectUtil.isNull(sysUser) ? null : sysUser.getUserName();
    }

    @Override
    @Cacheable(cacheNames = CacheNames.SYS_NICKNAME, key = "#userId")
    public String selectNicknameById(Long userId) {
        SysUser sysUser = baseMapper.selectOne(new LambdaQueryWrapper<SysUser>()
            .select(SysUser::getNickName).eq(SysUser::getUserId, userId));
        return ObjectUtil.isNull(sysUser) ? null : sysUser.getNickName();
    }


    /**
     * 通过用户账号查询userId
     *
     * @param userName 用户名称
     * @return 用户账户
     */
   public  Long selectUserIdByUserName(String userName){
       SysUser sysUser = baseMapper.selectOne(new LambdaQueryWrapper<SysUser>()
           .select(SysUser::getUserId).eq(SysUser::getUserName, userName));
       return ObjectUtil.isNull(sysUser) ? null : sysUser.getUserId();
   }

   public  List<SysUserVo> selectAll(){
      return  baseMapper.selectVoList(new LambdaQueryWrapper<SysUser>().orderByDesc(SysUser::getUserId));
    }


    /**
     * 通过用户id查询dept
     * @param userId
     * @return
     */
    @Override
    public String selectDeptByUserId(Long userId){
        List<SysUserDept>  list = userDeptMapper.selectList(Wrappers.<SysUserDept>query().lambda().eq(SysUserDept::getUserId,userId));
        if(CollUtil.isEmpty(list)){
            return null;
        }else {
            return list.stream()
                .map(d -> d.getDeptId()+"")
                .collect(Collectors.joining(StringUtils.SEPARATOR));
        }

    }

    @Override
    public TableDataInfo<SysUserVo> selectPageUserFilterList(SysUserBo user, PageQuery pageQuery) {
        Wrapper<SysUser> userWrapper = this.buildNewQueryWrapper(user);
        if( userWrapper == null){
            //返回null
            Page<SysUserVo> page = pageQuery.build();
            return TableDataInfo.build(page);
        }
        Page<SysUserVo> page = baseMapper.selectPageUserList(pageQuery.build(), userWrapper);
        for (SysUserVo record : page.getRecords()) {
            List<String> deptNames = deptMapper.selectDeptNameListByUserId(record.getUserId());
            record.setDeptName(String.join(", ", deptNames));
        }
        return TableDataInfo.build(page);
    }


    private Wrapper<SysUser> buildNewQueryWrapper(SysUserBo user) {
        //增加下级部门查询  获取所有的部门列表
        List<Long> deptIdList   = new ArrayList<>();
        //部门增加显示
        if( ArrayUtil.isNotEmpty(user.getDeptIds()) ){
//            List<SysDept> depts   = new ArrayList<>();
//            for(Long deptId :user.getDeptIds()){
//                List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
//                    .select(SysDept::getDeptId)
//                    .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
//                SysDept dept = new SysDept();
//                dept.setDeptId(deptId);
//                depts.add(dept);
//                depts.addAll(deptList);
//            }
//            //去除重复数据
//            deptIdList = depts.stream().map(o->o.getDeptId()).distinct().collect(Collectors.toList());
//            if(ObjectUtil.isNotNull(user.getDeptId())){
//                if(!deptIdList.contains(user.getDeptId())){
//                    //用户选择部门与限制部门冲突，直接返回空对象
//                    return null;
//                }else {
//                    //作进一步的筛选 仅选择已传入的deptId
//                    List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
//                        .select(SysDept::getDeptId)
//                        .apply(DataBaseHelper.findInSet(user.getDeptId(), "ancestors")));
//                    if(CollUtil.isEmpty(deptList)){
//                        deptList = new ArrayList<>();
//                    }
//                    SysDept dept = new SysDept();
//                    dept.setDeptId(user.getDeptId());
//                    deptList.add(dept);
//                    deptIdList = StreamUtils.toList(deptList, SysDept::getDeptId);
//
//                }
//            }
           ;
            deptIdList.addAll( Arrays.asList(user.getDeptIds()));
        }
        if(ObjectUtil.isNotNull(user.getDeptId())){
            if(!deptIdList.contains(user.getDeptId())){
               //用户选择部门与限制部门冲突，直接返回空对象
               return null;
            } else {
                //仅展示当前部门的用户
                deptIdList =new ArrayList<>();
                deptIdList.add(user.getDeptId());
            }

        }


        Map<String, Object> params = user.getParams();
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
            .eq(ObjectUtil.isNotNull(user.getUserId()), "u.user_id", user.getUserId())
            .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
            .eq(StringUtils.isNotBlank(user.getStatus()), "u.status", user.getStatus())
            .like(StringUtils.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                "u.create_time", params.get("beginTime"), params.get("endTime"));
        if(CollUtil.isNotEmpty(deptIdList)){
            wrapper.in("sud.dept_id", deptIdList);
        }

        if(user.getIsManager()!=null && user.getIsManager()){
            //仅查询部门负责人
          List<Long>  managers =  baseMapper.selectManagersByUserId(LoginHelper.getUserId());
          if(CollUtil.isEmpty(managers)){
              return null;
          }else {
              wrapper.in("u.user_id", managers);
          }
        }
        wrapper.orderByAsc("u.user_id");
        return wrapper;
    }



}
