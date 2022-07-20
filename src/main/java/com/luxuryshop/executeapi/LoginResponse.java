package com.luxuryshop.executeapi;

import lombok.Data;

/**
 * Copyright 2019 {@author Loda} (https://loda.me).
 * This project is licensed under the MIT license.
 *
 * @since 5/1/2019
 * Github: https://github.com/loda-kun
 */
@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String message;

    public LoginResponse(String accessToken, String mess) {
        this.accessToken = accessToken;
        this.message = mess;
    }
}