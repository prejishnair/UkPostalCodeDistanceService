package com.wcc.dto;

import lombok.Data;

@Data
public class DistanceResponse {

    private String postalCode1;
    private String postalCode2;
    private double latitude1;
    private double longitude1;
    private double latitude2;
    private double longitude2;
    private double distance;
    private String unit;
    private ErrorResponse error;

}

