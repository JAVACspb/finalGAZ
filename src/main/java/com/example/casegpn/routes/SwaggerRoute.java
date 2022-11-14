package com.example.casegpn.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SwaggerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // Конфигурация REST и Swagger-UI
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
