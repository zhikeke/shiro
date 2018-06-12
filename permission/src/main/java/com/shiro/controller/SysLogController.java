package com.shiro.controller;

import com.shiro.beans.PageQuery;
import com.shiro.beans.PageResult;
import com.shiro.common.JsonData;
import com.shiro.model.SysLog;
import com.shiro.model.SysLogWithBLOBs;
import com.shiro.parm.SearchParam;
import com.shiro.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/12 8:56
 */
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    @RequestMapping("/log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }

    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData search(SearchParam searchParam, PageQuery page) {
        PageResult<SysLogWithBLOBs> data = sysLogService.searchPage(searchParam, page);
        return JsonData.success(data);
    }

    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData recover(@RequestParam("id") int id) {
        sysLogService.recover(id);
        return JsonData.success();
    }



}
