# Distance Calculation

This project basically will return the geographic (straight line) distance between two
postal codes in the UK.

## Requirement

The project need to expose 2 endpoints, that can talk to database layer.

| OPERATION | ENDPOINT                                                                            | DESCRIPTION                                                                                                                                         |
| --------- |-------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| GET | /api/distance/calculatedistance?postalcode1={postalcode1}&postalcode2={postalcode2} | Endpoint fetches the required data based on the input variables (postalcode1,postalcode2) and then calculates the distance based on business logic. |
| PUT | /api/distance/update                                                                | updates the postal-codes                                                                                                                            | 

## Skill set used

- Java 17
- Spring
- Basic Auth
- h2 Database
- Test Driven Development (TDD)
- Docker container

## How to start the project?
Run the main app (PostalCodeDistanceApplication.java) class.

## WE ARE READY?

so by default it will start on the port localhost:8080 now!


## Swagger API

> Access Swagger API page on the URL **http://localhost:8080/swagger-ui.html**

> Since basic auth is implemented a username and password need to be entered. 
> You can find the same in application.properties file

## Start the project with Docker
Prerequisite : Need docker to be installed on your machine

Steps
1)Start a docker daemon (I use command dockerd.But there are more other options)
2)docker build -t ukpostalcodedistanceservice:<version> .
3)docker run -p 8080:8080 ukpostalcodedistanceservice:<version> .

### Table Definition

                                    | ID  | POSTCODE            | LATITIDE | LONGITUDE |
                                    |-----|---------------------|----------|-----------|
                                    | ID1 | POSTALCODE1         | LATITIDE | LONGITUDE |
                                    | ID2 | POSTALCODE2         | LATITIDE | LONGITUDE |
                                    | ID3 | POSTALCODE3         | LATITIDE | LONGITUDE |
                                    |-----|---------------------|----------|-----------|



