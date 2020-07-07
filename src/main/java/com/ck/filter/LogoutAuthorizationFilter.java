package com.ck.filter;

import com.ck.bean.ResponseResult;
import com.ck.bean.ResponseStatus;
import com.ck.util.JSONUtil;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class LogoutAuthorizationFilter extends LogoutFilter {

    @Override
    public void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) throws Exception {
        response.setContentType("application/json; charset=utf-8");
        //返回json
        response.getWriter().write(JSONUtil.writeValue(new ResponseResult(ResponseStatus.LOGIN_OUT)));
    }
}
