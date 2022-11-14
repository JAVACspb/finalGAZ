package com.example.finalgaz.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class Response {
    String firstName;
    String lastName;
    String middleName;
    Boolean member;
    Map<String, Object> errors = new HashMap<>();
}
