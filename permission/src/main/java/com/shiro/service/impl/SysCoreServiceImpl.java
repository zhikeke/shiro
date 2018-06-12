package com.shiro.service.impl;

import com.google.common.collect.Lists;
import com.shiro.beans.CacheKeyConstants;
import com.shiro.common.RequestHolder;
import com.shiro.dao.*;
import com.shiro.model.SysAcl;
import com.shiro.model.SysUser;
import com.shiro.service.SysCoreService;
import com.shiro.util.JsonMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/8 10:20
 */
@Service
public class SysCoreServiceImpl implements SysCoreService {
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysCacheService sysCacheService;

    public List<SysAcl> getCurrentUserAclList() {
         int userId = RequestHolder.getCurrentUser().getId();
         return getUserAclList(userId);
    }

    private List<SysAcl> getCurrentUserAclListFromCache() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.selectFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
               sysCacheService.saveCache(JsonMapper.obj2String(aclList), 600, CacheKeyConstants.USER_ACLS, String.valueOf(userId));
            }
            return aclList;
        }
        return  JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });

    }


    public List<SysAcl> getRoleAclList(int roleId) {
        // 根据角色Id 列表获取角色Id 已分配的所有权限点
        List<Integer> userAclIds = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(userAclIds)) {
            return Lists.newArrayList();
        }

        // 根据权限点Id 列表获取所有的权限点信息
        return sysAclMapper.getByIdList(userAclIds);
    }

    @Override
    public boolean hasUrlAcl(String url) {
        if (isSuperAdmin(RequestHolder.getCurrentUser().getId())) {
            return true;
        }
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        List<SysAcl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        // 规则： 只要有一个权限点有权限，我们就认为有访问权限
        boolean hasValidAcl = false;
        for (SysAcl acl : aclList) {
            // 判断用户是否有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1) {
                continue;
            }
            hasValidAcl = true;
            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }
        if (!hasValidAcl) {
            return true;
        }
        return false;
    }




    private List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin(userId)) {
            return sysAclMapper.getAll();
        }

        // 取出当前用户已分配的角色Id列表
        List<Integer> userRoleIds = sysRoleUserMapper.getRoleIdsByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIds)) {
            return Lists.newArrayList();
        }

        // 根据角色Id 列表获取所有角色Id 已分配的权限点
        List<Integer> userAclIds = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIds);
        if (CollectionUtils.isEmpty(userAclIds)) {
            return Lists.newArrayList();
        }

        // 根据权限点Id 列表获取所有的权限点信息
        return sysAclMapper.getByIdList(userAclIds);
    }

    // TODO: 是否为超级管理员
    private boolean isSuperAdmin(int userId) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (sysUser.getUsername().equals("keke")) {
            return true;
        }
        return false;
    }

}
