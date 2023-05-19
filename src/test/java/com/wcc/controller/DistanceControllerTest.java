package com.wcc.controller;

import com.wcc.dto.DistanceRequest;
import com.wcc.dto.DistanceResponse;
import com.wcc.entity.Location;
import com.wcc.exception.InvalidPostalCodeException;
import com.wcc.service.DistanceCalculatorService;
import com.wcc.transformer.DistanceRequestTransformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

class DistanceControllerTest {

    private DistanceController distanceController;

    @Mock
    private DistanceCalculatorService distanceCalculatorService;

    @Mock
    private DistanceRequestTransformer distanceRequestTransformer;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        distanceController = new DistanceController(distanceCalculatorService, distanceRequestTransformer);
    }

    @Test
    void calculateDistance_ValidPostalCodes_Success() {
        // Mock input
        String postalCode1 = "AB101XG";
        String postalCode2 = "AB106RN";
        DistanceRequest distanceRequest = new DistanceRequest();
        DistanceResponse distanceResponse = new DistanceResponse();
        distanceResponse.setPostalCode1(postalCode1);
        distanceResponse.setPostalCode2(postalCode2);
        distanceResponse.setLongitude1(-2.114864);
        distanceResponse.setLatitude1(57.144156);
        distanceResponse.setLatitude2(57.137871);
        distanceResponse.setLongitude2(-2.121487);
        distanceResponse.setDistance("0.81");

        // Mock dependencies
        doReturn(distanceRequest).when(distanceRequestTransformer).transformCalculateDistanceRequest(postalCode1, postalCode2);
        doReturn(distanceResponse).when(distanceCalculatorService).calculateDistance(distanceRequest);

        // Invoke the method
        ResponseEntity<DistanceResponse> responseEntity = distanceController.calculateDistance(postalCode1, postalCode2);

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(distanceResponse.getPostalCode1(), responseEntity.getBody().getPostalCode1());
    }

    @Test
    void calculateDistance_InvalidPostalCode_ExceptionThrown() {
        // Mock input
        String postalCode1 = "invalid";
        String postalCode2 = "67890";
        InvalidPostalCodeException exception = new InvalidPostalCodeException("Invalid postal code");

        // Mock dependencies
        doThrow(exception).when(distanceRequestTransformer).transformCalculateDistanceRequest(postalCode1, postalCode2);

        // Invoke the method
        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> distanceController.calculateDistance(postalCode1, postalCode2), "Exception expected");
        Assertions.assertEquals("Invalid postal code", thrown.getReason());
    }

    @Test
    public void testUpdatePostalCodes_ExistingPostalCode_Success() {
        // Mocking input
        Location inputLocation = new Location();
        inputLocation.setPostalCode("12345");
        inputLocation.setLatitude(1.23);
        inputLocation.setLongitude(4.56);

        // Mocking existing location
        Location existingLocation = new Location();
        existingLocation.setPostalCode("12345");
        existingLocation.setLatitude(7.89);
        existingLocation.setLongitude(0.12);

        Mockito.when(distanceRequestTransformer.transformUpdateRequest(inputLocation)).thenReturn(inputLocation);
        Mockito.when(distanceCalculatorService.findByPostalCode(inputLocation.getPostalCode())).thenReturn(existingLocation);

        ResponseEntity<?> response = distanceController.updatePostalCodes(inputLocation);

        // Verify that the service method was called with the correct location
        Mockito.verify(distanceCalculatorService).updateService(existingLocation);

        // Verify the response code
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test()

    public void testUpdatePostalCodes_NonExistingPostalCode_BadRequest() {
        // Mocking input
        Location inputLocation = new Location();
        inputLocation.setPostalCode("12345");
        inputLocation.setLatitude(1.23);
        inputLocation.setLongitude(4.56);

        // Mocking existing location as null
        Mockito.when(distanceRequestTransformer.transformUpdateRequest(inputLocation)).thenReturn(inputLocation);
        Mockito.when(distanceCalculatorService.findByPostalCode(inputLocation.getPostalCode())).thenReturn(null);


        ResponseStatusException thrown = Assertions.assertThrows(ResponseStatusException.class, () -> distanceController.updatePostalCodes(inputLocation), "Exception expected");
        Assertions.assertEquals("Postal code does not exist in the database.", thrown.getReason());
        // Verify that the service method was not called
        Mockito.verify(distanceCalculatorService, Mockito.never()).updateService(any(Location.class));


    }


}

