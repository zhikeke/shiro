package com.shiro.common;


import com.shiro.exception.ParamException;
import com.shiro.exception.PermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/3 19:32
 */
@Slf4j
public class SpringExceptionHandler implements HandlerExceptionResolver {
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        String Default_Message = "Server Exception";
        ModelAndView mv;

        String requestUrl = httpServletRequest.getRequestURI().toString();

        // 要求项目中所有请求JSON的数据都使用.json 结尾
        if (requestUrl.endsWith(".json")) {
            log.info("unknow json exception, url=" + requestUrl, e);
            if (e instanceof PermissionException || e instanceof ParamException) {
                JsonData result = JsonData.fail(e.getMessage());
                mv = new ModelAndView("jsonView", result.toMap());
            } else {
                JsonData result = JsonData.fail(Default_Message);
                mv = new ModelAndView("jsonView", result.toMap());
            }
        } else if (requestUrl.endsWith(".page")) {   // 要求项目中所有请求page页面都使用.page 结尾
            log.info("unknow page exception, url=" + requestUrl, e);
            JsonData result = JsonData.fail(Default_Message);
            mv = new ModelAndView("exception", result.toMap());
        } else {
            log.info("unknow exception, url=" + requestUrl, e);
            JsonData result = JsonData.fail(Default_Message);
            mv = new ModelAndView("jsonView", result.toMap());
        }

     return mv;
    }
}
