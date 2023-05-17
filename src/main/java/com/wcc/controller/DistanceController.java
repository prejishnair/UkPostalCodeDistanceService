package com.wcc.controller;

import com.wcc.dto.DistanceRequest;
import com.wcc.dto.DistanceResponse;
import com.wcc.dto.ErrorResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = "/{postalCode1}/{postalCode2}", produces = "application/json")
    public ResponseEntity<?> calculateDistance(
            @PathVariable("postalCode1") String postalCode1,
            @PathVariable("postalCode2") String postalCode2) {
        logger.info("Calculating distance for postal codes: {} and {}", postalCode1, postalCode2);

        DistanceResponse response;
        try {
            DistanceRequest request = distanceRequestTransformer.transform(postalCode1, postalCode2);
            response = distanceCalculatorService.calculateDistance(request);
        } catch (InvalidPostalCodeException e) {
            logger.error("Invalid postal code request: {}", e.getMessage());
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setError("Invalid Request");
            errorResponse.setDescription(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        logger.info("Distance calculated successfully");

        return ResponseEntity.ok().body(response);
    }
}

