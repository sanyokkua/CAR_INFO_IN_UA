package ua.kostenko.carinfo.common.database.repositories.helpers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.records.Registration;

import javax.annotation.Nonnull;

@Repository
@Slf4j
public class RegistrationRecordHelperRepository {
    private static final RowMapper<Registration> ROW_MAPPER = (resultSet, i) -> Registration.builder() //TODO: change each constant
                                                                                            .id(resultSet.getLong(Constants.RegistrationRecord.ID))
                                                                                            .adminObjectId(resultSet.getString(Constants.RegistrationRecord.ADMIN_OBJ_ID))
                                                                                            .opCode(resultSet.getString(Constants.RegistrationRecord.OPERATION_CODE))
                                                                                            .depCode(resultSet.getString(Constants.RegistrationRecord.DEPARTMENT_CODE))
                                                                                            .kindId(resultSet.getString(Constants.RegistrationRecord.KIND))
                                                                                            .brand(resultSet.getString(Constants.RegistrationRecord.VEHICLE_ID))
                                                                                            .model(resultSet.getString(Constants.RegistrationRecord.VEHICLE_ID))
                                                                                            .colorId(resultSet.getString(Constants.RegistrationRecord.COLOR_ID))
                                                                                            .bodyTypeId(resultSet.getString(Constants.RegistrationRecord.BODY_TYPE_ID))
                                                                                            .purposeId(resultSet.getString(Constants.RegistrationRecord.PURPOSE_ID))
                                                                                            .fuelTypeId(resultSet.getString(Constants.RegistrationRecord.FUEL_TYPE_ID))
                                                                                            .engineCapacity(resultSet.getLong(Constants.RegistrationRecord.ENGINE_CAPACITY))
                                                                                            .ownWeight(resultSet.getLong(Constants.RegistrationRecord.OWN_WEIGHT))
                                                                                            .totalWeight(resultSet.getLong(Constants.RegistrationRecord.TOTAL_WEIGHT))
                                                                                            .makeYear(resultSet.getLong(Constants.RegistrationRecord.MAKE_YEAR))
                                                                                            .registrationNumber(resultSet.getString(Constants.RegistrationRecord.REGISTRATION_NUMBER))
                                                                                            .registrationDate(resultSet.getDate(Constants.RegistrationRecord.REGISTRATION_DATE))
                                                                                            .build();
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RegistrationRecordHelperRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
