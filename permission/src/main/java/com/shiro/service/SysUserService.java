package com.shiro.service;

import com.shiro.beans.PageQuery;
import com.shiro.beans.PageResult;
import com.shiro.model.SysUser;
import com.shiro.parm.UserParam;

import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/6 10:47
 */
public interface SysUserService {
    /**
     * 添加用户
     * @param userParam 用户参数
     */
    void saveUser(UserParam userParam);


    /**
     * 更新用户信息
     * @param userParam
     */
    void updateUser(UserParam userParam);

    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return
     */
    SysUser getUserByName(String username);

    /**
     * 根据部门id 和页面数据返回用户列表
     * @param deptId 部门Id
     * @param pageQuery 页面参数
     * @return
     */
    PageResult<SysUser> getPageByDeptId(Integer deptId, PageQuery pageQuery);
}
