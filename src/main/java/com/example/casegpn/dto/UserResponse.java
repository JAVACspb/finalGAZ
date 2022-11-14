package com.example.casegpn.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserResponse {
     String first_name;
     String nickname;
     String last_name;
}