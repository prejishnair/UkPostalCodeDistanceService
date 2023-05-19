package com.wcc.controller;

import com.wcc.dto.DistanceRequest;
import com.wcc.dto.DistanceResponse;
import com.wcc.dto.ErrorResponse;
import com.wcc.entity.Location;
import com.wcc.exception.InvalidPostalCodeException;
import com.wcc.service.DistanceCalculatorService;
import com.wcc.transformer.DistanceRequestTransformer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value = "/api/distance")
public class DistanceController {
    private static final Logger logger = LoggerFactory.getLogger(DistanceController.class);

    private final DistanceCalculatorService distanceCalculatorService;
    private final DistanceRequestTransformer distanceRequestTransformer;

    public DistanceController(DistanceCalculatorService distanceCalculatorService, DistanceRequestTransformer distanceRequestTransformer) {
        this.distanceCalculatorService = distanceCalculatorService;
        this.distanceRequestTransformer = distanceRequestTransformer;
    }

    @Operation(description = "Get distance based on postal codes of 2 locations", operationId = "fetchDetails")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = DistanceRequest.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request (when the values of the input not in the correct way)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(produces = "application/json")
    public ResponseEntity<DistanceResponse> calculateDistance(
            @RequestParam("postalCode1") String postalCode1,
            @RequestParam("postalCode2") String postalCode2) {
        logger.info("Calculating distance for postal codes: {} and {}", postalCode1, postalCode2);

        try {
            DistanceRequest request = distanceRequestTransformer.transformCalculateDistanceRequest(postalCode1, postalCode2);
            DistanceResponse response = distanceCalculatorService.calculateDistance(request);
            logger.info("Distance calculated successfully");
            return ResponseEntity.ok().body(response);
        } catch (InvalidPostalCodeException e) {
            logger.error("Invalid postal code request: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }


    @Operation(description = "Update postal codes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation"),
            @ApiResponse(responseCode = "400", description = "Bad Request (when the values of the input not in the correct way)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PutMapping(value = "/update", consumes = "application/json")
    public ResponseEntity<String> updatePostalCodes(@RequestBody Location location) {
        logger.info("Updating postal codes: {}", location);

        try {
            location = distanceRequestTransformer.transformUpdateRequest(location);
            Location existingLocation = distanceCalculatorService.findByPostalCode(location.getPostalCode());
            if (existingLocation == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Postal code does not exist in the database.");
            }

            existingLocation.setPostalCode(location.getPostalCode());
            existingLocation.setLatitude(location.getLatitude());
            existingLocation.setLongitude(location.getLongitude());
            distanceCalculatorService.updateService(existingLocation);

            logger.info("Postal codes updated successfully");

            return ResponseEntity.ok().body("Postal code Updated");

        } catch (InvalidPostalCodeException e) {
            logger.error("Invalid postal code request: {}", e.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}

