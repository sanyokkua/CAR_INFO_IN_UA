package ua.kostenko.carinfo.rest.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.common.entities.RegionCodeEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

import static ua.kostenko.carinfo.common.TablesAndFieldsConstants.AdministrativeObjectTable.FIELD_NAME;
import static ua.kostenko.carinfo.common.TablesAndFieldsConstants.AdministrativeObjectTable.FIELD_TYPE_NAME;
import static ua.kostenko.carinfo.common.TablesAndFieldsConstants.RegistrationInformationTable.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Registration {
    private PersonKind person;
    private String administrativeObjectTypeName;
    private String administrativeObjectName;
    private String operationName;
    private String registrationDate;
    private String newRegistrationNumber;
    private RegionCodeEntity registrationNumberRegionName;

    public static Registration map(ResultSet resultSet, RegionCodeEntity registrationNumberRegionName) {
        try {
            return new Registration(PersonKind.getPersonKind(resultSet.getString(FIELD_PERSON)),
                    resultSet.getString(FIELD_TYPE_NAME),
                    resultSet.getString(FIELD_NAME),
                    resultSet.getString(FIELD_OPERATION_NAME),
                    resultSet.getString(FIELD_REGISTRATION_DATE),
                    resultSet.getString(FIELD_CAR_NEW_REG_NUMBER),
                    registrationNumberRegionName);
        } catch (SQLException e) {
            log.error("Error with mapping resultSet row to Registration", e);
        }
        return new Registration();
    }

    public enum PersonKind {
        PERSON,
        JURIDICAL;

        static PersonKind getPersonKind(String person) {
            switch (person) {
                case "P":
                    return PERSON;
                case "J":
                    return JURIDICAL;
                default:
                    return PERSON;
            }
        }
    }
}
