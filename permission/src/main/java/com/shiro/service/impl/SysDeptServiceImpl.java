package com.shiro.service.impl;

import com.google.common.base.Preconditions;
import com.shiro.common.RequestHolder;
import com.shiro.dao.SysDeptMapper;
import com.shiro.dao.SysUserMapper;
import com.shiro.exception.ParamException;
import com.shiro.model.SysDept;
import com.shiro.parm.DeptParam;
import com.shiro.service.SysDeptService;
import com.shiro.service.SysLogService;
import com.shiro.util.BeanVaildator;
import com.shiro.util.LevelUtil;
import com.shiro.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 15:12
 */
@Service
@Slf4j
public class SysDeptServiceImpl implements SysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogService sysLogService;

    public void save(DeptParam deptParam) {
        BeanVaildator.check(deptParam);
        if (checkExists(deptParam.getName(), deptParam.getParentId(), deptParam.getId())) {
            throw new ParamException("该级别下已存在该部门!!!");
        }
        SysDept dept = SysDept.builder().name(deptParam.getName()).remark(deptParam.getRemark())
                .seq(deptParam.getSeq()).parentId(deptParam.getParentId()).build();

        dept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(new Date());

        sysDeptMapper.insertSelective(dept);
        sysLogService.saveDeptLog(null, dept);
    }


    public void update(DeptParam deptParam) {
        BeanVaildator.check(deptParam);
        if (checkExists(deptParam.getName(), deptParam.getParentId(), deptParam.getId())) {
            throw new ParamException("该级别下已存在该部门!!!");
        }

        SysDept beforeDept = sysDeptMapper.selectByPrimaryKey(deptParam.getId());
        Preconditions.checkNotNull(beforeDept, "待更新部门不存在!!!");


        SysDept afterDept = SysDept.builder().id(deptParam.getId()).name(deptParam.getName()).remark(deptParam.getRemark())
                .seq(deptParam.getSeq()).parentId(deptParam.getParentId()).build();

        afterDept.setLevel(LevelUtil.calculateLevel(getLevel(deptParam.getParentId()), deptParam.getParentId()));
        afterDept.setOperator(RequestHolder.getCurrentUser().getUsername());
        afterDept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        afterDept.setOperateTime(new Date());

        updateWithChild(beforeDept, afterDept);
    }

    public void delete(@RequestParam("id") Integer id) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(dept, "待删除的部门不存在!!!");

        if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有子部门，无法删除");
        }
        if(sysUserMapper.countByDeptId(dept.getId()) > 0) {
            throw new ParamException("当前部门下面有用户，无法删除");
        }

        sysDeptMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    void updateWithChild(SysDept beforeDept, SysDept afterDept) {
        String newLevelPrefix = afterDept.getLevel();
        String oldLevelPrefix = beforeDept.getLevel();

        if (!newLevelPrefix.equals(oldLevelPrefix)) {

            String childDeptLevel = LevelUtil.calculateLevel(beforeDept.getLevel(), beforeDept.getId());

            List<SysDept> childDeptList = sysDeptMapper.getChildDeptLlistByLevel(childDeptLevel);
            if (CollectionUtils.isNotEmpty(childDeptList)) {
                for (SysDept chileDept : childDeptList) {
                    String level = chileDept.getLevel();
                    if (level.indexOf(oldLevelPrefix) == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        chileDept.setLevel(level);
                    }
                }
                sysDeptMapper.batchUpdateLevel(childDeptList);
            }
        }

        sysDeptMapper.updateByPrimaryKey(afterDept);
        sysLogService.saveDeptLog(beforeDept, afterDept);
    }




    private boolean checkExists(String deptName, Integer parentId, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(deptName, parentId, deptId) > 0;
    }

    public String getLevel(Integer deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        if (dept == null) {
            return null;
        }
        return dept.getLevel();
    }

}
