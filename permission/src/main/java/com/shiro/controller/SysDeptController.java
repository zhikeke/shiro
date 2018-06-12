package com.shiro.controller;

import com.shiro.common.JsonData;
import com.shiro.dto.DeptLevelDTO;
import com.shiro.parm.DeptParam;
import com.shiro.service.SysDeptService;
import com.shiro.service.SysTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 15:09
 */
@Controller
@RequestMapping("/sys/dept")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private SysTreeService sysTreeService;

    @RequestMapping("/dept.page")
    public ModelAndView deptPage() {
        return new ModelAndView("dept");
    }


    /**
     * 添加部门
     * @param deptParam 部门参数
     * @return
     */
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(DeptParam deptParam) {
        sysDeptService.save(deptParam);
        return JsonData.success();
    }

    /**
     * 获取部门树
     * @return
     */
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {
        List<DeptLevelDTO> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }

    /**
     * 修改部门
     * @param deptParam 部门参数
     * @return
     */
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(DeptParam deptParam) {
        sysDeptService.update(deptParam);
        return JsonData.success();
    }


    /**
     * 删除部门
     * @param id 部门id
     * @return
     */
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData deleteDept(@RequestParam("id") Integer id) {
        sysDeptService.delete(id);
        return JsonData.success();
    }

}
