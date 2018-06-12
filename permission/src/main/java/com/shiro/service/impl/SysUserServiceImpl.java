package com.shiro.service.impl;

import com.google.common.base.Preconditions;
import com.shiro.beans.PageQuery;
import com.shiro.beans.PageResult;
import com.shiro.common.RequestHolder;
import com.shiro.dao.SysUserMapper;
import com.shiro.exception.ParamException;
import com.shiro.exception.PermissionException;
import com.shiro.model.SysAcl;
import com.shiro.model.SysUser;
import com.shiro.parm.UserParam;
import com.shiro.service.SysLogService;
import com.shiro.service.SysUserService;
import com.shiro.util.BeanVaildator;
import com.shiro.util.IpUtil;
import com.shiro.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/6 10:49
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    public void saveUser(UserParam userParam) {
        BeanVaildator.check(userParam);
        if (checkExists(userParam.getId(), userParam.getUsername())) {
            throw new ParamException("该用户名已存在!!!");
        }

        SysUser user = SysUser.builder().username(userParam.getUsername()).mail(userParam.getMail())
                .telephone(userParam.getTelephone()).deptId(userParam.getDeptId())
                .status(userParam.getStatus()).remark(userParam.getRemark()).build();

        user.setPassword(MD5Util.encrypt(userParam.getUsername()));
        user.setOperator(RequestHolder.getCurrentUser().getUsername());
        user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        user.setOperateTime(new Date());

        sysUserMapper.insertSelective(user);
        sysLogService.saveUserLog(null, user);
    }

    public void updateUser(UserParam userParam) {
        BeanVaildator.check(userParam);

        SysUser beforeUser = sysUserMapper.selectByPrimaryKey(userParam.getId());
        Preconditions.checkNotNull(beforeUser, "待更新用户不存在!!!");

        if (checkExists(userParam.getId(), userParam.getUsername())) {
            throw new ParamException("该用户名已存在!!!");
        }

        SysUser aferUser = SysUser.builder().id(userParam.getId()).username(userParam.getUsername()).mail(userParam.getMail())
                .telephone(userParam.getTelephone()).deptId(userParam.getDeptId())
                .status(userParam.getStatus()).remark(userParam.getRemark()).build();

        aferUser.setOperator(RequestHolder.getCurrentUser().getUsername());
        aferUser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aferUser.setOperateTime(new Date());

        sysUserMapper.updateByPrimaryKey(aferUser);
        sysLogService.saveUserLog(beforeUser, aferUser);
    }

    public SysUser getUserByName(String username) {
         if (StringUtils.isBlank(username)) {
             throw new PermissionException("用户名不能为空!!!");
         }
         return sysUserMapper.getUserByName(username);
    }

    public PageResult<SysUser> getPageByDeptId(Integer deptId, PageQuery pageQuery) {
        BeanVaildator.check(pageQuery);
        int count = sysUserMapper.countByDeptId(deptId);
        if (count > 0) {
            List<SysUser> userList = sysUserMapper.getPageByDeptId(deptId, pageQuery);
            return PageResult.<SysUser>builder().data(userList).total(count).build();
        }
        return PageResult.<SysUser>builder().build();
    }


    private boolean checkExists(Integer userId, String username) {
        return sysUserMapper.countByUsername(userId, username) > 0;
    }
}
