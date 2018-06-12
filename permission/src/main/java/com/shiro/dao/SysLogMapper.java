package com.shiro.dao;

import com.shiro.beans.PageQuery;
import com.shiro.dto.SearchDTO;
import com.shiro.model.SysLog;
import com.shiro.model.SysLogWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysLogWithBLOBs record);

    int insertSelective(SysLogWithBLOBs record);

    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    int updateByPrimaryKey(SysLog record);

    int countByLogParam(@Param("dto") SearchDTO dto);

    List<SysLogWithBLOBs> getLogByLogParamAndPage(@Param("dto") SearchDTO dto, @Param("page") PageQuery page);
}