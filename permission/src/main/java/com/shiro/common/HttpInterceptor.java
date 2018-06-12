package com.shiro.common;

import com.shiro.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 14:15
 */
@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURI().toString();
        Map requestParam = request.getParameterMap();

        log.info("request start--》requestUrl:{}, requestParam:{}", requestUrl, JsonMapper.obj2String(requestParam));

        return true;
}

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String requestUrl = request.getRequestURI().toString();
//        Map requestParam = request.getParameterMap();
//
//        log.info("request handle--》requestUrl:{}, requestParam:{}", requestUrl, JsonMapper.obj2String(requestParam));requestParam
        RequestHolder.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestUrl = request.getRequestURI().toString();
        Map requestParam = request.getParameterMap();

        log.info("request after--》requestUrl:{}, requestParam:{}", requestUrl, JsonMapper.obj2String(requestParam));
        RequestHolder.remove();
    }
}
