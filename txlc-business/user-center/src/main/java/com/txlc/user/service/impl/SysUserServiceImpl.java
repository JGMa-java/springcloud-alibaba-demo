package com.txlc.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.txlc.common.constant.CommonConstant;
import com.txlc.common.lock.DistributedLock;
import com.txlc.common.model.*;
import com.txlc.common.service.impl.SuperServiceImpl;
import com.txlc.user.mapper.SysRoleMenuMapper;
import com.txlc.user.mapper.SysUserMapper;
import com.txlc.user.model.SysRoleUser;
import com.txlc.user.service.ISysRoleUserService;
import com.txlc.user.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 */
@Slf4j
@Service
public class SysUserServiceImpl extends SuperServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private final static String LOCK_KEY_USERNAME = "username:";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private ISysRoleUserService roleUserService;

    @Resource
    private SysRoleMenuMapper roleMenuMapper;

    @Autowired(required = false)
    private DistributedLock lock;

    @Override
    public LoginAppUser findByUsername(String username) {
        SysUser sysUser = this.selectByUsername(username);
        return getLoginAppUser(sysUser);
    }

    @Override
    public LoginAppUser findByOpenId(String username) {
        SysUser sysUser = this.selectByOpenId(username);
        return getLoginAppUser(sysUser);
    }

    @Override
    public LoginAppUser findByMobile(String username) {
        SysUser sysUser = this.selectByMobile(username);
        return getLoginAppUser(sysUser);
    }

    @Override
    public LoginAppUser getLoginAppUser(SysUser sysUser) {
        if (sysUser != null) {
            LoginAppUser loginAppUser = new LoginAppUser();
            BeanUtils.copyProperties(sysUser, loginAppUser);

            List<SysRole> sysRoles = roleUserService.findRolesByUserId(sysUser.getId());
            // 设置角色
            loginAppUser.setRoles(sysRoles);

            if (!CollectionUtils.isEmpty(sysRoles)) {
                Set<Long> roleIds = sysRoles.parallelStream().map(SuperEntity::getId).collect(Collectors.toSet());
                List<SysMenu> menus = roleMenuMapper.findMenusByRoleIds(roleIds, CommonConstant.PERMISSION);
                if (!CollectionUtils.isEmpty(menus)) {
                    Set<String> permissions = menus.parallelStream().map(p -> p.getPath())
                            .collect(Collectors.toSet());
                    // 设置权限集合
                    loginAppUser.setPermissions(permissions);
                }
            }
            return loginAppUser;
        }
        return null;
    }

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    @Override
    public SysUser selectByUsername(String username) {
        List<SysUser> users = baseMapper.selectList(
                new QueryWrapper<SysUser>().eq("username", username)
        );
        return getUser(users);
    }

    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    @Override
    public SysUser selectByMobile(String mobile) {
        List<SysUser> users = baseMapper.selectList(
                new QueryWrapper<SysUser>().eq("mobile", mobile)
        );
        return getUser(users);
    }

    /**
     * 根据openId查询用户
     * @param openId
     * @return
     */
    @Override
    public SysUser selectByOpenId(String openId) {
        List<SysUser> users = baseMapper.selectList(
                new QueryWrapper<SysUser>().eq("open_id", openId)
        );
        return getUser(users);
    }

    private SysUser getUser(List<SysUser> users) {
        SysUser user = null;
        if (users != null && !users.isEmpty()) {
            user = users.get(0);
        }
        return user;
    }

    /**
     * 给用户设置角色
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setRoleToUser(Long id, Set<Long> roleIds) {
        SysUser sysUser = baseMapper.selectById(id);
        if (sysUser == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        roleUserService.deleteUserRole(id, null);
        if (!CollectionUtils.isEmpty(roleIds)) {
            List<SysRoleUser> roleUsers = new ArrayList<>(roleIds.size());
            roleIds.forEach(roleId -> roleUsers.add(new SysRoleUser(id, roleId)));
            roleUserService.saveBatch(roleUsers);
        }
    }

    @Transactional
    @Override
    public BaseRes updatePassword(Long id, String oldPassword, String newPassword) {
        SysUser sysUser = baseMapper.selectById(id);
        if (StrUtil.isNotBlank(oldPassword)) {
            if (!passwordEncoder.matches(oldPassword, sysUser.getPassword())) {
                return BaseRes.err("旧密码错误");
            }
        }
        if (StrUtil.isBlank(newPassword)) {
            newPassword = CommonConstant.DEF_USER_PASSWORD;
        }
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        baseMapper.updateById(user);
        return BaseRes.ok("修改成功");
    }

    @Override
    public List<SysRole> findRolesByUserId(Long userId) {
        return roleUserService.findRolesByUserId(userId);
    }

    @Override
    public BaseRes updateEnabled(Map<String, Object> params) {
        Long id = MapUtils.getLong(params, "id");
        Boolean enabled = MapUtils.getBoolean(params, "enabled");

        SysUser appUser = baseMapper.selectById(id);
        if (appUser == null) {
            return BaseRes.err("用户不存在");
        }
        appUser.setEnabled(enabled);
        appUser.setUpdateTime(new Date());

        int i = baseMapper.updateById(appUser);
        log.info("修改用户：{}", appUser);

        return i > 0 ? BaseRes.ok(appUser, "更新成功") : BaseRes.err("更新失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseRes saveOrUpdateUser(SysUser sysUser) throws Exception {
        if (sysUser.getId() == null) {
            if (StringUtils.isBlank(sysUser.getType())) {
                sysUser.setType(UserType.BACKEND.name());
            }
            sysUser.setPassword(passwordEncoder.encode(CommonConstant.DEF_USER_PASSWORD));
            sysUser.setEnabled(Boolean.TRUE);
        }
        String username = sysUser.getUsername();
        boolean result = super.saveOrUpdateIdempotency(sysUser, lock
                , LOCK_KEY_USERNAME+username, new QueryWrapper<SysUser>().eq("username", username)
                , username+"已存在");
        //更新角色
        if (result && StrUtil.isNotEmpty(sysUser.getRoleId())) {
            roleUserService.deleteUserRole(sysUser.getId(), null);
            List roleIds = Arrays.asList(sysUser.getRoleId().split(","));
            if (!CollectionUtils.isEmpty(roleIds)) {
                List<SysRoleUser> roleUsers = new ArrayList<>(roleIds.size());
                roleIds.forEach(roleId -> roleUsers.add(new SysRoleUser(sysUser.getId(), Long.parseLong(roleId.toString()))));
                roleUserService.saveBatch(roleUsers);
            }
        }
        return result ? BaseRes.ok(sysUser, "操作成功") : BaseRes.err("操作失败");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delUser(Long id) {
        roleUserService.deleteUserRole(id, null);
        return baseMapper.deleteById(id) > 0;
    }

}