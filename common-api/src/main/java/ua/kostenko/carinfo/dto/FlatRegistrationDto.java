package ua.kostenko.carinfo.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ua.kostenko.carinfo.enums.PersonType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlatRegistrationDto {

    private PersonType personType;
    private Long administrativeTerritorialUnitCode;
    private Long administrativeTerritorialUnitType;
    private Long administrativeTerritorialUnitName;
    private Long operationCode;
    private String operationName;
    private LocalDate registrationDate;
    private Long departmentCode;
    private String departmentName;
    private String vehicleBrand;
    private String vehicleModel;
    private Integer vehicleMakeYear;
    private String vehicleColor;
    private String vehicleKind;
    private String vehicleBody;
    private String vehiclePurpose;
    private String vehicleFuelType;
    private Integer capacity;
    private Integer vehicleOwnWeight;
    private Integer vehicleTotalWeight;
    private String registrationNumber;
    private String companyName;
    private String companyStatus;
    private String phoneNumber;
    private String companyEmail;
    private String companyAddress;
    private String carRegistrationNumber;
    private String carVinNumber;
    private String carType;
    private String carBrandAndModel;
}
