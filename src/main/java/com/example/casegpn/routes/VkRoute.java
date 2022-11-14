package com.example.casegpn.routes;


import com.example.casegpn.aggregators.VkResponseAggregator;
import com.example.casegpn.dto.VkRequest;
import com.example.casegpn.dto.VkResponse;
import com.example.casegpn.proc.RequestBuildersProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@Component
@AllArgsConstructor
@Slf4j
public class VkRoute extends RouteBuilder {
    RequestBuildersProcessor requestBuildersProcessor;

    VkResponseAggregator vkResponseAggregator;

    @Override
    public void configure() {
        rest().tag("vk service")
                .id("api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)

                .post("/vk")
                .type(VkRequest.class)
                .outType(VkResponse.class)
                .bindingMode(RestBindingMode.auto)
                .param().name("vk_service_token").required(true).type(RestParamType.header).endParam()
                .param().name("body").required(true).type(RestParamType.body).endParam()
                .responseMessage().code(OK.value()).message(OK.getReasonPhrase()).endResponseMessage()
                .responseMessage().code(BAD_REQUEST.value()).message(BAD_REQUEST.getReasonPhrase()).endResponseMessage()
                .to("direct:vk");

        from("direct:vk")
                .streamCaching()
                .process(requestBuildersProcessor)
                .split(body(), vkResponseAggregator).parallelProcessing()
                .removeHeader(Exchange.COOKIE_HANDLER)
                .toD("${body}")
                .unmarshal().json()
                .end()
                .unmarshal().json()
                .log(LoggingLevel.INFO, body().toString());
    }
}
