package com.shiro.dao;

import com.shiro.beans.PageQuery;
import com.shiro.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    int countByUsername(@Param("id") Integer userId,@Param("username") String username);

    SysUser getUserByName(@Param("username") String username);

    int countByDeptId(@Param("deptId") Integer id);

    List<SysUser> getPageByDeptId(@Param("deptId") Integer deptId, @Param("page") PageQuery pageQuery);

    List<SysUser> selectAll();

}