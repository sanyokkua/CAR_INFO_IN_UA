package ua.kostenko.carinfo.importing.csv.mappers.registration;

import lombok.NonNull;
import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.importing.csv.mappers.CsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.Registration;
import ua.kostenko.carinfo.importing.csv.structure.headers.registration.RegistrationHeaders;

public class RegistrationCsvMapper implements CsvMapper<Registration> {
    private final RegistrationHeaders headers;

    public RegistrationCsvMapper(@NonNull RegistrationHeaders headers) {
        this.headers = headers;
    }

    @Override
    public Registration map(CSVRecord csvRecord) {
        return Registration.builder()
                           .personType(getString(csvRecord, headers.getPersonType()))
                           .administrativeObject(getLong(csvRecord, headers.getAdministrativeObject()))
                           .operationCode(getLong(csvRecord, headers.getOperationCode()))
                           .operationName(getString(csvRecord, headers.getOperationName()))
                           .registrationDate(getString(csvRecord, headers.getRegistrationDate()))
                           .departmentCode(getLong(csvRecord, headers.getDepartmentCode()))
                           .departmentName(getString(csvRecord, headers.getDepartmentName()))
                           .vehicleBrand(getString(csvRecord, headers.getVehicleBrand()))
                           .vehicleModel(getString(csvRecord, headers.getVehicleModel()))
                           .vehicleMakeYear(getLong(csvRecord, headers.getVehicleMakeYear()))
                           .vehicleColor(getString(csvRecord, headers.getVehicleColor()))
                           .vehicleKind(getString(csvRecord, headers.getVehicleKind()))
                           .vehicleBodyType(getString(csvRecord, headers.getVehicleBodyType()))
                           .vehiclePurpose(getString(csvRecord, headers.getVehiclePurpose()))
                           .vehicleFuelType(getString(csvRecord, headers.getVehicleFuelType()))
                           .vehicleEngineCapacity(getLong(csvRecord, headers.getVehicleEngineCapacity()))
                           .vehicleOwnWeight(getLong(csvRecord, headers.getVehicleOwnWeight()))
                           .vehicleTotalWeight(getLong(csvRecord, headers.getVehicleTotalWeight()))
                           .vehicleRegistrationNumber(getString(csvRecord, headers.getVehicleRegistrationNumber()))
                           .build();
    }
}
