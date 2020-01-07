package ua.kostenko.carinfo.csv.records;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRecord {

    private String personType;
    private Long administrativeTerritorialUnitCode;
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
}
