package com.shiro.service;

import com.shiro.parm.DeptParam;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 15:11
 */
public interface SysDeptService {

    /**
     * 添加部门
     * @param deptParam 部门参数
     */
    void save(DeptParam deptParam);

    /**
     * 更新部门
     * @param deptParam 部门参数
     */
    void update(DeptParam deptParam);

    /**
     * 删除部门
     * @param id 部门Id
     */
    void delete(Integer id);
}
