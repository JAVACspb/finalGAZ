package com.example.finalgaz.routes;

import com.example.finalgaz.agregator.ResponseAgregator;
import com.example.finalgaz.dto.MemberResponse;
import com.example.finalgaz.dto.Request;
import com.example.finalgaz.dto.Response;
import com.example.finalgaz.proc.BuilderProc;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.apache.camel.Exchange.HTTP_QUERY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Component
@Slf4j
public class VkRoute extends RouteBuilder {

    BuilderProc builderProc;

    ResponseAgregator responseAgregator;


    @Override
    public void configure() {


        rest().tag("vk service")
                .id("api")
                .produces(MediaType.APPLICATION_JSON)
                .consumes(MediaType.APPLICATION_JSON)

                .post("/vk")
                .type(Request.class)
                .outType(Request.class)
                .param().name("vk_service_token").required(true).type(RestParamType.header).endParam()
                .param().name("body").required(true).type(RestParamType.body).endParam()
                .responseMessage().code(OK.value()).message(OK.getReasonPhrase()).endResponseMessage()
                .responseMessage().code(BAD_REQUEST.value()).message(BAD_REQUEST.getReasonPhrase()).endResponseMessage()
                .to("direct:vk");

        from("direct:vk")
                .streamCaching()
                .process(builderProc)
                .split(body(),responseAgregator).parallelProcessing()
                .removeHeader(Exchange.COOKIE_HANDLER)
                    .toD("${body}")
                    .unmarshal().json()
                .end()
                .unmarshal().json()
                .log(LoggingLevel.INFO, body().toString());

    }
}
