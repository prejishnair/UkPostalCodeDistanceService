package com.wcc.transformer;

import com.wcc.dto.DistanceRequest;
import com.wcc.dto.DistanceResponse;
import com.wcc.entity.Location;
import org.springframework.stereotype.Component;


@Component
public class DistanceResponseTransformer {
    public static DistanceResponse toDistanceResponse(DistanceRequest request, Location location1, Location location2, double distance){
        DistanceResponse result = new DistanceResponse();
        result.setPostalCode1(location1.getPostalCode());
        result.setPostalCode2(location2.getPostalCode());
        result.setLatitude1(location1.getLatitude());
        result.setLongitude1(location1.getLongitude());
        result.setLatitude2(location2.getLatitude());
        result.setLongitude2(location2.getLongitude());
        result.setDistance(distance);
        result.setUnit("km");
        return result;
    }

}

