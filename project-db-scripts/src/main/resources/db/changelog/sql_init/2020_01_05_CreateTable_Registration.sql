CREATE TABLE IF NOT EXISTS car_info.registration
(
    "id"                                SERIAL NOT NULL,
    "personType"                        VARCHAR(2),
    "administrativeTerritorialUnitCode" BIGINT,
    "registrationDate"                  DATE,
    "vehicleRegistrationNumber"         VARCHAR,
    "vehicle"                           INT    NOT NULL,
    "purpose"                           VARCHAR(255),
    "operationCode"                     INT    NOT NULL,
    "departmentCode"                    INT,
    CONSTRAINT fk_vehicle FOREIGN KEY ("vehicle") REFERENCES car_info.vehicle ("id"),
    CONSTRAINT fk_purpose FOREIGN KEY ("purpose") REFERENCES car_info.purpose ("purpose"),
    CONSTRAINT fk_operation_code FOREIGN KEY ("operationCode") REFERENCES car_info.operation ("code"),
    CONSTRAINT fk_department_code FOREIGN KEY ("departmentCode") REFERENCES car_info.department ("code")
);