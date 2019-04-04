package ua.kostenko.carinfo.common.database.repositories.helpers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.records.Vehicle;

import javax.annotation.Nonnull;

@Repository
@Slf4j
public class RegistrationVehicleHelperRepository {
    private static final RowMapper<Vehicle> ROW_MAPPER = (resultSet, i) -> Vehicle.builder()
                                                                                  .vehicleId(resultSet.getLong(Constants.RegistrationVehicle.ID))
                                                                                  .registrationBrand(resultSet.getString(Constants.RegistrationVehicle.BRAND_ID))
                                                                                  .registrationModel(resultSet.getString(Constants.RegistrationVehicle.MODEL_ID))
                                                                                  .build();
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RegistrationVehicleHelperRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
