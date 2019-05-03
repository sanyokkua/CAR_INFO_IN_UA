package ua.kostenko.carinfo.importing.csv.mappers.registration;

import lombok.NonNull;
import org.apache.commons.csv.CSVRecord;
import ua.kostenko.carinfo.importing.csv.mappers.CsvMapper;
import ua.kostenko.carinfo.importing.csv.pojo.RegistrationCsvRecord;
import ua.kostenko.carinfo.importing.csv.structure.headers.registration.RegistrationHeaders;

import javax.annotation.Nonnull;

public class RegistrationCsvMapper implements CsvMapper<RegistrationCsvRecord> {
    private final RegistrationHeaders headers;

    public RegistrationCsvMapper(@NonNull @Nonnull RegistrationHeaders headers) {
        this.headers = headers;
    }

    @Override
    public RegistrationCsvRecord map(CSVRecord csvRecord) {
        String model = getStringValueInUpperCaseOrDash(csvRecord, headers.getVehicleModel());
        String fuel = getStringValueInUpperCaseOrDash(csvRecord, headers.getVehicleFuelType());
        String brand = getStringValueInUpperCaseOrDash(csvRecord, headers.getVehicleBrand());
        return RegistrationCsvRecord.builder()
                                    .personType(getStringValueInUpperCase(csvRecord, headers.getPersonType()))
                                    .administrativeObject(getLong(csvRecord, headers.getAdministrativeObject()))
                                    .operationCode(getLong(csvRecord, headers.getOperationCode()))
                                    .operationName(getStringValueInUpperCase(csvRecord, headers.getOperationName()))
                                    .registrationDate(getStringValueInUpperCase(csvRecord, headers.getRegistrationDate()))
                                    .departmentCode(getLong(csvRecord, headers.getDepartmentCode()))
                                    .departmentName(getStringValueInUpperCase(csvRecord, headers.getDepartmentName()))
                                    .vehicleBrand(brand)
                                    .vehicleModel(model)
                                    .vehicleMakeYear(getLong(csvRecord, headers.getVehicleMakeYear()))
                                    .vehicleColor(getStringValueInUpperCase(csvRecord, headers.getVehicleColor()))
                                    .vehicleKind(getStringValueInUpperCase(csvRecord, headers.getVehicleKind()))
                                    .vehicleBodyType(getStringValueInUpperCase(csvRecord, headers.getVehicleBodyType()))
                                    .vehiclePurpose(getStringValueInUpperCase(csvRecord, headers.getVehiclePurpose()))
                                    .vehicleFuelType(fuel)
                                    .vehicleEngineCapacity(getLong(csvRecord, headers.getVehicleEngineCapacity()))
                                    .vehicleOwnWeight(getLong(csvRecord, headers.getVehicleOwnWeight()))
                                    .vehicleTotalWeight(getLong(csvRecord, headers.getVehicleTotalWeight()))
                                    .vehicleRegistrationNumber(getStringValueInUpperCase(csvRecord, headers.getVehicleRegistrationNumber()))
                                    .build();
    }
}
