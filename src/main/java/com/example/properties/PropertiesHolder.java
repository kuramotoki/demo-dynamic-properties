package com.example.properties;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class PropertiesHolder {

    @Getter
    private volatile Map<String, Object> properties;

    @PostConstruct
    public void init() {
        // dummy value for easiness.
        properties = Map.of("k1", "v1", "k2", "v2", "k3", "v3");
    }

    public void update(String key, Object value) {

        log.info("Start updating. Before:{}", properties);
        // clone
        var newProps = new HashMap<>(properties);
        // patch
        newProps.put(key, value);
        // replace
        properties = Collections.unmodifiableMap(newProps);
        log.info("Finish updating. After:{}", properties);
    }

    public void updateAll(Map<String, String> configMapData) {
        log.info("Start updating. Before:{}", properties);
        // clone
        var newProps = new HashMap<>(properties);
        // patch
        newProps.putAll(configMapData);
        // replace
        properties = Collections.unmodifiableMap(newProps);
        log.info("Finish updating. After:{}", properties);
    }
}
