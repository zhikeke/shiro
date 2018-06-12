package com.shiro.filter;


import com.shiro.common.RequestHolder;
import com.shiro.model.SysUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 8:39
 */
@Slf4j
public class LoginFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        SysUser user = (SysUser)request.getSession().getAttribute("user");
        if (user == null) {
            String path = "/login.jsp";
            response.sendRedirect(path);
            return;
        }

        RequestHolder.add(user);
        RequestHolder.add(request);
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    public void destroy() {

    }
}
