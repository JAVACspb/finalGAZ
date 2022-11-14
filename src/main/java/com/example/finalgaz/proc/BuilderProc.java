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
    String group_request = "https://api.vk.com/method/groups.isMember?bridgeEndpoint=true";
    String user_request = "https://api.vk.com/method/users.get?bridgeEndpoint=true";
    String vkToken_header = "vk_service_token";

    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getMessage();
        List<String> list = new ArrayList<>();
        Request param = message.getBody(Request.class);



        list.add(
                String.format(
                        group_request + "&user_id=%s&group_id=%s&access_token=%s&v=5.131",
                        param.getUser_id(),
                        param.getGroup_id(),
                        exchange.getIn().getHeader(vkToken_header)
                )
        );
        list.add(
                String.format(
                        user_request + "&user_id=%s&access_token=%s&fields=nickname&v=5.131",
                        param.getUser_id(),
                        exchange.getIn().getHeader(vkToken_header)
                )
        );

        exchange.getIn().setBody(list);
     }
}
