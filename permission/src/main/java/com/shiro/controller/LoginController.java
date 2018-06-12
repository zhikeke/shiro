package com.shiro.controller;

import com.shiro.beans.Mail;
import com.shiro.exception.ParamException;
import com.shiro.model.SysUser;
import com.shiro.service.SysUserService;
import com.shiro.util.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/6 14:20
 */
@Controller
public class LoginController {

    @Autowired
    private SysUserService sysUserService;


    @RequestMapping("/logout.page")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        String path = "/login.jsp";
        response.sendRedirect(path);
    }


    /**
     * 用户登录
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
         String username = request.getParameter("username");
         String password = request.getParameter("password");
         String ret = request.getParameter("ret");
         String err = "";

         if (StringUtils.isBlank(username)) {
             throw new  ParamException("用户名不能为空!!!");
         }

         if (StringUtils.isBlank(password)) {
             throw new ParamException("密码不能为空!!!");
         }

        SysUser user = sysUserService.getUserByName(username);

         if (user == null) {
             err = "查询不到指定用户";
         } else if (!user.getPassword().equals(password)) {
             err = "用户密码错误";
         } else if (user.getStatus() != 1) {
             err = "该用户已被冻结，请联系管理员";
         } else {
             request.getSession().setAttribute("user", user);
             if (StringUtils.isNotBlank(ret)) {
                 response.sendRedirect(ret);
             } else {
//                 // 发送邮件
//                 Set<String> receivers = new HashSet<String>();
//                 receivers.add(user.getMail());
//                 Mail mail = Mail.builder().subject("科科达有限公司")
//                         .message("欢迎登陆本系统哦!!!")
//                         .receivers(receivers).build();
//                 MailUtil.send(mail);
                 response.sendRedirect("/admin/admin.page");
                 return;
             }
         }

         request.setAttribute("err", err);
         request.setAttribute("username", username);
         if (StringUtils.isNotBlank(ret)) {
             request.setAttribute("ret", ret);
         }
         String path = "/login.jsp";
         request.getRequestDispatcher(path).forward(request, response);

    }


}
