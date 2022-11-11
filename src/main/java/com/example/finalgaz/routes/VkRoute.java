package com.example.finalgaz.routes;

import com.example.finalgaz.dto.MemberResponse;
import com.example.finalgaz.dto.Request;
import com.example.finalgaz.dto.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

import static org.apache.camel.Exchange.HTTP_QUERY;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Component
@Slf4j
public class VkRoute extends RouteBuilder {
    ObjectMapper objectMapper = new ObjectMapper();
    Response vkResponse = new Response();

    MemberResponse vkMemberResponse = new MemberResponse();
    MemberResponse vkMemberResponse2 = new MemberResponse();


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
//        from("direct:vk")
//                .process(exchange -> {
//                    Message message = exchange.getIn();
//                    Request vkRequest1 = objectMapper.readValue(message.getBody(String.class), Request.class);
//                    message.setHeader(HTTP_QUERY,
//                            String.format("user_id=%s&access_token=%s&v=5.131",
//                                    vkRequest1.getUser_id(),
//                                    message.getHeader("vk_service_token")
//                            )
//                    );
//                })
//                .to("https://api.vk.com/method/users.group?bridgeEndpoint=true")
//                        .process(exchange -> {
//                            Message message = exchange.getIn();
//
//                        })

        from("direct:vk")
                .process(exchange -> {
                    Message message = exchange.getIn();
                    Request vkRequest = objectMapper.readValue(message.getBody(String.class), Request.class);
                    message.setHeader(HTTP_QUERY,
                            String.format("user_id=%s&group_id=%s&access_token=%s&v=5.131",
                                    vkRequest.getUser_id(),
                                    vkRequest.getGroup_id(),
                                    message.getHeader("vk_service_token")
                            )
                    );
                })
                .to("https://api.vk.com/method/groups.isMember?bridgeEndpoint=true")

                .process(exchange -> {
                    Message message = exchange.getIn();
                    MemberResponse resp = objectMapper.readValue(message.getBody(String.class), MemberResponse.class);
                    vkResponse.setMember((resp.getResponse(

                    )));

                })
                .log(LoggingLevel.INFO, String.valueOf(vkResponse.getMember()));

    }
}
