package com.winter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginInfo {
    private Integer id;
    private String username;
    private String password;
    private String token;
}
