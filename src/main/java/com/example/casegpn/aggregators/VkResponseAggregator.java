package com.example.casegpn.aggregators;

import com.example.casegpn.dto.VkResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Component
public class VkResponseAggregator implements AggregationStrategy {
    ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        VkResponse vkResponse = new VkResponse();
        if (oldExchange != null) {
            vkResponse = objectMapper.readValue(oldExchange.getIn().getBody(String.class), VkResponse.class);
        }

        Object response = newExchange.getMessage().getBody();

        if (response instanceof LinkedHashMap<?, ?> linkedHashMap) {
            for (Object o : linkedHashMap.keySet()) {
                Object type = linkedHashMap.get(o);
                //Обработка ошибок
                if (type instanceof LinkedHashMap<?, ?> errorsList) {
                    for (Object err : errorsList.keySet()) {
                        vkResponse.getErrors().put(err.toString(), errorsList.get(err));
                    }
                }
                if (type instanceof Integer integer) {
                    vkResponse.setMember(integer == 1);
                }
                if (type instanceof ArrayList<?> arrayList) {
                    for (Object innerArrayListObjectType : arrayList) {
                        if (innerArrayListObjectType instanceof LinkedHashMap<?, ?> innerLinkedHashMap) {
                            for (Object key : innerLinkedHashMap.keySet()) {
                                if (key.equals("first_name")) {
                                    vkResponse.setFirstName(innerLinkedHashMap.get(key).toString());
                                }
                                if (key.equals("last_name")) {
                                    vkResponse.setLastName(innerLinkedHashMap.get(key).toString());
                                }
                                if (key.equals("nickname")) {
                                    vkResponse.setMiddleName(innerLinkedHashMap.get(key).toString());
                                }
                            }
                        }
                    }
                }
            }
        }

        if (oldExchange == null) {
            newExchange.getMessage().setBody(objectMapper.writeValueAsString(vkResponse));
            return newExchange;
        }

        oldExchange.getMessage().setBody(objectMapper.writeValueAsString(vkResponse));
        return oldExchange;
    }
}
