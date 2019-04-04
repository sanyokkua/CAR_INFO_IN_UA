package ua.kostenko.carinfo.importing.csv.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationPojo {
    private String personType;

    private Long administrativeObject;

    private Long operationCode;
    private String operationName;

    private String registrationDate;

    private Long departmentCode;
    private String departmentName;

    private String vehicleBrand;

    private String vehicleModel;

    private Long vehicleMakeYear;
    private String vehicleColor;

    private String vehicleKind;

    private String vehicleBodyType;

    private String vehiclePurpose;

    private String vehicleFuelType;

    private Long vehicleEngineCapacity;
    private Long vehicleOwnWeight;
    private Long vehicleTotalWeight;
    private String vehicleRegistrationNumber;
}