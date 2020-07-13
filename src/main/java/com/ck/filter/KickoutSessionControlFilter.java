package com.ck.filter;

import com.ck.bean.ResponseResult;
import com.ck.bean.ResponseStatus;
import com.ck.domain.entity.UserEntity;
import com.ck.util.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.thymeleaf.expression.Maps;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
public class KickoutSessionControlFilter extends AccessControlFilter {
    private String kickoutUrl; //踢出后到的地址
    private boolean kickoutAfter = false; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession = 1; //同一个帐号最大会话数 默认1

    /**
     * 用户account, SessionId
     */
    public static Map<String, Serializable> loginSessionCache = new ConcurrentHashMap<>();

    /**
     * session map
     * true: 踢出
     * false: 未踢出
     */
    public static Map<Serializable, Boolean> sessionStatusMap = new ConcurrentHashMap<>();

    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;

    public void setKickoutUrl(String kickoutUrl) {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter) {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    //设置Cache的key的前缀
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_redis_cache");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        //如果是相关目录或者是如果没有登录, 就直接return true
        if ((!subject.isAuthenticated() && !subject.isRemembered())) {
            return Boolean.TRUE;
        }
        // 获取当前登录的用户的相关信息
        Session session = subject.getSession();
        Serializable sessionId = session.getId();
        //  判断是否已经踢出
        Boolean kickout = sessionStatusMap.get(sessionId);
        if (null != kickout && kickout) {
            // 移除该session数据
            sessionStatusMap.remove(sessionId);
            return Boolean.FALSE;
        }
        // 获取mobileId
        String userName = (String) subject.getPrincipal();
        if (loginSessionCache.containsKey(userName)) {
            // 如果已经包含当前Session，并且是同一个用户，跳过。
            if (loginSessionCache.containsValue(sessionId)) {
                return Boolean.TRUE;
            }
            /*
             * 如果用户Id相同, Session值不相同
             * 1.获取到原来的session，并且标记为踢出。
             * 2.继续走
             */
            Serializable oldSessionId = loginSessionCache.get(userName);
            sessionStatusMap.put(oldSessionId, Boolean.TRUE);
            log.info("用户 姓名: {} 登陆, 当前sessionId: {}, 踢出 session id: {}",
                    userName, sessionId, oldSessionId);
        }
        loginSessionCache.put(userName, sessionId);
        // 当前session标记为未被踢出
        sessionStatusMap.put(sessionId, Boolean.FALSE);
        return Boolean.TRUE;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        // 先退出该用户
        subject.logout();
        response.setContentType("application/json; charset=utf-8");
        //返回json
        response.getWriter().write(JSONUtil.writeValue(new ResponseResult(ResponseStatus.REPEAT_LOGIN)));
        return false;
    }
}
