# Distance Calculation

This project basically will return the geographic (straight line) distance between two
postal codes in the UK.

## Requirement

The project need to expose 2 endpoints, that can talk to database layer.

| OPERATION | ENDPOINT                                  | DESCRIPTION                                                                                                                                         |
| --------- |-------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| GET | /api/distance/{postalcode1}/{postalcode2} | Endpoint fetches the required data based on the input variables (postalcode1,postalcode2) and then calculates the distance based on business logic. |
| PUT | /api/distance/update                      | updates the postal-codes                                                                                                                            | 

## Skill set used

- Java 17
- Spring
- h2 Database
- Testcontainers
- Test Driven Development (TDD)

## How to start the project?


## WE ARE READY?

let's start the microservice project, so by default it will start on the port localhost:8080 now!


## Swagger API

> Access Swagger API page on the URL **http://localhost:8080/swagger-ui.html**


### Table Definition

                                    | ID  | POSTCODE            | LATITIDE | LONGITUDE |
                                    |-----|---------------------|----------|-----------|
                                    | ID1 | POSTALCODE1         | LATITIDE | LONGITUDE |
                                    | ID2 | POSTALCODE2         | LATITIDE | LONGITUDE |
                                    | ID3 | POSTALCODE3         | LATITIDE | LONGITUDE |
                                    |-----|---------------------|----------|-----------|



