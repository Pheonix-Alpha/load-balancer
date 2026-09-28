package com.orderflow.lb;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class LoadBalancerService {

    private final List<BackendServer> servers = List.of(
            new BackendServer("http://localhost:8080"),
            new BackendServer("http://localhost:8082"));

    private final AtomicInteger currentServer = new AtomicInteger(0);

    public BackendServer getNextServer() {

        for (int i = 0; i < servers.size(); i++) {

            int index = currentServer.getAndUpdate(
                    value -> (value + 1) % servers.size());

            BackendServer server = servers.get(index);

            if (server.isHealthy()) {
                return server;
            }
        }

        throw new RuntimeException("No healthy servers available");
    }

    public BackendServer getRetryServer(BackendServer failedServer) {

        for (BackendServer server : servers) {

            if (server != failedServer && server.isHealthy()) {
                return server;
            }
        }

        throw new RuntimeException(
                "No other healthy server available");
    }

    public List<BackendServer> getServers() {
        return servers;
    }

    public void markUnhealthy(BackendServer server) {
        server.setHealthy(false);

        System.out.println(
                "Marked server unhealthy → " + server.getUrl());
    }
}