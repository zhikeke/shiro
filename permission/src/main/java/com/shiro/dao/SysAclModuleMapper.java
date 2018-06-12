package com.shiro.dao;

import com.shiro.model.SysAclModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);

    int countByNameAndParentId(@Param("name") String aclModuleName,
                               @Param("parentId") Integer parentId, @Param("id") Integer aclModuleId);

    List<SysAclModule> getChildAclModuleLlistByLevel(@Param("level") String childActModuleLevel);

    void batchUpdateLevel(@Param("childAclModuleList") List<SysAclModule> childAclModuleList);

    List<SysAclModule> getAllAclModule();

    int countByParentId(@Param("parentId") Integer id);

}