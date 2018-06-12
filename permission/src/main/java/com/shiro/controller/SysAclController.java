package com.shiro.controller;

import com.shiro.beans.PageQuery;
import com.shiro.common.JsonData;
import com.shiro.parm.AclParam;
import com.shiro.service.SysAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 16:01
 */
@Controller
@RequestMapping("/sys/acl")
public class SysAclController {
    @Autowired
    private SysAclService sysAclService;


    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData list(@RequestParam("aclModuleId") Integer aclModuleId, PageQuery pageQuery) {
        return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));
    }


    /**
     * 添加权限点
     * @param aclParam 权限点参数
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAcl(AclParam aclParam) {
        sysAclService.save(aclParam);
        return JsonData.success();
    }

    /**
     * 更新权限点
     * @param aclParam 权限点参数
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public JsonData updateAcl(AclParam aclParam) {
        sysAclService.update(aclParam);
        return JsonData.success();
    }
}
