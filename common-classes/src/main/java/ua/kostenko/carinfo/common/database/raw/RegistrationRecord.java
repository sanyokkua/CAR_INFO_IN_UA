package ua.kostenko.carinfo.common.database.raw;

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
public class RegistrationRecord implements Serializable {
    private Long id;
    private Long adminObjectId;
    private Long opCode;
    private Long depCode;
    private Long kindId;
    private Long vehicleId;
    private Long colorId;
    private Long bodyTypeId;
    private Long purposeId;
    private Long fuelTypeId;
    private Long engineCapacity;
    private Long ownWeight;
    private Long totalWeight;
    private Long makeYear;
    private String registrationNumber;
    private Date registrationDate;
}
