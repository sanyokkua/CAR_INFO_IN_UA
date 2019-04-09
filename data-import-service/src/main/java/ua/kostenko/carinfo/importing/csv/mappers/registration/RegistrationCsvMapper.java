package ua.kostenko.carinfo.importing.csv.mappers.registration;

import lombok.NonNull;
import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.importing.csv.mappers.CsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.RegistrationPojo;
import ua.kostenko.carinfo.importing.csv.structure.headers.registration.RegistrationHeaders;

import javax.annotation.Nonnull;

public class RegistrationCsvMapper implements CsvMapper<RegistrationPojo> {
    private final RegistrationHeaders headers;

    public RegistrationCsvMapper(@NonNull @Nonnull RegistrationHeaders headers) {
        this.headers = headers;
    }

    @Override
    public RegistrationPojo map(CSVRecord csvRecord) {
        return RegistrationPojo.builder()
                               .personType(getString(csvRecord, headers.getPersonType()).toUpperCase())
                               .administrativeObject(getLong(csvRecord, headers.getAdministrativeObject()))
                               .operationCode(getLong(csvRecord, headers.getOperationCode()))
                               .operationName(getString(csvRecord, headers.getOperationName()).toUpperCase())
                               .registrationDate(getString(csvRecord, headers.getRegistrationDate()))
                               .departmentCode(getLong(csvRecord, headers.getDepartmentCode()))
                               .departmentName(getString(csvRecord, headers.getDepartmentName()).toUpperCase())
                               .vehicleBrand(getString(csvRecord, headers.getVehicleBrand()).toUpperCase())
                               .vehicleModel(getString(csvRecord, headers.getVehicleModel()).toUpperCase())
                               .vehicleMakeYear(getLong(csvRecord, headers.getVehicleMakeYear()))
                               .vehicleColor(getString(csvRecord, headers.getVehicleColor()).toUpperCase())
                               .vehicleKind(getString(csvRecord, headers.getVehicleKind()).toUpperCase())
                               .vehicleBodyType(getString(csvRecord, headers.getVehicleBodyType()).toUpperCase())
                               .vehiclePurpose(getString(csvRecord, headers.getVehiclePurpose()).toUpperCase())
                               .vehicleFuelType(getString(csvRecord, headers.getVehicleFuelType()).toUpperCase())
                               .vehicleEngineCapacity(getLong(csvRecord, headers.getVehicleEngineCapacity()))
                               .vehicleOwnWeight(getLong(csvRecord, headers.getVehicleOwnWeight()))
                               .vehicleTotalWeight(getLong(csvRecord, headers.getVehicleTotalWeight()))
                               .vehicleRegistrationNumber(getString(csvRecord, headers.getVehicleRegistrationNumber()).toUpperCase())
                               .build();
    }
}
