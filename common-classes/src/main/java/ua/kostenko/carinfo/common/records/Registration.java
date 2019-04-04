package ua.kostenko.carinfo.common.records;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registration implements Serializable {
    private Long id;
    private String adminObjectId;
    private String opCode;
    private String depCode;
    private String kindId;
    private String brand;
    private String model;
    private String colorId;
    private String bodyTypeId;
    private String purposeId;
    private String fuelTypeId;
    private Long engineCapacity;
    private Long ownWeight;
    private Long totalWeight;
    private Long makeYear;
    private String registrationNumber;
    private Date registrationDate;
}