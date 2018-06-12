package com.shiro.service;

import com.shiro.beans.PageQuery;
import com.shiro.beans.PageResult;
import com.shiro.parm.AclParam;
import com.shiro.model.SysAcl;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 16:03
 */
public interface SysAclService {
    /**
     * 添加权限点
     * @param aclParam
     */
    void save(AclParam aclParam);

    /**
     * 修改权限点
     * @param aclParam
     */
    void update(AclParam aclParam);

    /**
     * 根据权限模块Id获取权限点列表
     * @param aclModuleId 权限模块Id
     * @param pageQuery
     * @return
     */
    PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery pageQuery);
}
