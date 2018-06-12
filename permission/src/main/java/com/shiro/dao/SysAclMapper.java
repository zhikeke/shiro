package com.shiro.dao;

import com.shiro.beans.PageQuery;
import com.shiro.model.SysAcl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);

    int getCountByNameAndModuleId(@Param("name") String name, @Param("aclModuleId") Integer aclModuleId, @Param("id") Integer id);

    int countByAclModuleId(@Param("aclModuleId") Integer aclModuleId);

    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") Integer aclModuleId, @Param("page") PageQuery pageQuery);

    List<SysAcl> getAll();

    List<SysAcl> getByIdList(@Param("ids") List<Integer> userAclIds);

    List<SysAcl> getByUrl(@Param("url") String url);
}