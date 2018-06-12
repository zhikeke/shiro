package com.shiro.controller;

import com.shiro.common.JsonData;
import com.shiro.parm.RoleParam;
import com.shiro.service.SysRoleService;
import com.shiro.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/8 9:22
 */
@Controller
@Slf4j
@RequestMapping("/sys/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("/role.page")
    public ModelAndView page() {
        return new ModelAndView("role");
    }

    /**
     * 添加角色
     * @param param 角色参数
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public JsonData save(RoleParam param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    /**
     * 更新角色
     * @param param 角色参数
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public JsonData update(RoleParam param) {
        sysRoleService.update(param);
        return JsonData.success();
    }

    /**
     * 返回所有角色列表
     * @return
     */
    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {
        return JsonData.success(sysRoleService.getAll());
    }


    /**
     * 返回角色权限树
     * @param roleId 角色Id
     * @return
     */
    @RequestMapping("/roleAclTree.json")
    @ResponseBody
    public JsonData roleAclTree(@RequestParam("roleId") int roleId) {
          return JsonData.success(sysTreeService.roleAclTree(roleId));
    }

    /**
     * 保存角色权限点数据
     * @param roleId 角色Id
     * @param aclIds 权限点Id 列表
     * @return
     */
    @RequestMapping("/changeAcls")
    @ResponseBody
    public JsonData changeAcls(@RequestParam("roleId") Integer roleId,@RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {
         sysRoleService.changeRoleAcls(roleId, aclIds.split(","));
         return JsonData.success();
    }

    /**
     * 返回角色用户数据
     * @param roleId 角色Id
     * @return
     */
    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData users(@RequestParam("roleId") int roleId) {
        return JsonData.success(sysRoleService.users(roleId));
    }

    /**
     * 保存角色用户数据
     * @param roleId 角色Id
     * @param userIds 用户Id 列表
     * @return
     */
    @RequestMapping("/changeUsers")
    @ResponseBody
    public JsonData changeUsers(@RequestParam("roleId") Integer roleId, @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        sysRoleService.changeRoleUsers(roleId, userIds.split(","));
         return JsonData.success();
    }



}
