package ua.kostenko.carinfo.common.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private Long adminObjectId;
    @JsonIgnore
    private Long opCode;
    @JsonIgnore
    private Long depCode;
    @JsonIgnore
    private Long kindId;
    @JsonIgnore
    private Long colorId;
    @JsonIgnore
    private Long bodyTypeId;
    @JsonIgnore
    private Long purposeId;
    @JsonIgnore
    private Long fuelTypeId;
    @JsonIgnore
    private Long vehicleId;

    private String adminObjName;
    private String adminObjType;
    private String operation;
    private String departmentName;
    private String departmentAddress;
    private String departmentEmail;
    private String kind;
    private String brand;
    private String model;
    private String color;
    private String bodyType;
    private String purpose;
    private String fuelType;
    private Long engineCapacity;
    private Long ownWeight;
    private Long totalWeight;
    private Long makeYear;
    private String registrationNumber;
    private Date registrationDate;
}