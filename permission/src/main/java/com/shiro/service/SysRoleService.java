package com.shiro.service;

import com.shiro.model.SysRole;
import com.shiro.model.SysUser;
import com.shiro.parm.RoleParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/8 9:23
 */
public interface SysRoleService {

    /**
     * 添加角色
     * @param param 角色参数
     * @return
     */
    void save(RoleParam param);

    /**
     * 更新角色
     * @param param 角色参数
     * @return
     */
    void update(RoleParam param);

    /**
     * 返回所有角色列表
     * @return
     */
    List<SysRole> getAll();

    /**
     * 保存用户权限点信息
     * @param roleId 角色Id
     * @param aclIds 权限点Id 列表
     */
    void changeRoleAcls(Integer roleId, String[] aclIds);

    /**
     * 获取角色用户数据
     * @param roleId 角色Id
     * @return
     */
    Map<String, List<SysUser>> users(int roleId);

    /**
     * 保存角色用户数据
     * @param roleId 角色Id
     * @param userIds 用户Id 列表
     * @return
     */
    void changeRoleUsers(Integer roleId, String[] userIds);

    /**
     * 保存角色用户数据
     * @param roleId 角色Id
     * @param aclIds 权限点Id 列表
     * @return
     */
    void changeRoleAcl(Integer roleId, List<Integer> aclIds);

    /**
     * 保存角色用户数据
     * @param roleId 角色Id
     * @param userIds 用户Id 列表
     * @return
     */
    void changeRoleUser(Integer roleId, List<Integer> userIds);
}
