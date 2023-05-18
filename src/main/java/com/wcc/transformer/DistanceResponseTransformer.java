package com.wcc.transformer;

import com.wcc.dto.DistanceResponse;
import com.wcc.entity.Location;
import org.springframework.stereotype.Component;


@Component
public class DistanceResponseTransformer {
    public static DistanceResponse toDistanceResponse(Location location1, Location location2, String distance){
        DistanceResponse result = new DistanceResponse();
        if (location1 != null) {
            result.setPostalCode1(location1.getPostalCode());
            result.setLatitude1(location1.getLatitude());
            result.setLongitude1(location1.getLongitude());
        }
        if (location2 != null) {
            result.setPostalCode2(location2.getPostalCode());
            result.setLatitude2(location2.getLatitude());
            result.setLongitude2(location2.getLongitude());
        }
        result.setDistance(distance);
        return result;
    }


}

