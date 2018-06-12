package com.shiro.controller;

import com.shiro.common.JsonData;
import com.shiro.dto.AclModuleDTO;
import com.shiro.model.SysAclModule;
import com.shiro.parm.SysAclModuleParam;
import com.shiro.service.SysAclModuleService;
import com.shiro.service.SysTreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 10:52
 */

@Slf4j
@RequestMapping("/sys/aclModule")
@Controller
public class SysAclModuleController {
    @Autowired
    private SysAclModuleService sysAclModuleService;
    @Autowired
    private SysTreeService sysTreeService;


    @RequestMapping("/acl.page")
    public ModelAndView aclModulePage() {
        return new ModelAndView("acl");
    }

    /**
     * 获取权限操作树
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        List<AclModuleDTO> dtoList = sysTreeService.aclModuleTree();
        return JsonData.success(dtoList);
    }


    /**
     * 添加权限操作
     * @param param 权限操作参数
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(SysAclModuleParam param) {
        sysAclModuleService.save(param);
        return JsonData.success();
    }

    /**
     * 修改权限操作
     * @param param
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(SysAclModuleParam param) {
        sysAclModuleService.update(param);
        return JsonData.success();
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@RequestParam("id") Integer aclModuleId) {
        sysAclModuleService.delete(aclModuleId);
        return JsonData.success();
    }

}
