package com.example.finalgaz.proc;

import com.example.finalgaz.dto.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class BuilderProc implements Processor {
    String group = "https://api.vk.com/method/groups.isMember?bridgeEndpoint=true";
    String user = "https://api.vk.com/method/users.get?bridgeEndpoint=true";
    String vkToken = "vk_service_token";

    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getMessage();
        List<String> list = new ArrayList<>();
        Request param = objectMapper.readValue(message.getBody(String.class), Request.class);


        list.add(
                String.format(
                        group + "&user_id=%s&group_id=%s&access_token=%s&v=5.131",
                        param.getUser_id(),
                        param.getGroup_id(),
                        exchange.getIn().getHeader(vkToken)
                )
        );
        list.add(
                String.format(
                        user + "&user_id=%s&access_token=%s&v=5.131",
                        param.getUser_id(),
                        exchange.getIn().getHeader(vkToken)
                )
        );

        exchange.getIn().setBody(list);
     }
}
