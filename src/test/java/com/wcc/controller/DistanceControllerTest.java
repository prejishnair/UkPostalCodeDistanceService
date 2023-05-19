package com.wcc.controller;

import com.wcc.dto.DistanceRequest;
import com.wcc.dto.DistanceResponse;
import com.wcc.dto.ErrorResponse;
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
        String postalCode1 = "12345";
        String postalCode2 = "67890";
        DistanceRequest distanceRequest = new DistanceRequest();
        DistanceResponse distanceResponse = new DistanceResponse();

        // Mock dependencies
        doReturn(distanceRequest).when(distanceRequestTransformer).transformCalculateDistanceRequest(postalCode1, postalCode2);
        doReturn(distanceResponse).when(distanceCalculatorService).calculateDistance(distanceRequest);

        // Invoke the method
        ResponseEntity<?> responseEntity = distanceController.calculateDistance(postalCode1, postalCode2);

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(distanceResponse, responseEntity.getBody());
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
        ResponseEntity<?> responseEntity = distanceController.calculateDistance(postalCode1, postalCode2);

        // Verify the response
        assertNotNull(responseEntity);
        assertEquals(400, responseEntity.getStatusCodeValue());

        ErrorResponse errorResponse = (ErrorResponse) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals("Invalid Request", errorResponse.getError());
        assertEquals("Invalid postal code", errorResponse.getDescription());
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

        Mockito.when(distanceRequestTransformer.transformUpdateRequest(inputLocation.getPostalCode())).thenReturn(inputLocation);
        Mockito.when(distanceCalculatorService.findByPostalCode(inputLocation.getPostalCode())).thenReturn(existingLocation);

        ResponseEntity<?> response = distanceController.updatePostalCodes(inputLocation);

        // Verify that the service method was called with the correct location
        Mockito.verify(distanceCalculatorService).updateService(existingLocation);

        // Verify the response code
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdatePostalCodes_NonExistingPostalCode_BadRequest() {
        // Mocking input
        Location inputLocation = new Location();
        inputLocation.setPostalCode("12345");
        inputLocation.setLatitude(1.23);
        inputLocation.setLongitude(4.56);

        // Mocking existing location as null
        Mockito.when(distanceRequestTransformer.transformUpdateRequest(inputLocation.getPostalCode())).thenReturn(inputLocation);
        Mockito.when(distanceCalculatorService.findByPostalCode(inputLocation.getPostalCode())).thenReturn(null);

        ResponseEntity<?> response = distanceController.updatePostalCodes(inputLocation);

        // Verify that the service method was not called
        Mockito.verify(distanceCalculatorService, Mockito.never()).updateService(any(Location.class));

        // Verify the response code and error message
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals("Invalid Postal Code", errorResponse.getError());
        Assertions.assertEquals("Postal code does not exist in the database.", errorResponse.getDescription());
    }

    @Test
    public void testUpdatePostalCodes_Exception_InternalServerError() {
        // Mocking input
        Location inputLocation = new Location();
        inputLocation.setPostalCode("12345");
        inputLocation.setLatitude(1.23);
        inputLocation.setLongitude(4.56);

        // Mocking an exception
        Mockito.when(distanceRequestTransformer.transformUpdateRequest(inputLocation.getPostalCode())).thenThrow(new RuntimeException("Something went wrong"));

        ResponseEntity<?> response = distanceController.updatePostalCodes(inputLocation);

        // Verify that the service method was not called
        Mockito.verify(distanceCalculatorService, Mockito.never()).updateService(any(Location.class));

        // Verify the response code and error message
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        Assertions.assertNotNull(errorResponse);
        Assertions.assertEquals("Error Updating Postal Codes", errorResponse.getError());
        Assertions.assertEquals("Something went wrong", errorResponse.getDescription());
    }


}

