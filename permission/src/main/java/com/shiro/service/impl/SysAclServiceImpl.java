package com.shiro.service.impl;

import com.google.common.base.Preconditions;
import com.shiro.beans.PageQuery;
import com.shiro.beans.PageResult;
import com.shiro.common.RequestHolder;
import com.shiro.dao.SysAclMapper;
import com.shiro.parm.AclParam;
import com.shiro.exception.ParamException;
import com.shiro.model.SysAcl;
import com.shiro.service.SysAclService;
import com.shiro.service.SysLogService;
import com.shiro.util.BeanVaildator;
import com.shiro.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 16:14
 */
@Service
public class SysAclServiceImpl implements SysAclService {
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysLogService sysLogService;

    public void save(AclParam param) {
        BeanVaildator.check(param);
        if (checkExists(param.getName(), param.getAclModuleId(), param.getId())) {
            throw new ParamException("该级别下已存在该对象");
        }
        SysAcl sysAcl = SysAcl.builder().name(param.getName()).aclModuleId(param.getAclModuleId())
                .url(param.getUrl()).type(param.getType()).status(param.getStatus()).seq(param.getSeq())
                .remark(param.getRemark()).build();

        sysAcl.setCode(getCode());
        sysAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAcl.setOperateTime(new Date());

        sysAclMapper.insertSelective(sysAcl);
        sysLogService.saveAclLog(null, sysAcl);
    }


    public void update(AclParam param) {
        BeanVaildator.check(param);

        SysAcl beforeAcl = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(beforeAcl, "待更新对象不存在!!!");

        SysAcl afterAcl = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId())
                .url(param.getUrl()).type(param.getType()).status(param.getStatus()).seq(param.getSeq())
                .remark(param.getRemark()).build();

        afterAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterAcl.setOperateTime(new Date());

        sysAclMapper.updateByPrimaryKey(afterAcl);
        sysLogService.saveAclLog(beforeAcl, afterAcl);
    }

    public PageResult<SysAcl> getPageByAclModuleId(Integer aclModuleId, PageQuery pageQuery) {
        BeanVaildator.check(pageQuery);
        int count = sysAclMapper.countByAclModuleId(aclModuleId);
        if (count > 0) {
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, pageQuery);
            return PageResult.<SysAcl>builder().data(aclList).total(count).build();
        }
        return PageResult.<SysAcl>builder().build();
    }

    private boolean checkExists(String name, Integer aclModuleId, Integer id) {
        return sysAclMapper.getCountByNameAndModuleId(name, aclModuleId, id) > 0;
    }

    private String getCode(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
