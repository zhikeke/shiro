package com.shiro.dao;

import com.shiro.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    List<SysDept> getAllDept();

    List<SysDept> getChildDeptLlistByLevel(@Param("level") String level);

    void batchUpdateLevel(@Param("childDeptList") List<SysDept> childDeptList);

    int countByNameAndParentId(@Param("name") String name, @Param("parentId") Integer parentId, @Param("id") Integer id);

    int countByParentId(@Param("parentId") Integer id);
}