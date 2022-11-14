package com.example.finalgaz.agregator;

import com.example.finalgaz.dto.MemberResponse;
import com.example.finalgaz.dto.Response;
import com.example.finalgaz.dto.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ResponseAgregator implements AggregationStrategy {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Response vkresponse = new Response();

        if (oldExchange != null){
            vkresponse = oldExchange.getIn().getBody(Response.class);
        }
        Object response = newExchange.getMessage().getBody();

        if (response instanceof LinkedHashMap<?, ?> linkedHashMap) {
            for (Object o : linkedHashMap.keySet()) {
                Object type = linkedHashMap.get(o);
                //Обработка ошибок
                if (type instanceof LinkedHashMap<?, ?> errorsList) {
                    for (Object err : errorsList.keySet()) {
                        vkresponse.getErrors().put(err.toString(), errorsList.get(err));
                    }
                }
                if (type instanceof Integer integer) {
                    vkresponse.setMember(integer == 1);
                }
                if (type instanceof ArrayList<?> arrayList) {
                    for (Object innerArrayListObjectType : arrayList) {
                        if (innerArrayListObjectType instanceof LinkedHashMap<?, ?> innerLinkedHashMap) {
                            for (Object key : innerLinkedHashMap.keySet()) {
                                if (key.equals("first_name")) {
                                    vkresponse.setFirstName(innerLinkedHashMap.get(key).toString());
                                }
                                if (key.equals("last_name")) {
                                    vkresponse.setLastName(innerLinkedHashMap.get(key).toString());
                                }
                                if (key.equals("nickname")) {
                                    vkresponse.setMiddleName(innerLinkedHashMap.get(key).toString());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (oldExchange == null) {
            newExchange.getMessage().setBody(objectMapper.writeValueAsString(vkresponse));
            return newExchange;
        }

        oldExchange.getMessage().setBody(objectMapper.writeValueAsString(vkresponse));
        return oldExchange;
    }
}
