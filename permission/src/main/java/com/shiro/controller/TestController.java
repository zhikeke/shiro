package com.shiro.controller;

import com.shiro.common.ApplicationContentHelper;
import com.shiro.common.JsonData;
import com.shiro.dao.SysAclModuleMapper;
import com.shiro.exception.PermissionException;
import com.shiro.model.SysAclModule;
import com.shiro.parm.TestVo;
import com.shiro.util.BeanVaildator;
import com.shiro.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/1 14:50
 */
@Controller
@RequestMapping("/test")
@Slf4j
public class TestController {

    @RequestMapping("/hello.json")
    @ResponseBody
    public JsonData hello() {
        throw new PermissionException("test exception");
        //return JsonData.success("hhhh");
    }


    @RequestMapping("/validate.json")
    @ResponseBody
    public JsonData validate(TestVo testVo) throws PermissionException{
        log.info("validate");
        SysAclModuleMapper moduleMapper = ApplicationContentHelper.popBean(SysAclModuleMapper.class);
        SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.obj2String(module));

        BeanVaildator.check(testVo);
        return JsonData.success("validate");
    }



}
