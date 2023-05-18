package com.wcc.dto;

import lombok.Data;

@Data
public class DistanceRequest {
    private String postalCode1;
    private String postalCode2;

    public DistanceRequest() {
    }
}

