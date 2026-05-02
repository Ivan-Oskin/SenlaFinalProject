package com.oskin.ad_board.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class UserRequest {
    @Email(message = "no valid mail")
    private String mail;
    @Size(min = 8, max = 24, message = "The password must be between 8 and 24 characters long.")
    private String password;

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public UserRequest() {
    }
}
