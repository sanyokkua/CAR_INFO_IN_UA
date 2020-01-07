CREATE TABLE IF NOT EXISTS car_info.admin_unit
(
    code BIGINT       NOT NULL,
    type VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    PRIMARY KEY (code)
);
CREATE INDEX admin_unit_index ON car_info.admin_unit (type, name);