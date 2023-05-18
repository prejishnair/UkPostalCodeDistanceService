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
        doReturn(distanceRequest).when(distanceRequestTransformer).transform(postalCode1, postalCode2);
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
        doThrow(exception).when(distanceRequestTransformer).transform(postalCode1, postalCode2);

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
    public void testUpdatePostalCodes_ValidInput_Success() {
        // Arrange
        Location location = new Location();
        location.setPostalCode("12345");
        location.setLatitude(12.345);
        location.setLongitude(67.890);

        Location existingLocation = new Location();
        existingLocation.setPostalCode("12345");
        existingLocation.setLatitude(12.345);
        existingLocation.setLongitude(67.890);

        Mockito.when(distanceCalculatorService.findByPostalCode(location.getPostalCode()))
                .thenReturn(existingLocation);

        // Act
        ResponseEntity<?> response = distanceController.updatePostalCodes(location);

        // Assert
        Mockito.verify(distanceCalculatorService, Mockito.times(1)).updateService(existingLocation);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdatePostalCodes_InvalidInput_BadRequest() {
        // Arrange
        Location location = new Location();
        location.setPostalCode("54321");
        location.setLatitude(12.345);
        location.setLongitude(67.890);

        Mockito.when(distanceCalculatorService.findByPostalCode(location.getPostalCode()))
                .thenReturn(null);

        // Act
        ResponseEntity<?> response = distanceController.updatePostalCodes(location);

        // Assert
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertEquals("Postal code does not exist in the database.", errorResponse.getDescription());
    }

    @Test
    public void testUpdatePostalCodes_Exception_InternalServerError() {
        // Arrange
        Location location = new Location();
        location.setPostalCode("12345");
        location.setLatitude(12.345);
        location.setLongitude(67.890);

        Mockito.when(distanceCalculatorService.findByPostalCode(location.getPostalCode()))
                .thenThrow(new RuntimeException("Database connection error."));

        // Act
        ResponseEntity<?> response = distanceController.updatePostalCodes(location);

        // Assert
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Error Updating Postal Codes", errorResponse.getError());
        Assertions.assertEquals("Database connection error.", errorResponse.getDescription());
    }
}

