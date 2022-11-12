package com.example.finalgaz.agregator;

import com.example.finalgaz.dto.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.Message;

public class ResponseAgregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Response response = new Response();
        ObjectMapper objectMapper = new ObjectMapper();
//        if (oldExchange != null){
//            try {
//                response = objectMapper.readValue(oldExchange.getIn().getBody(String.class),Response.class);
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        }else return newExchange;
//
//        return oldExchange;
        if (oldExchange != null){
            try {
                response = objectMapper.readValue(oldExchange.getIn().getBody(String.class),Response.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        if (oldExchange == null){
            try {
                newExchange.getMessage().setBody(objectMapper.writeValueAsString(response));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return newExchange;
        }
        try {
            oldExchange.getMessage().setBody(objectMapper.writeValueAsString(response));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return oldExchange;
    }
}
