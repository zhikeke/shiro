package com.shiro.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.shiro.beans.LogType;
import com.shiro.beans.PageQuery;
import com.shiro.beans.PageResult;
import com.shiro.common.RequestHolder;
import com.shiro.dao.*;
import com.shiro.dto.SearchDTO;
import com.shiro.exception.ParamException;
import com.shiro.model.*;
import com.shiro.parm.SearchParam;
import com.shiro.service.SysLogService;
import com.shiro.service.SysRoleService;
import com.shiro.util.BeanVaildator;
import com.shiro.util.IpUtil;
import com.shiro.util.JsonMapper;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/12 8:56
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysRoleService sysRoleService;


    @Override
    public void recover(int id) {
        SysLogWithBLOBs sysLog = sysLogMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysLog, "待还原的记录不存在!!!");
        switch (sysLog.getType()) {
            case LogType.TPYE_USER:
                SysUser beforeUser = sysUserMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeUser, "待还原的用户不存在!!!");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增或删除操作不做还原!!!");
                }
                SysUser afterUser = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysUser>() {
                });
                sysUserMapper.updateByPrimaryKeySelective(afterUser);
                saveUserLog(beforeUser, afterUser);
                break;

            case LogType.TYPE_DEPT:
                SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeDept, "待还原的部门不存在!!!");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增或删除操作不做还原!!!");
                }
                SysDept afterDept = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysDept>() {
                });
                sysDeptMapper.updateByPrimaryKeySelective(afterDept);
                saveDeptLog(beforeDept, afterDept);
                break;

            case LogType.TYPE_ACL_MODULE:
                SysAclModule beforeAclModule = sysAclModuleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeAclModule, "待还原的权限模块不存在!!!");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增或删除操作不做还原!!!");
                }
                SysAclModule aferAclModule = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysAclModule>() {
                });
                sysAclModuleMapper.updateByPrimaryKeySelective(aferAclModule);
                saveAclModuleLog(beforeAclModule, aferAclModule);
                break;

            case LogType.TYPE_ACL:
                SysAcl beforeAcl = sysAclMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeAcl, "待还原的权限点不存在!!!");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增或删除操作不做还原!!!");
                }
                SysAcl afterAcl = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysAcl>() {
                });
                sysAclMapper.updateByPrimaryKeySelective(afterAcl);
                saveAclLog(beforeAcl, afterAcl);
                break;

            case LogType.TYPE_ROLE:
                SysRole beforeRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(beforeRole, "待还原的角色不存在!!!");
                if (StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
                    throw new ParamException("新增或删除操作不做还原!!!");
                }
                SysRole afterRole = JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<SysRole>() {
                });
                sysRoleMapper.updateByPrimaryKeySelective(afterRole);
                saveRoleLog(beforeRole, afterRole);
                break;

            case LogType.TYPE_ROLE_ACL:
                SysRole aclRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(aclRole, "角色已经不存在了");
                sysRoleService.changeRoleAcl(sysLog.getTargetId(), JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;

            case LogType.TYPE_ROLE_USER:
                SysRole userRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
                Preconditions.checkNotNull(userRole, "角色已经不存在了");
                sysRoleService.changeRoleUser(sysLog.getTargetId(), JsonMapper.string2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
                }));
                break;
        }


    }


    @Override
    public PageResult<SysLogWithBLOBs> searchPage(SearchParam param, PageQuery page) {
        BeanVaildator.check(page);

        SearchDTO dto = new SearchDTO();

        dto.setType(param.getType());
        if (StringUtils.isNotBlank(param.getBeforeSeg())) {
            dto.setBeforeSeg("%" + param.getBeforeSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getAfterSeg())) {
            dto.setAfterSeg("%" + param.getAfterSeg() + "%");
        }
        if (StringUtils.isNotBlank(param.getOperator())) {
            dto.setOperator("%" + param.getOperator() + "%");
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (StringUtils.isNotBlank(param.getFromTime())) {
                dto.setFromTime(dateFormat.parse(param.getFromTime()));
            }
            if (StringUtils.isNotBlank(param.getToTime())) {
                dto.setToTime(dateFormat.parse(param.getToTime()));
            }
        } catch (Exception e) {
            throw new ParamException("传入的日期格式有问题，正确格式为：yyyy-MM-dd HH:mm:ss");
        }

        int count = sysLogMapper.countByLogParam(dto);
        if (count > 0) {
            List<SysLogWithBLOBs> data = sysLogMapper.getLogByLogParamAndPage(dto, page);
            PageResult<SysLogWithBLOBs> result = PageResult.<SysLogWithBLOBs>builder().data(data).total(count).build();
            return result;
        }
       return PageResult.<SysLogWithBLOBs>builder().build();
    }


    @Override
    public void saveUserLog(SysUser before, SysUser after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TPYE_USER);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    @Override
    public void saveDeptLog(SysDept before, SysDept after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_DEPT);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    @Override
    public void saveAclModuleLog(SysAclModule before, SysAclModule after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL_MODULE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    @Override
    public void saveAclLog(SysAcl before, SysAcl after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ACL);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    @Override
    public void saveRoleLog(SysRole before, SysRole after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE);
        sysLog.setTargetId(after == null ? before.getId() : after.getId());
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    @Override
    public void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }

    @Override
    public void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {
        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(new Date());
        sysLogMapper.insertSelective(sysLog);
    }


}
