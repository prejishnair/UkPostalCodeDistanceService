package com.wcc.connector;

import com.wcc.entity.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationConnector extends CrudRepository<Location, String> {
        Location findByPostalCode(String postalCode);
}
