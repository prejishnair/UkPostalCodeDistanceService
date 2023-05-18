package com.wcc.transformer;

import com.wcc.dto.DistanceRequest;
import com.wcc.dto.ErrorResponse;
import com.wcc.exception.InvalidPostalCodeException;
import com.wcc.utility.PostalCodeUtility;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DistanceRequestTransformer {
    private static final String UK_POSTAL_CODE_REGEX = "^[A-Z]{1,2}[0-9RCHNQ][0-9A-Z]?\\s?[0-9][ABD-HJLNP-UW-Z]{2}$|^[A-Z]{2}-?[0-9]{4}$";
    private static final Pattern UK_POSTAL_CODE_PATTERN = Pattern.compile(UK_POSTAL_CODE_REGEX);

    public DistanceRequest transform(String postalCode1, String postalCode2) throws InvalidPostalCodeException {
        postalCode1 = PostalCodeUtility.removeSpaces(postalCode1); // Remove white spaces from postalCode1
        postalCode2 = PostalCodeUtility.removeSpaces(postalCode2); // Remove white spaces from postalCode2

        List<String> invalidPostalCodes = Stream.of(postalCode1, postalCode2)
                .filter(postalCode -> !validatePostalCode(postalCode))
                .collect(Collectors.toList());

        if (!invalidPostalCodes.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse("Bad Request", "Postal codes are invalid");
            throw new InvalidPostalCodeException("Postal Code Validation Failed.", errorResponse);
        }

        DistanceRequest request = new DistanceRequest();
        request.setPostalCode1(postalCode1);
        request.setPostalCode2(postalCode2);
        return request;
    }

    private boolean validatePostalCode(String postalCode) {
        return UK_POSTAL_CODE_PATTERN.matcher(postalCode).matches();
    }
}


