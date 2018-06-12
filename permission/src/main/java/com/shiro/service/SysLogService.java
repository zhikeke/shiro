package com.shiro.service;

import com.shiro.beans.PageQuery;
import com.shiro.beans.PageResult;
import com.shiro.model.*;
import com.shiro.parm.SearchParam;

import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/12 8:56
 */
public interface SysLogService {

    /**
     * 保存用户更新记录
     * @param before
     * @param after
     */
    public void saveUserLog(SysUser before, SysUser after);

    /**
     * 保存部门更新记录
     * @param before
     * @param after
     */
    public void saveDeptLog(SysDept before, SysDept after);

    /**
     * 保存权限模块更新记录
     * @param before
     * @param after
     */
    public void saveAclModuleLog(SysAclModule before, SysAclModule after);

    /**
     * 保存权限点更新记录
     * @param before
     * @param after
     */
    public void saveAclLog(SysAcl before, SysAcl after);

    /**
     * 保存角色更新记录
     * @param before
     * @param after
     */
    public void saveRoleLog(SysRole before, SysRole after);

    /**
     * 保存角色权限点更新记录
     * @param roleId
     * @param before
     * @param after
     */
    public void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after);

    /**
     * 保存角色用户更新记录
     * @param roleId
     * @param before
     * @param after
     */
    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after);

    /**
     * 根据搜索条件查询日志
     * @param searchParam 查询参数
     * @param page 分页参数
     * @return
     */
    PageResult<SysLogWithBLOBs> searchPage(SearchParam searchParam, PageQuery page);

    /**
     * 恢复操作
     * @param id
     */
    void recover(int id);
}
