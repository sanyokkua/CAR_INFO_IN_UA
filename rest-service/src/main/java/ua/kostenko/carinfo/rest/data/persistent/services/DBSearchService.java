package ua.kostenko.carinfo.rest.data.persistent.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.entities.RegionCodeEntity;
import ua.kostenko.carinfo.common.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.rest.data.persistent.repositories.RegionCodeCrudRepository;
import ua.kostenko.carinfo.rest.data.persistent.repositories.RegistrationInformationCrudRepository;
import ua.kostenko.carinfo.rest.data.presentation.Auto;
import ua.kostenko.carinfo.rest.data.presentation.CombinedInformation;
import ua.kostenko.carinfo.rest.data.presentation.Registration;
import ua.kostenko.carinfo.rest.data.presentation.ServiceCenter;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.kostenko.carinfo.common.TablesAndFieldsConstants.*;

@Service
@Transactional
@Slf4j
public class DBSearchService implements SearchService {
    private final RegistrationInformationCrudRepository regInfoCRUD;
    private final DataSource dataSource;
    private final RegionCodeCrudRepository regionCodeCRUD;

    @Autowired
    public DBSearchService(RegistrationInformationCrudRepository regInfoCRUD, DataSource dataSource, RegionCodeCrudRepository regionCodeCRUD) {
        Preconditions.checkNotNull(regInfoCRUD);
        Preconditions.checkNotNull(dataSource);
        Preconditions.checkNotNull(regionCodeCRUD);
        this.regInfoCRUD = regInfoCRUD;
        this.dataSource = dataSource;
        this.regionCodeCRUD = regionCodeCRUD;
    }

    @Override
    public List<RegistrationInformationEntity> search(String value) {
        log.info("Searching RegistrationInformationEntity for field: {}, value: {}", "CarNewRegistrationNumber", value);
        List<RegistrationInformationEntity> results = regInfoCRUD.findAllByCarNewRegistrationNumberLike(value);
        log.warn("Field {} is not supported yet, null will be returned", "CarNewRegistrationNumber");
        return results;
    }

    /*
    SELECT registration_information.*, administrative_object.*, service_center.* FROM registration_information
                        JOIN administrative_object
                            ON administrative_object.id = registration_information.administrative_object_code
                        JOIN service_center
                            ON service_center.dep_code = registration_information.department_code
                    WHERE registration_information.car_new_registration_number = 'АХ3670ЕХ'
    */
    @Override
    public List<CombinedInformation> searchAllByRegistrationNumber(String number) {
        try (Connection c = dataSource.getConnection()) {
            String sqlTemplate = "SELECT * " +
                    "FROM ${registration_information} " +
                    "JOIN ${administrative_object} ON ${administrative_object}.${id} = ${registration_information}.${administrative_object_code} " +
                    "JOIN ${service_center} ON ${service_center}.${dep_code} = ${registration_information}.${department_code} " +
                    "WHERE ${registration_information}.${car_new_registration_number} = '${value}'";
            
            Map<String, String> params = new HashMap<>();
            params.put("registration_information", RegistrationInformationTable.TABLE);
            params.put("administrative_object", AdministrativeObjectTable.TABLE);
            params.put("service_center", ServiceCenterTable.TABLE);

            params.put("id", AdministrativeObjectTable.FIELD_ID);
            params.put("administrative_object_code", RegistrationInformationTable.FIELD_ADMIN_OBJECT_CODE);

            params.put("dep_code", ServiceCenterTable.FIELD_DEPARTMENT_CODE);
            params.put("department_code", RegistrationInformationTable.FIELD_DEPARTMENT_CODE);

            params.put("car_new_registration_number", RegistrationInformationTable.FIELD_CAR_NEW_REG_NUMBER);
            params.put("value", number);

            StrSubstitutor strSubstitutor = new StrSubstitutor(params);

            return new NamedParameterJdbcTemplate(new SingleConnectionDataSource(c, true)).query(strSubstitutor.replace(sqlTemplate), params, (resultSet, i) -> {
                String car_new_registration_number = resultSet.getString(RegistrationInformationTable.FIELD_CAR_NEW_REG_NUMBER);
                String code = car_new_registration_number.substring(0, 2);
                RegionCodeEntity defaultIfEmpty = new RegionCodeEntity("", "");
                RegionCodeEntity registrationNumberRegionName = regionCodeCRUD.findById(code).orElse(defaultIfEmpty);

                Auto auto = Auto.map(resultSet);
                Registration registration = Registration.map(resultSet, registrationNumberRegionName);
                ServiceCenter serviceCenter = ServiceCenter.map(resultSet);
                return new CombinedInformation(auto, registration, serviceCenter);
            });
        } catch (SQLException e) {
            log.error("Exception with SQL query", e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllBrands() {
        return null;
    }

    @Override
    public List<String> getAllModelsForBrand(String brand) {
        return null;
    }

    @Override
    public List<String> getAllColors() {
        return null;
    }

    @Override
    public List<String> getAllFuelTypes() {
        return null;
    }

    @Override
    public List<String> getAllCarKinds() {
        return null;
    }

    @Override
    public List<String> getAllRegions() {
        return null;
    }

    @Override
    public int countAllRegistrations() {
        return 0;
    }

    @Override
    public int countAllByBrand(String brand) {
        return 0;
    }

    @Override
    public int countAllByCarBrandAndModel(String brand, String model) {
        return 0;
    }

    @Override
    public int countAllByCarColor(String color) {
        return 0;
    }

    @Override
    public int countAllByFuelType(String fuelType) {
        return 0;
    }

    @Override
    public int countAllByCarKind(String carKind) {
        return 0;
    }

    @Override
    public int countAllCarsByYear(int year) {
        return 0;
    }

    @Override
    public int countAllCarsInRegion(String region) {
        return 0;
    }

}
