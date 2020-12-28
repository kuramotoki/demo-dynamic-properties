package k8s;

import io.fabric8.kubernetes.api.model.ConfigMap;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientException;
import io.fabric8.kubernetes.client.Watcher;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class WatchTest {

    public static void main(String[] args) throws InterruptedException {

        try (final KubernetesClient client = new DefaultKubernetesClient()) {
            // read
            var cm = client.configMaps().inNamespace("default").withName("config-test").get();
            log.info("ConfigMap: {}", cm);

            // watch
            var watch = client.configMaps().inNamespace("default").withName("config-test").watch(new Watcher<ConfigMap>() {
                @Override
                public void eventReceived(Action action, ConfigMap resource) {
                    log.info("Event Received. action: {} , resource: {}", action.name(), resource.getMetadata().getName());
                    switch (action) {
                        case ADDED:
                            log.info("{} got added", resource.getMetadata().getName());
                            log.info("ConfigMap: {}", resource.getData());
                            break;
                        case DELETED:
                            log.info("{} got deleted", resource.getMetadata().getName());
                            log.info("ConfigMap: {}", resource.getData());
                            break;
                        case MODIFIED:
                            log.info("{} got modified", resource.getMetadata().getName());
                            log.info("ConfigMap: {}", resource.getData());
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

            TimeUnit.MINUTES.sleep(1);
            log.info("watch close.");
            watch.close();
        }
    }
}
