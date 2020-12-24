package com.example.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Slf4j
public class PropertyAccessor {

    @Autowired
    private PropertiesHolder holder;

    private Map<String, Object> properties;

    @PostConstruct
    public void init() {
        properties = holder.getProperties();
    }

    public Object get(String key) {
        log.info("properties instance hash: {}", properties.hashCode());
        return properties.get(key);
    }
}
