package ua.kostenko.carinfo.rest.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.kostenko.carinfo.common.TablesAndFieldsConstants.RegistrationInformationTable.FIELD_DEPARTMENT_CODE;
import static ua.kostenko.carinfo.common.TablesAndFieldsConstants.RegistrationInformationTable.FIELD_DEPARTMENT_NAME;
import static ua.kostenko.carinfo.common.TablesAndFieldsConstants.ServiceCenterTable.FIELD_ADDRESS;
import static ua.kostenko.carinfo.common.TablesAndFieldsConstants.ServiceCenterTable.FIELD_EMAIL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ServiceCenter {
    long numberOfServiceCenter;
    String address;
    String email;
    private String name;

    public static ServiceCenter map(ResultSet resultSet) {
        try {
            return new ServiceCenter(resultSet.getLong(FIELD_DEPARTMENT_CODE),
                    resultSet.getString(FIELD_ADDRESS),
                    resultSet.getString(FIELD_EMAIL),
                    resultSet.getString(FIELD_DEPARTMENT_NAME));
        } catch (SQLException e) {
            log.error("Error with mapping resultSet row to ServiceCenter", e);
        }
        return new ServiceCenter();
    }
}
