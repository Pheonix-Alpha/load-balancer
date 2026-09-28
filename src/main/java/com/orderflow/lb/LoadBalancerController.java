package com.orderflow.lb;

import java.net.URI;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class LoadBalancerController {

        private final LoadBalancerService loadBalancerService;

        private final RestClient restClient = RestClient.create();

        public LoadBalancerController(
                        LoadBalancerService loadBalancerService) {

                this.loadBalancerService = loadBalancerService;
        }

        private boolean isRetryable(String method) {

                return method.equalsIgnoreCase("GET")
                                || method.equalsIgnoreCase("HEAD")
                                || method.equalsIgnoreCase("OPTIONS");
        }

        @RequestMapping("/proxy/**")
        public ResponseEntity<String> forwardRequest(
                        HttpServletRequest request,
                        @RequestBody(required = false) String body,
                        @RequestHeader HttpHeaders incomingHeaders) {

                BackendServer server = loadBalancerService.getNextServer();

                try {

                        return forwardToServer(
                                        server,
                                        request,
                                        body,
                                        incomingHeaders);

                } catch (Exception e) {

                        System.out.println(
                                        "Request failed on → "
                                                        + server.getUrl());

                        if (!isRetryable(request.getMethod())) {

                                System.out.println(
                                                "Not retrying "
                                                                + request.getMethod()
                                                                + " request");

                                throw e;
                        }

                        BackendServer retryServer = loadBalancerService.getRetryServer(server);

                        System.out.println(
                                        "Retrying request on → "
                                                        + retryServer.getUrl());

                        return forwardToServer(
                                        retryServer,
                                        request,
                                        body,
                                        incomingHeaders);
                }
        }

        private ResponseEntity<String> forwardToServer(
                        BackendServer server,
                        HttpServletRequest request,
                        String body,
                        HttpHeaders incomingHeaders) {

                String path = request.getRequestURI();

                String backendPath = path.substring("/proxy".length());

                String queryString = request.getQueryString();

                String targetUrl = server.getUrl() + backendPath;

                if (queryString != null &&
                                !queryString.isEmpty()) {

                        targetUrl += "?" + queryString;
                }

                System.out.println(
                                request.getMethod()
                                                + " "
                                                + path
                                                + " → "
                                                + server.getUrl());

                HttpMethod method = HttpMethod.valueOf(request.getMethod());

                return restClient
                                .method(method)
                                .uri(URI.create(targetUrl))
                                .headers(headers -> {

                                        headers.addAll(incomingHeaders);

                                        headers.remove(
                                                        HttpHeaders.HOST);
                                })
                                .body(body == null ? "" : body)
                                .exchange((requestSpec, clientResponse) -> {

                                        String responseBody = clientResponse.bodyTo(String.class);

                                        return ResponseEntity
                                                        .status(
                                                                        clientResponse
                                                                                        .getStatusCode())
                                                        .headers(
                                                                        clientResponse
                                                                                        .getHeaders())
                                                        .body(responseBody);
                                });
        }
}