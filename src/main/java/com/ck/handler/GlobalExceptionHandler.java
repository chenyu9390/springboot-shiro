package com.ck.handler;

import com.ck.bean.ResponseResult;
import com.ck.bean.ResponseStatus;
import com.ck.util.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ck
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UnauthorizedException.class)
    public String handleUnauthorizedException(UnauthorizedException e) throws JsonProcessingException {
        log.error("UnauthorizedException, {}", e.getMessage());
        return JSONUtil.writeValue(new ResponseResult(ResponseStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public String handleAuthorizationException(AuthorizationException e) throws JsonProcessingException {
        log.error("AuthorizationException, {}", e.getMessage());
        return JSONUtil.writeValue(new ResponseResult(ResponseStatus.UNAUTHORIZED));
    }
}
