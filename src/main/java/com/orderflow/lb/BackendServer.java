package com.orderflow.lb;

public class BackendServer {

    private final String url;
    private boolean healthy;

    public BackendServer(String url) {
        this.url = url;
        this.healthy = true;
    }

    public String getUrl() {
        return url;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }
}