package com.shiro.service;

import com.shiro.dto.AclModuleDTO;
import com.shiro.dto.DeptLevelDTO;

import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 16:39
 */
public interface SysTreeService {

    /**
     * 获取部门树
     * @return
     */
    List<DeptLevelDTO> deptTree();

    /**
     * 获取权限操作树
     * @return
     */
    List<AclModuleDTO> aclModuleTree();

    /**
     * 返回角色权限树
     * @param roleId 角色Id
     * @return
     */
    List<AclModuleDTO> roleAclTree(int roleId);
}
