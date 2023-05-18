package com.wcc.service;

import com.wcc.connector.LocationConnector;
import com.wcc.constants.DistanceConstants;
import com.wcc.dto.DistanceRequest;
import com.wcc.dto.DistanceResponse;
import com.wcc.entity.Location;
import com.wcc.exception.InvalidPostalCodeException;
import com.wcc.transformer.DistanceResponseTransformer;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class DistanceCalculatorService {
    private final LocationConnector locationConnector;
    private final DistanceResponseTransformer responseTransformer;

    public DistanceCalculatorService(LocationConnector locationConnector, DistanceResponseTransformer responseTransformer) {
        this.locationConnector = locationConnector;
        this.responseTransformer = responseTransformer;
    }

    public DistanceResponse calculateDistance(DistanceRequest request) {
        Location location1 = locationConnector.findByPostalCode(request.getPostalCode1());
        Location location2 = locationConnector.findByPostalCode(request.getPostalCode2());
        if (location1 == null || location2 == null) {
            throw new InvalidPostalCodeException("No Postal codes retrieved from Database");
        }

        String distance = calculateDistanceBetweenLocations(location1, location2);


        return responseTransformer.toDistanceResponse(location1, location2, distance);
    }


    String calculateDistanceBetweenLocations(Location location1, Location location2) {
        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());

        // Haversine formula to calculate distance
        double deltaLat = lat2 - lat1;
        double deltaLon = lon2 - lon1;
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = DistanceConstants.EARTH_RADIUS * c;

        DecimalFormat df = new DecimalFormat("0.00");
        String formattedDistance = df.format(distance);

        return Double.parseDouble(formattedDistance) + " km";
    }


    public Location findByPostalCode(String postalCode) {
        return locationConnector.findByPostalCode(postalCode);
    }

    public void updateService(Location existingLocation) {
        Location updatedLocation = locationConnector.save(existingLocation);
    }
}

