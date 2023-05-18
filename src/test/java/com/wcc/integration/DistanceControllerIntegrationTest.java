package com.wcc.integration;

import com.wcc.dto.DistanceResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DistanceControllerIntegrationTest {

    @ClassRule
    public static GenericContainer<?> h2Container = new GenericContainer<>("1000kit/h2")
            .withExposedPorts(8181, 1521)
            .withEnv("H2_OPTIONS", "-ifNotExists");

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeClass
    public static void setup() {
        h2Container.start();
    }

    @AfterClass
    public static void cleanup() {
        h2Container.stop();
    }

    @Test
    public void testCalculateDistance() {
        // Perform an HTTP request to the controller endpoint using TestRestTemplate
        ResponseEntity<DistanceResponse> response = restTemplate.getForEntity(
                "/api/distance/{postalCode1}/{postalCode2}",
                DistanceResponse.class,
                "12345",
                "67890"
        );

        // Validate the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        // Add more assertions as needed
    }
}

