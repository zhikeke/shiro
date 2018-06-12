package com.shiro.controller;

import com.shiro.beans.PageQuery;
import com.shiro.common.JsonData;
import com.shiro.model.SysUser;
import com.shiro.parm.UserParam;
import com.shiro.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/6 10:27
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth() {
        return new ModelAndView("noAuth");
    }


    /**
     * 根据部门id 和页面数据返回用户列表
     * @param deptId 部门Id
     * @param pageQuery 页面参数
     * @return
     */
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData list(@RequestParam("deptId") Integer deptId, PageQuery pageQuery) {

        return JsonData.success(sysUserService.getPageByDeptId(deptId, pageQuery));
    }



    /**
     * 添加用户
     * @param userParam 用户参数
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public JsonData saveUser(UserParam userParam) {
         sysUserService.saveUser(userParam);
         return JsonData.success();
    }


    /**
     * 修改用户
     * @param userParam 用户参数
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public JsonData updateUser(UserParam userParam) {
        sysUserService.updateUser(userParam);
        return JsonData.success();
    }


}
