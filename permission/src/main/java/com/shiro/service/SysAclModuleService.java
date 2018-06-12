package com.shiro.service;

import com.shiro.parm.SysAclModuleParam;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 10:56
 */
public interface SysAclModuleService {
    /**
     * 添加权限操作
     * @param param 权限操作参数
     */
    void save(SysAclModuleParam param);

    /**
     * 修改权限操作
     * @param param
     */
    void update(SysAclModuleParam param);

    /**
     * 删除权限操作
     * @param aclModuleId 权限操作Id
     */
    void delete(Integer aclModuleId);
}
