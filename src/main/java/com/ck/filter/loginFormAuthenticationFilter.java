package com.ck.filter;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class loginFormAuthenticationFilter extends FormAuthenticationFilter {

    /**
     * 因为发现设置的successUrl没生效，所以追踪源码发现如果SavedRequest对象不为null,则它会覆盖掉我们设置
     * 的successUrl，所以我们要重写onLoginSuccess方法，在它覆盖掉我们设置的successUrl之前，去除掉
     * SavedRequest对象,SavedRequest对象的获取方式为：
     * savedRequest = (SavedRequest) session.getAttribute(SAVED_REQUEST_KEY);
     * public static final String SAVED_REQUEST_KEY = "shiroSavedRequest";
     * 解决方案：从session对象中移出shiroSavedRequest
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
                                     ServletResponse response) throws Exception {

        String successUlr = "/index";
        WebUtils.issueRedirect(request,response,successUlr);
        return super.onLoginSuccess(token, subject, request, response);
    }

    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, "error", null, true);
    }

}
