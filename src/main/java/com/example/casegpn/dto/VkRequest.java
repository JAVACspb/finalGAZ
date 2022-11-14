package com.example.casegpn.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VkRequest {
    private String userId;
    private String groupId;
}
