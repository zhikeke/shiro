package com.shiro.filter;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.shiro.common.ApplicationContentHelper;
import com.shiro.common.JsonData;
import com.shiro.common.RequestHolder;
import com.shiro.model.SysUser;
import com.shiro.service.SysCoreService;
import com.shiro.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/11 13:21
 */
@Slf4j
public class AclControllerFilter implements Filter {
    private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();

    private final static String noAuthUrl = "/sys/user/noAuth.page";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
         String exclusionUrls = filterConfig.getInitParameter("exlusionUrls");
         List<String> exclusionUrlList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
         exclusionUrlSet = Sets.newHashSet(exclusionUrlList);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String servletPath = request.getServletPath();
        Map requestMap = request.getParameterMap();

        if (exclusionUrlSet.contains(servletPath)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser == null) {
            log.info("someone visit {}, but no login, request parameter:{}", servletPath, JsonMapper.obj2String(requestMap));
            noAuth(request, response);
            return;
        }

        SysCoreService sysCoreService = ApplicationContentHelper.popBean(SysCoreService.class);
        if (! sysCoreService.hasUrlAcl(servletPath)) {
            log.info("{} visit {},, but no authority, request parameter:{}", RequestHolder.getCurrentUser().getUsername(), servletPath, JsonMapper.obj2String(requestMap));
            noAuth(request, response);
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
        return;
    }

    private static void noAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
          String servletPath = request.getServletPath();
          if (servletPath.endsWith(".json")) {
              JsonData jsonData = JsonData.fail("没有权限访问，如需访问，请联系管理员!!!");
              response.setHeader("Content-Type", "application/json");
              response.getWriter().print(JsonMapper.obj2String(jsonData));
          } else {
              clientRedirect(noAuthUrl, response);
              return;
          }
    }

    private static void clientRedirect(String url, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "text/html");
        response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
    }


    @Override
    public void destroy() {

    }
}
