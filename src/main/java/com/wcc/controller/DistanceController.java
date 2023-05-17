package com.wcc.controller;

import com.wcc.dto.DistanceRequest;
import com.wcc.dto.DistanceResponse;
import com.wcc.dto.ErrorResponse;
import com.wcc.exception.InvalidPostalCodeException;
import com.wcc.service.DistanceCalculatorService;
import com.wcc.transformer.DistanceRequestTransformer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

