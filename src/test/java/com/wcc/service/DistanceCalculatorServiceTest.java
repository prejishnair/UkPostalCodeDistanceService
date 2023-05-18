package com.wcc.service;

import com.wcc.connector.LocationConnector;
import com.wcc.dto.DistanceRequest;
import com.wcc.dto.DistanceResponse;
import com.wcc.entity.Location;
import com.wcc.exception.InvalidPostalCodeException;
import com.wcc.transformer.DistanceResponseTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DistanceCalculatorServiceTest {

    @Mock
    private LocationConnector locationConnector;

    @Mock
    private DistanceResponseTransformer responseTransformer;

    private DistanceCalculatorService distanceCalculatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        distanceCalculatorService = new DistanceCalculatorService(locationConnector, responseTransformer);
    }


    @Test
    public void testCalculateDistance_InvalidPostalCode() {
        // Mocking location data with null values
        when(locationConnector.findByPostalCode(anyString())).thenReturn(null);

        // Create invalid request
        DistanceRequest request = new DistanceRequest();
        request.setPostalCode1("12345");
        request.setPostalCode2("56789");

        // Assertion for InvalidPostalCodeException
        Assertions.assertThrows(InvalidPostalCodeException.class,
                () -> distanceCalculatorService.calculateDistance(request));
    }

    @Test
    public void testCalculateDistanceBetweenLocations() {
        // Creating test locations
        Location location1 = new Location();
        Location location2 = new Location();
        location1.setLatitude(57.144156);
        location1.setLongitude(-2.114864);
        location2.setLatitude(57.137871);
        location2.setLongitude(-2.121487);

        // Calculating expected distance
        String expectedDistance = "0.81 km";

        // Calculating actual distance
        String actualDistance = distanceCalculatorService.calculateDistanceBetweenLocations(location1, location2);

        // Assertion
        Assertions.assertEquals(expectedDistance, actualDistance);
    }

    @Test
    public void testCalculateDistance() {
        // Mocking the Location objects
        Location location1 = new Location();
        location1.setPostalCode("10001");
        Location location2 = new Location();
        location2.setPostalCode("20001");

        // Mocking the request
        DistanceRequest request = new DistanceRequest();
        request.setPostalCode1("10001");
        request.setPostalCode2("20001");

        // Mocking the response
        DistanceResponse expectedResponse = new DistanceResponse();
        expectedResponse.setPostalCode1("10001");
        expectedResponse.setPostalCode2("20001");
        expectedResponse.setLatitude1(location1.getLatitude());
        expectedResponse.setLongitude1(location1.getLongitude());
        expectedResponse.setLatitude2(location2.getLatitude());
        expectedResponse.setLongitude2(location2.getLongitude());
        expectedResponse.setDistance("225.5km");

        // Mocking the behavior of the LocationConnector
        LocationConnector locationConnector = Mockito.mock(LocationConnector.class);
        Mockito.when(locationConnector.findByPostalCode("10001")).thenReturn(location1);
        Mockito.when(locationConnector.findByPostalCode("20001")).thenReturn(location2);

        // Creating an instance of DistanceResponseTransformer
        DistanceResponseTransformer responseTransformer = new DistanceResponseTransformer();

        // Calling the method under test
        DistanceResponse actualResponse = responseTransformer.toDistanceResponse(location1, location2, "225.5km");

        // Asserting the expected response with the actual response
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

}
