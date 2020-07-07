package com.ck.filter;

import com.ck.bean.ResponseResult;
import com.ck.bean.ResponseStatus;
import com.ck.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Slf4j
public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
//        super.redirectToLogin(request, response);
        response.setContentType("application/json; charset=utf-8");
        //返回json
        response.getWriter().write(JSONUtil.writeValue(new ResponseResult(ResponseStatus.NEED_LOGIN)));
    }


}
