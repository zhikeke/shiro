package com.shiro.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.shiro.common.JsonData;
import com.shiro.common.RequestHolder;
import com.shiro.dao.SysAclMapper;
import com.shiro.dao.SysAclModuleMapper;
import com.shiro.dao.SysDeptMapper;
import com.shiro.dto.AclDTO;
import com.shiro.dto.AclModuleDTO;
import com.shiro.dto.DeptLevelDTO;
import com.shiro.model.SysAcl;
import com.shiro.model.SysAclModule;
import com.shiro.model.SysDept;
import com.shiro.service.SysCoreService;
import com.shiro.service.SysTreeService;
import com.shiro.util.JsonMapper;
import com.shiro.util.LevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.acl.Acl;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 16:39
 */
@Service
public class SysTreeServiceImpl implements SysTreeService {

    @Autowired
    private SysDeptMapper sysDeptMapper;
    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;
    @Autowired
    private SysAclMapper sysAclMapper;
    @Autowired
    private SysCoreService sysCoreService;

    public List<DeptLevelDTO> deptTree() {
        // 获取所有部门列表
        List<SysDept> deptList = sysDeptMapper.getAllDept();

        List<DeptLevelDTO> dtoList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            DeptLevelDTO dto = DeptLevelDTO.adapt(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);
    }


    private List<DeptLevelDTO> deptListToTree(List<DeptLevelDTO> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }

        // level --> [dept1, dept2....]
        Multimap<String, DeptLevelDTO> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDTO> rootList = Lists.newArrayList();

        for (DeptLevelDTO dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);

            // 判断该节点是否为根目录,如果是则加入根目录中
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
            // 按照seq从小到大排序
        Collections.sort(rootList, deptSeqComparator);

        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }


    public void transformDeptTree(List<DeptLevelDTO> deptLevelList, String level, Multimap<String, DeptLevelDTO> levelDeptMap) {
           for (int i = 0; i < deptLevelList.size(); i++) {
               // 遍历该层的所有元素
              DeptLevelDTO deptLevelDTO = deptLevelList.get(i);

               // 处理当前层级的数据
               String nextLevel = LevelUtil.calculateLevel(level, deptLevelDTO.getId());

               // 处理下一层
               List<DeptLevelDTO> tempDeptList = (List<DeptLevelDTO>)levelDeptMap.get(nextLevel);
               if (CollectionUtils.isNotEmpty(tempDeptList)) {
                   // 排序
                   Collections.sort(tempDeptList, deptSeqComparator);
                   // 将原节点的部门列表设置为排序好的部门列表
                   deptLevelDTO.setDeptList(tempDeptList);
                   // 进入下一层处理
                   transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
               }
           }
    }



    public List<AclModuleDTO> aclModuleTree() {
        // 获取所有权限操作列表
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();

        List<AclModuleDTO> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : aclModuleList) {
            AclModuleDTO dto = AclModuleDTO.adapt(aclModule);
            dtoList.add(dto);
        }
        return aclModuleListToTree(dtoList);
    }


    public List<AclModuleDTO> roleAclTree(int roleId) {
         // 当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();

        // 当前角色已分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);

        // 当前系统所有的权限点
        List<AclDTO> aclDtoList = new ArrayList<>();

        Set<Integer> userAclSet = userAclList.stream().map(SysAcl -> SysAcl.getId()).collect(Collectors.toSet());
        Set<Integer> roleAclSet = roleAclList.stream().map(SysAcl -> SysAcl.getId()).collect(Collectors.toSet());

        // 获取所有的acl 列表
        List<SysAcl> allAclList =sysAclMapper.getAll();

        for (SysAcl acl : allAclList) {
            AclDTO aclDto = AclDTO.adapt(acl);
            if (userAclSet.contains(acl.getId())) {
                aclDto.setHasAcl(true);
            }
            if (roleAclSet.contains(acl.getId())) {
                aclDto.setChecked(true);
            }
            aclDtoList.add(aclDto);
        }
       return aclDtoListToTree(aclDtoList);
    }

    // 将Acl 列表对象转换为树对象
    private List<AclModuleDTO> aclDtoListToTree(List<AclDTO> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        // 获取所有的权限操作列表
        List<AclModuleDTO> aclModuleList = aclModuleTree();

        Multimap<Integer, AclDTO> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDTO aclDto : aclDtoList) {
            if (aclDto.getStatus() == 1) {
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }

        bindAclsWithOrder(aclModuleList, moduleIdAclMap);
        return aclModuleList;
    }

    private void bindAclsWithOrder(List<AclModuleDTO> aclModuleList, Multimap<Integer, AclDTO> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleList)) {
            return;
        }

        for (AclModuleDTO aclModuleDTO : aclModuleList) {
            List<AclDTO> aclDtoList = (List<AclDTO>)moduleIdAclMap.get(aclModuleDTO.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                 Collections.sort(aclDtoList, aclSeqComparator);
                 aclModuleDTO.setAclList(aclDtoList);
            }
           bindAclsWithOrder(aclModuleDTO.getAclModuleList(), moduleIdAclMap);
        }
    }


    private List<AclModuleDTO> aclModuleListToTree(List<AclModuleDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Lists.newArrayList();
        }

        // level --> [aclModule1, aclModule2....]
        Multimap<String, AclModuleDTO> levelAclModuleMap = ArrayListMultimap.create();
        List<AclModuleDTO> rootList = Lists.newArrayList();

        for (AclModuleDTO dto : dtoList) {
            levelAclModuleMap.put(dto.getLevel(), dto);

            // 判断该节点是否为根目录,如果是则加入根目录中
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 按照seq从小到大排序
        Collections.sort(rootList, aclModuleSeqComparator);

        transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
        return rootList;
    }

    private void transformAclModuleTree(List<AclModuleDTO> rootList, String level, Multimap<String,AclModuleDTO> levelAclModuleMap) {
        for (int i = 0; i < rootList.size(); i++) {
            // 遍历该层的所有元素
            AclModuleDTO aclModuleDTO = rootList.get(i);

            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, aclModuleDTO.getId());

            // 处理下一层
            List<AclModuleDTO> tempAclModuleList = (List<AclModuleDTO>)levelAclModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempAclModuleList)) {
                // 排序
                Collections.sort(tempAclModuleList, aclModuleSeqComparator);
                // 将原节点的部门列表设置为排序好的部门列表
                aclModuleDTO.setAclModuleList(tempAclModuleList);
                // 进入下一层处理
                 transformAclModuleTree(tempAclModuleList, nextLevel, levelAclModuleMap);
            }
        }
    }


    // 排序 按seq 从小到大排序
    public Comparator<DeptLevelDTO> deptSeqComparator = new Comparator<DeptLevelDTO>() {
        public int compare(DeptLevelDTO o1, DeptLevelDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    // 排序 按seq 从小到大排序
    public Comparator<AclModuleDTO> aclModuleSeqComparator = new Comparator<AclModuleDTO>() {
        public int compare(AclModuleDTO o1, AclModuleDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    // 排序 按seq 从小到大排序
    public Comparator<AclDTO> aclSeqComparator = new Comparator<AclDTO>() {
        public int compare(AclDTO o1, AclDTO o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };


}
