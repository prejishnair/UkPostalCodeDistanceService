package com.wcc.transformer;

import com.wcc.dto.DistanceRequest;
import com.wcc.entity.Location;
import com.wcc.exception.InvalidPostalCodeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DistanceRequestTransformerTest {
    private DistanceRequestTransformer distanceRequestTransformer;

    @BeforeEach
    void setUp() {
        distanceRequestTransformer = new DistanceRequestTransformer();
    }

    @Test
    void transformCalculateDistanceRequest_ValidPostalCodes_ReturnsDistanceRequest() {
        // Arrange
        String postalCode1 = "SW1A 1AA";
        String postalCode2 = "WC2N 5DU";

        // Act
        DistanceRequest result = distanceRequestTransformer.transformCalculateDistanceRequest(postalCode1, postalCode2);

        // Assert
        Assertions.assertEquals(postalCode1.replace(" ", ""), result.getPostalCode1());
        Assertions.assertEquals(postalCode2.replace(" ", ""), result.getPostalCode2());
    }

    @Test
    void transformCalculateDistanceRequest_InvalidPostalCodes_ThrowsInvalidPostalCodeException() {
        // Arrange
        String postalCode1 = "12345";
        String postalCode2 = "ABCDEF";
        String expectedErrorMessage = "Postal Code Validation Failed.";

        // Act & Assert
        InvalidPostalCodeException exception = Assertions.assertThrows(
                InvalidPostalCodeException.class,
                () -> distanceRequestTransformer.transformCalculateDistanceRequest(postalCode1, postalCode2)
        );

        // Assert
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void transformUpdateRequest_ValidPostalCode_ReturnsLocation() {
        // Arrange
        Location inputLocation = new Location();
        inputLocation.setPostalCode("AB106RN");
        // Act
        Location result = distanceRequestTransformer.transformUpdateRequest(inputLocation);

        // Assert
        Assertions.assertEquals(inputLocation.getPostalCode().replace(" ", ""), result.getPostalCode());
    }

    @Test
    void transformUpdateRequest_InvalidPostalCode_ThrowsInvalidPostalCodeException() {
        // Arrange
        Location inputLocation = new Location();
        inputLocation.setPostalCode("12345");
        String expectedErrorMessage = "Postal Code Validation Failed.";

        // Act & Assert
        InvalidPostalCodeException exception = Assertions.assertThrows(
                InvalidPostalCodeException.class,
                () -> distanceRequestTransformer.transformUpdateRequest(inputLocation)
        );

        // Assert
        Assertions.assertEquals(expectedErrorMessage, exception.getMessage());
    }
}
