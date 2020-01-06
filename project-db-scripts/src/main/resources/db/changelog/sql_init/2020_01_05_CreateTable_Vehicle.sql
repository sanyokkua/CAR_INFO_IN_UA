CREATE TABLE IF NOT EXISTS car_info.vehicle
(
    id           SERIAL       NOT NULL,
    brand        VARCHAR(255) NOT NULL,
    model        VARCHAR(255),
    body         VARCHAR(255),
    color        VARCHAR(255),
    fuel         VARCHAR(255),
    kind         VARCHAR(255),
    make_year    INT,
    capacity     INT,
    own_weight   INT,
    total_weight INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_brand FOREIGN KEY (brand) REFERENCES car_info.brand ("name"),
    CONSTRAINT fk_model FOREIGN KEY (model) REFERENCES car_info.model ("name"),
    CONSTRAINT fk_body FOREIGN KEY (body) REFERENCES car_info.body_type (type),
    CONSTRAINT fk_color FOREIGN KEY (color) REFERENCES car_info.color ("name"),
    CONSTRAINT fk_fuel FOREIGN KEY (fuel) REFERENCES car_info.fuel_type (type),
    CONSTRAINT fk_kind FOREIGN KEY (kind) REFERENCES car_info.kind (kind)
);
CREATE UNIQUE INDEX vehicle_unique_index ON car_info.vehicle (brand, model, body, make_year, capacity)
    WHERE make_year IS NULL
        OR body IS NULL
        OR capacity IS NULL
        OR model IS NULL;