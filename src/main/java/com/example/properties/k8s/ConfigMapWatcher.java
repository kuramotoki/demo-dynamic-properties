package com.example.properties.k8s;

import com.example.properties.PropertiesHolder;
import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watch;
import io.fabric8.kubernetes.client.Watcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;

@Slf4j
public class ConfigMapWatcher {

    @Autowired
    KubernetesClient client;

    @Autowired
    PropertiesHolder propertiesHolder;

    private Watch watch;

    private final String namespace = "default";

    private final String configMapName = "config-test";

    @PostConstruct
    public void init() {
        // watch
        watch = client.configMaps().inNamespace(namespace).withName(configMapName).watch(new Watcher<>() {
            @Override
            public void eventReceived(Watcher.Action action, ConfigMap resource) {
                log.info("Event Received. action: {} , resource: {}", action.name(), resource.getMetadata().getName());
                switch (action) {
                    case ADDED:
                        log.info("{} got added", resource.getMetadata().getName());
                        log.info("ConfigMap: {}", resource.getData());

                        configMapDataToProperties(resource.getData());

                        break;
                    case DELETED:
                        log.info("{} got deleted", resource.getMetadata().getName());
                        log.info("ConfigMap: {}", resource.getData());

                        configMapDataToProperties(resource.getData());

                        break;
                    case MODIFIED:
                        log.info("{} got modified", resource.getMetadata().getName());
                        log.info("ConfigMap: {}", resource.getData());

                        configMapDataToProperties(resource.getData());

                        break;
                    default:
                        log.error("Unrecognized event: {}", action.name());
                }
            }

            @Override
            public void onClose(KubernetesClientException cause) {
                log.info("Closed");
            }
        });
    }

    private void configMapDataToProperties(Map<String, String> configMapData) {
        propertiesHolder.updateAll(configMapData);
    }

    @PreDestroy
    public void destory() {
        watch.close();
    }

}
