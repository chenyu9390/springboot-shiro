package com.ck.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author ck
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private long sessionTimeout;
    private int cookieTimeout;
    private String anonUrl;
    private String unauthorizedUrl;
    private Map<String,String> filterMap;
}
