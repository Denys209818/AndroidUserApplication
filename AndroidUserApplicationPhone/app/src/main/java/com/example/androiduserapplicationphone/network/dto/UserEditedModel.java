package com.example.androiduserapplicationphone.network.dto;

import lombok.Data;

@Data
public class UserEditedModel {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String image;
    private String oldEmail;
}
