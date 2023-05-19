package com.wcc.transformer;

import com.wcc.dto.DistanceRequest;
import com.wcc.entity.Location;
import com.wcc.exception.InvalidPostalCodeException;
import com.wcc.utility.PostalCodeUtility;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DistanceRequestTransformer {
    private static final String UK_POSTAL_CODE_REGEX = "^[A-Z]{1,2}[0-9RCHNQ][0-9A-Z]?\\s?[0-9][ABD-HJLNP-UW-Z]{2}$|^[A-Z]{2}-?[0-9]{4}$";
    private static final Pattern UK_POSTAL_CODE_PATTERN = Pattern.compile(UK_POSTAL_CODE_REGEX);

    public DistanceRequest transformCalculateDistanceRequest(String postalCode1, String postalCode2) throws InvalidPostalCodeException {
        postalCode1 = removeSpacesAndValidate(postalCode1);
        postalCode2 = removeSpacesAndValidate(postalCode2);

        DistanceRequest request = new DistanceRequest();
        request.setPostalCode1(postalCode1);
        request.setPostalCode2(postalCode2);
        return request;
    }

    public Location transformUpdateRequest(Location location) throws InvalidPostalCodeException {
        String postalCode = removeSpacesAndValidate(location.getPostalCode());

        location.setPostalCode(postalCode);
        location.setLatitude(location.getLatitude());
        location.setLongitude(location.getLongitude());
        return location;
    }

    private String removeSpacesAndValidate(String postalCode) throws InvalidPostalCodeException {
        postalCode = PostalCodeUtility.removeSpaces(postalCode);

        if (!validatePostalCode(postalCode)) {
            throw new InvalidPostalCodeException("Postal Code Validation Failed.");
        }

        return postalCode;
    }


    private boolean validatePostalCode(String postalCode) {
        return UK_POSTAL_CODE_PATTERN.matcher(postalCode).matches();
    }

}
