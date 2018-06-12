package com.shiro.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.shiro.common.RequestHolder;
import com.shiro.dao.SysRoleAclMapper;
import com.shiro.dao.SysRoleMapper;
import com.shiro.dao.SysRoleUserMapper;
import com.shiro.dao.SysUserMapper;
import com.shiro.exception.ParamException;
import com.shiro.model.SysRole;
import com.shiro.model.SysRoleAcl;
import com.shiro.model.SysRoleUser;
import com.shiro.model.SysUser;
import com.shiro.parm.RoleParam;
import com.shiro.service.SysLogService;
import com.shiro.service.SysRoleService;
import com.shiro.util.BeanVaildator;
import com.shiro.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/8 9:26
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    public void save(RoleParam param) {
        BeanVaildator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("该角色名已存在!!!");
        }

        SysRole sysRole = SysRole.builder().name(param.getName()).type(param.getType()).status(param.getStatus())
                .remark(param.getRemark()).build();
        sysRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysRole.setOperateTime(new Date());

        sysRoleMapper.insertSelective(sysRole);
        sysLogService.saveRoleLog(null, sysRole);
    }

    public void update(RoleParam param) {
        BeanVaildator.check(param);

        SysRole beforeRole = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(beforeRole, "待更新对象不存在!!!");

        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("该角色名已存在!!!");
        }

        SysRole afterRole = SysRole.builder().id(param.getId()).name(param.getName()).type(param.getType()).status(param.getStatus())
                .remark(param.getRemark()).build();
        afterRole.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterRole.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterRole.setOperateTime(new Date());

        sysRoleMapper.updateByPrimaryKey(afterRole);
        sysLogService.saveRoleLog(beforeRole, afterRole);
    }

    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    @Override
    @Transactional
    public void changeRoleAcls(Integer roleId, String[] aclIds) {
        if (roleId == null) {
            throw  new ParamException("角色Id为空");
        }

        List<Integer> beforeAclIds = sysRoleAclMapper.getAclIdListByRoleIdList(Arrays.asList(roleId));
        List<Integer> afterAclIds = Lists.newArrayList();

         // 删除角色Id 原有的所有权限点
        sysRoleAclMapper.deleteByRoleId(roleId);
        // 添加角色权限点信息
        for (String temp : aclIds) {
            Integer aclId = Integer.parseInt(temp);
            SysRoleAcl sysRoleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).build();
            sysRoleAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysRoleAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            sysRoleAcl.setOperateTime(new Date());
            sysRoleAclMapper.insertSelective(sysRoleAcl);

            afterAclIds.add(aclId);
        }

        sysLogService.saveRoleAclLog(roleId, beforeAclIds, afterAclIds);
    }

    @Override
    public Map<String, List<SysUser>> users(int roleId) {
        // 获取角色绑定的用户列表
        List<SysUser> selectedUserList = sysRoleUserMapper.selectUsersByRoleId(roleId);

        // 获取所有的用户列表
        List<SysUser> allUserList = sysUserMapper.selectAll();

        List<SysUser> unselectedUserList = Lists.newArrayList();

        Set<Integer> selectedUserIdSet = selectedUserList.stream().map(sysUser -> sysUser.getId()).collect(Collectors.toSet());
        for(SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unselectedUserList.add(sysUser);
            }
        }

        Map<String, List<SysUser>> res = new HashMap<>();
        res.put("selected", selectedUserList);
        res.put("unselected", unselectedUserList);
        return res;
    }

    @Override
    @Transactional
    public void changeRoleUsers(Integer roleId, String[] userIds) {
        if (roleId == null) {
            throw new ParamException("角色Id不能为空!!!");
        }

        List<Integer> beforeUserIds = sysRoleUserMapper.selectUserIdsByRoleId(roleId);
        List<Integer> afterUserIds = Lists.newArrayList();

        // 删除角色Id 原绑定的所有用户
        sysRoleUserMapper.deleteByRoleId(roleId);

        // 角色绑定用户
        if (userIds != null && userIds.length > 0) {
            for (String temp : userIds) {
                int userId = Integer.parseInt(temp);
                SysRoleUser sysRoleUser = SysRoleUser.builder().roleId(roleId).userId(userId).build();
                sysRoleUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                sysRoleUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                sysRoleUser.setOperateTime(new Date());
                sysRoleUserMapper.insertSelective(sysRoleUser);

                afterUserIds.add(userId);
            }
        }

        sysLogService.saveRoleUserLog(roleId, beforeUserIds, afterUserIds);
    }

    private boolean checkExist(String roleName, Integer roleId) {
        return sysRoleMapper.getCountByName(roleName, roleId) > 0;
    }

    @Override
    public void changeRoleAcl(Integer roleId, List<Integer> aclIds) {
        if (roleId == null) {
            throw  new ParamException("角色Id为空");
        }

        List<Integer> beforeAclIds = sysRoleAclMapper.getAclIdListByRoleIdList(Arrays.asList(roleId));
        List<Integer> afterAclIds = Lists.newArrayList();

        // 删除角色Id 原有的所有权限点
        sysRoleAclMapper.deleteByRoleId(roleId);
        // 添加角色权限点信息
        for (Integer aclId : aclIds) {
            SysRoleAcl sysRoleAcl = SysRoleAcl.builder().roleId(roleId).aclId(aclId).build();
            sysRoleAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
            sysRoleAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
            sysRoleAcl.setOperateTime(new Date());
            sysRoleAclMapper.insertSelective(sysRoleAcl);

            afterAclIds.add(aclId);
        }

        sysLogService.saveRoleAclLog(roleId, beforeAclIds, afterAclIds);
    }

    @Override
    public void changeRoleUser(Integer roleId, List<Integer> userIds) {
        if (roleId == null) {
            throw new ParamException("角色Id不能为空!!!");
        }

        List<Integer> beforeUserIds = sysRoleUserMapper.selectUserIdsByRoleId(roleId);
        List<Integer> afterUserIds = Lists.newArrayList();

        // 删除角色Id 原绑定的所有用户
        sysRoleUserMapper.deleteByRoleId(roleId);

        // 角色绑定用户
        if (userIds != null && userIds.size() > 0) {
            for (Integer userId : userIds) {
                SysRoleUser sysRoleUser = SysRoleUser.builder().roleId(roleId).userId(userId).build();
                sysRoleUser.setOperator(RequestHolder.getCurrentUser().getUsername());
                sysRoleUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
                sysRoleUser.setOperateTime(new Date());
                sysRoleUserMapper.insertSelective(sysRoleUser);

                afterUserIds.add(userId);
            }
        }

        sysLogService.saveRoleUserLog(roleId, beforeUserIds, afterUserIds);
    }



}
