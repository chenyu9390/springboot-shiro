package com.ck.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.ck.bean.em.ShiroFilterEnum;
import com.ck.filter.LogoutAuthorizationFilter;
import com.ck.filter.ShiroFormAuthenticationFilter;
import com.ck.filter.loginFormAuthenticationFilter;
import com.ck.properties.ShiroProperties;
import com.ck.shiro.ShiroRealm;
import com.ck.shiro.ShiroSessionListener;
import com.ck.util.StringValueUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.Filter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Configuration
public class ShiroConfig {

    @Autowired
    private ShiroProperties shiroProperties;

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.database}")
    private int database;

    /**
     * shiro 中配置 redis 缓存
     *
     * @return RedisManager
     */
    private RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host + ":" + port);
        if (!StringUtils.isEmpty(password)) {
            redisManager.setPassword(password);
        }
        redisManager.setTimeout(timeout);
        redisManager.setDatabase(database);
        return redisManager;
    }

    private RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        // 必须要设置主键名称，shiro-redis 插件用过这个缓存用户信息
        redisCacheManager.setPrincipalIdFieldName("userId");
        return redisCacheManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        Map<String, Filter> filters=new HashMap<>();
        filters.put("user", new loginFormAuthenticationFilter());
        filters.put("authc", new ShiroFormAuthenticationFilter());
        filters.put("logout", new LogoutAuthorizationFilter());
        shiroFilter.setFilters(filters);
        /**
         * 权限不足时的跳转，重写filter后可以直接返回错误信息
         * 路径对应controll中的路径
         * shiroFilter.setLoginUrl("/shiro/un_auth");
         * shiroFilter.setUnauthorizedUrl("/shiro/unauthorized");
         */
        Map<String, String> filterMap = new LinkedHashMap<>();
        //自定义权限拦截
        if(ObjectUtils.isNotEmpty(shiroProperties.getFilterMap())){
            filterMap.putAll(shiroProperties.getFilterMap());
        }
        filterMap.put("/logout", ShiroFilterEnum.LOGOUT.getName());
        filterMap.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    /**
     *  配置 密码认证器
     *  1.可以使用 Shiro提供的 密码认证器
     *  2. 自定义密码认证器
     *  不配置会抛出异常：org.apache.shiro.crypto.CryptoException: Unable to execute 'doFinal' with cipher instance [javax.crypto.Cipher@48c22c79].
     * @return
     */
    @Bean
    public SimpleCredentialsMatcher HashedCredentialsMatcher() {
        SimpleCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher("SHA-256");
        return credentialsMatcher;
    }

    /**
     *  shiro的核心，所有的授权认证都在这里处理。
     *  此处使用自定义的认证器
     * @param credentialsMatcher
     * @return
     */
    @Bean
    public ShiroRealm customRealm(SimpleCredentialsMatcher credentialsMatcher) {
        ShiroRealm realm = new ShiroRealm();
        // 配置 密码认证器，用于匹配密码
        realm.setCredentialsMatcher(credentialsMatcher);
        return realm;
    }

    /**
     * Shiro核心处理类，所有的类都围绕着这个处理器进行执行
     * @param shiroRealm
     * @return
     */
    @Bean
    public SecurityManager securityManager(ShiroRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置 shiro session管理器
        securityManager.setSessionManager(sessionManager());
        // 配置 缓存管理类 cacheManager
        securityManager.setCacheManager(cacheManager());
        // 配置 rememberMeCookie
        securityManager.setRememberMeManager(rememberMeManager());
        // 配置 SecurityManager，并注入 shiroRealm
        securityManager.setRealm(shiroRealm);
        return securityManager;
    }

    /**
     * rememberMe cookie 效果是重开浏览器后无需重新登录
     *
     * @return SimpleCookie
     */
    private SimpleCookie rememberMeCookie() {
        // 设置 cookie 名称，对应 login.html 页面的 <input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置 cookie 的过期时间，单位为秒，这里为一天
        cookie.setMaxAge(shiroProperties.getCookieTimeout());
        return cookie;
    }

    /**
     * cookie管理对象
     *
     * @return CookieRememberMeManager
     */
    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie 加密的密钥
        String encryptKey = "febs_shiro_key";
        byte[] encryptKeyBytes = encryptKey.getBytes(StandardCharsets.UTF_8);
        String rememberKey = Base64Utils.encodeToString(Arrays.copyOf(encryptKeyBytes, 16));
        cookieRememberMeManager.setCipherKey(Base64.decode(rememberKey));
        return cookieRememberMeManager;
    }

    /**
     * 用于开启 Thymeleaf 中的 shiro 标签的使用
     *
     * @return ShiroDialect shiro 方言对象
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * session 管理对象
     *
     * @return DefaultWebSessionManager
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<>();
        listeners.add(new ShiroSessionListener());
        // 设置 session超时时间
        sessionManager.setGlobalSessionTimeout(shiroProperties.getSessionTimeout()*1000L);
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionDAO(redisSessionDAO());
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Session ID 生成器
     * create by: ck
     * @return JavaUuidSessionIdGenerator
     */
    @Bean
    public JavaUuidSessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }




    /**
     * 未授权跳转url
     * shiro中如果使用注释来注入角色和权限的话，无法抛出UnauthorizedException的异常
     * 需要添加以下内容，当出现异常时，跳转至error/403.html。
     * 如果程序配置了全局异常拦截并捕获了UnauthorizedException,则此处配置无效
     * @return
     */
    /*@Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
        SimpleMappingExceptionResolver resolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
        //未授权的网页跳转至/error/403.html
        properties.setProperty("org.apache.shiro.authz.UnauthorizedException", shiroProperties.getUnauthorizedUrl());
        resolver.setExceptionMappings(properties);
        return resolver;
    }*/
}
