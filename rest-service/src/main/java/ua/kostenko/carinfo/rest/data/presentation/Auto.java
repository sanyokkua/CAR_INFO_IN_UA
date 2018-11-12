package ua.kostenko.carinfo.rest.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.kostenko.carinfo.common.TablesAndFieldsConstants.RegistrationInformationTable.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Auto {
    private String carBrand;
    private String carModel;
    private Long carMakeYear;
    private String carColor;
    private String carKind;
    private String carBody;
    private String carPurpose;
    private String carFuel;
    private Long carEngineCapacity;
    private Long carOwnWeight;
    private Long carTotalWeight;

    public static Auto map(ResultSet resultSet) {
        try {
            return new Auto(resultSet.getString(FIELD_CAR_BRAND),
                    resultSet.getString(FIELD_CAR_MODEL),
                    resultSet.getLong(FIELD_CAR_MAKE_YEAR),
                    resultSet.getString(FIELD_CAR_COLOR),
                    resultSet.getString(FIELD_CAR_KIND),
                    resultSet.getString(FIELD_CAR_BODY),
                    resultSet.getString(FIELD_CAR_PURPOSE),
                    resultSet.getString(FIELD_CAR_FUEL),
                    resultSet.getLong(FIELD_CAR_ENGINE_CAPACITY),
                    resultSet.getLong(FIELD_CAR_OWN_WEIGHT),
                    resultSet.getLong(FIELD_CAR_TOTAL_WEIGHT));
        } catch (SQLException e) {
            log.error("Error with mapping resultSet row to Auto", e);
        }
        return new Auto();
    }
}
