package com.shiro.service.impl;

import com.google.common.base.Preconditions;
import com.shiro.common.RequestHolder;
import com.shiro.dao.SysAclMapper;
import com.shiro.dao.SysAclModuleMapper;
import com.shiro.exception.ParamException;
import com.shiro.model.SysAclModule;
import com.shiro.parm.SysAclModuleParam;
import com.shiro.service.SysAclModuleService;
import com.shiro.service.SysLogService;
import com.shiro.util.BeanVaildator;
import com.shiro.util.IpUtil;
import com.shiro.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 11:07
 */
@Service
public class SysAclModuleServiceImpl implements SysAclModuleService {
    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysLogService sysLogService;


    public void save(SysAclModuleParam param) {
        BeanVaildator.check(param);
        if (checkExists(param.getName(), param.getParentId(), param.getId())) {
            throw new ParamException("该级别下已存在该对象");
        }
        SysAclModule sysAclModule = SysAclModule.builder().name(param.getName()).parentId(param.getParentId())
                .status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();

        sysAclModule.setLevel(LevelUtil.calculateLevel(getLevel(sysAclModule.getParentId()), param.getParentId()));
        sysAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysAclModule.setOperateTime(new Date());

        sysAclModuleMapper.insertSelective(sysAclModule);
        sysLogService.saveAclModuleLog(null, sysAclModule);
    }

    public void update(SysAclModuleParam param) {
        SysAclModule beforeAclModule = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(beforeAclModule, "待更新的对象不存在!!!");

        BeanVaildator.check(param);
        if (checkExists(param.getName(), param.getParentId(), param.getId())) {
            throw new ParamException("该级别下已存在该对象");
        }
        SysAclModule afterAclModule = SysAclModule.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();

        afterAclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        afterAclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterAclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterAclModule.setOperateTime(new Date());

        updateWithChild(beforeAclModule, afterAclModule);
    }

    public void delete(Integer aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(sysAclModule, "待删除对象不存在!!!");


        if (sysAclModuleMapper.countByParentId(sysAclModule.getId()) > 0) {
            throw new ParamException("当前模块下存在其他对象,无法删除");
        }

        if (sysAclMapper.countByAclModuleId(sysAclModule.getId()) > 0) {
              throw new ParamException("当前模块绑定了其他权限点，无法操作");
        }


        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }

    private boolean checkExists(String aclModuleName, Integer parentId, Integer aclModuleId) {
        return sysAclModuleMapper.countByNameAndParentId(aclModuleName, parentId, aclModuleId) > 0;
    }

    public String getLevel(Integer aclModuleId) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (sysAclModule == null) {
            return null;
        }
        return sysAclModule.getLevel();
    }


    @Transactional
    void updateWithChild(SysAclModule beforeAclModule, SysAclModule afterAclModule) {
        String newLevelPrefix = afterAclModule.getLevel();
        String oldLevelPrefix = beforeAclModule.getLevel();

        if (!newLevelPrefix.equals(oldLevelPrefix)) {

            String childActModuleLevel = LevelUtil.calculateLevel(beforeAclModule.getLevel(), beforeAclModule.getId());

            List<SysAclModule> childAclModuleList = sysAclModuleMapper.getChildAclModuleLlistByLevel(childActModuleLevel);
            if (CollectionUtils.isNotEmpty(childAclModuleList)) {
                for (SysAclModule chileAclModule : childAclModuleList) {
                    String level = chileAclModule.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        chileAclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(childAclModuleList);
            }
        }

        sysAclModuleMapper.updateByPrimaryKey(afterAclModule);
        sysLogService.saveAclModuleLog(beforeAclModule, afterAclModule);
    }

}
