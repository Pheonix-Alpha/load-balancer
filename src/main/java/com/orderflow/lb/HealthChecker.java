package com.orderflow.lb;

import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HealthChecker {

    private final LoadBalancerService loadBalancerService;

    private final RestClient restClient = RestClient.create();

    public HealthChecker(LoadBalancerService loadBalancerService) {
        this.loadBalancerService = loadBalancerService;
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void checkServers() {

        System.out.println(
                LocalTime.now()
                        + " ========== HEALTH CHECK =========="
        );

        List<BackendServer> servers =
                loadBalancerService.getServers();

        for (BackendServer server : servers) {

            boolean wasHealthy = server.isHealthy();

            try {

                restClient
                        .get()
                        .uri(server.getUrl() + "/actuator/health")
                        .retrieve()
                        .toBodilessEntity();

                server.setHealthy(true);

                if (!wasHealthy) {

                    System.out.println(
                            LocalTime.now()
                                    + " Server RECOVERED → "
                                    + server.getUrl()
                    );
                }

            } catch (Exception e) {

                server.setHealthy(false);

                if (wasHealthy) {

                    System.out.println(
                            LocalTime.now()
                                    + " Server DOWN → "
                                    + server.getUrl()
                    );
                }
            }
        }
    }
}