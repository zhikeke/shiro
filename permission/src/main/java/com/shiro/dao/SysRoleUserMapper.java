package com.shiro.dao;

import com.shiro.model.SysRoleUser;
import com.shiro.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleUser record);

    int insertSelective(SysRoleUser record);

    SysRoleUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleUser record);

    int updateByPrimaryKey(SysRoleUser record);

    List<Integer> getRoleIdsByUserId(@Param("userId") int userId);

    List<SysUser> selectUsersByRoleId(@Param("roleId") int roleId);

    void deleteByRoleId(@Param("roleId") int roleId);

    List<Integer> selectUserIdsByRoleId(@Param("roleId") Integer roleId);
}