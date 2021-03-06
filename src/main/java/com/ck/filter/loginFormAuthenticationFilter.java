package com.ck.filter;

import com.ck.bean.ResponseResult;
import com.ck.bean.ResponseStatus;
import com.ck.util.JSONUtil;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class loginFormAuthenticationFilter extends UserFilter {

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
//        super.redirectToLogin(request, response);
        response.setContentType("application/json; charset=utf-8");
        //返回json
        response.getWriter().write(JSONUtil.writeValue(new ResponseResult(ResponseStatus.NEED_LOGIN)));
    }
}
