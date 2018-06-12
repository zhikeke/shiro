package com.shiro.service;

import com.shiro.model.SysAcl;

import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/8 10:18
 */
public interface SysCoreService {

    /**
     * 获取当前用户已分配权限点
     * @return
     */
    public List<SysAcl> getCurrentUserAclList();

    /**
     * 获取角色已分配权限点
     * @return
     */
    public List<SysAcl> getRoleAclList(int roleId);

    /**
     * 判断用户是否有权限访问该地址
     * @param url 访问的地址
     * @return
     */
    boolean hasUrlAcl(String url);
}
