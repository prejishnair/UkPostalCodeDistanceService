package com.wcc.dto;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;
    private String description;

    public ErrorResponse(String error, String description) {
    }

    public ErrorResponse() {

    }
}

