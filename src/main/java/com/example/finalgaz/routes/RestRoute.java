package com.example.finalgaz.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RestRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .contextPath("/api")
                .host("localhost")
                .port("{{server.port}}")
                .corsHeaderProperty("Access-Control-Allow-Headers", "Stub, Content-type, Accept, Authorization")
                .corsHeaderProperty("Access-Control-Allow-Origin", "*")
                .corsHeaderProperty("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS")
                .apiContextPath("/api-docs")
                .apiProperty("api.version", "1.0");
    }
}
