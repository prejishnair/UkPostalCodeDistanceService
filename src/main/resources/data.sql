CREATE TABLE IF NOT EXISTS Location (
                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                    POSTALCODE VARCHAR(255),
                                    LATITUDE DOUBLE,
                                    LONGITUDE DOUBLE

    );

INSERT INTO Location (POSTALCODE,LATITUDE,LONGITUDE) VALUES ('AB101XG',57.144156,-2.114864);
INSERT INTO Location (POSTALCODE,LATITUDE,LONGITUDE) VALUES ('AB106RN',57.137871,-2.121487);
