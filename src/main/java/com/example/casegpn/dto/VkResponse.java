package com.example.casegpn.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class VkResponse implements Serializable {
    String firstName;
    String lastName;
    String middleName;
    Boolean member;
    Map<String, Object> errors = new HashMap<>();
}
