package com.wcc.dto;

import lombok.Data;

@Data
public class DistanceResponse {

    private String postalCode1;
    private String postalCode2;
    private Double latitude1;
    private Double longitude1;
    private Double latitude2;
    private Double longitude2;
    private String distance;

    public DistanceResponse() {
    }
}

