package ua.kostenko.carinfo.rest.data.persistent.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.entities.RegionCodeEntity;
import ua.kostenko.carinfo.rest.data.persistent.repositories.RegionCodeCrudRepository;
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
    private final DataSource dataSource;
    private final RegionCodeCrudRepository regionCodeCRUD;

    @Autowired
    public DBSearchService(DataSource dataSource, RegionCodeCrudRepository regionCodeCRUD) {
        Preconditions.checkNotNull(dataSource);
        Preconditions.checkNotNull(regionCodeCRUD);
        this.dataSource = dataSource;
        this.regionCodeCRUD = regionCodeCRUD;
    }


    @Override
    public List<CombinedInformation> searchAllByRegistrationNumber(String number) {
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
        String sqlTemplate = new StringSubstitutor(params).replace("SELECT * " +
                "FROM ${registration_information} " +
                "JOIN ${administrative_object} ON ${administrative_object}.${id} = ${registration_information}.${administrative_object_code} " +
                "JOIN ${service_center} ON ${service_center}.${dep_code} = ${registration_information}.${department_code} " +
                "WHERE ${registration_information}.${car_new_registration_number} = '${value}'");

        try (Connection c = dataSource.getConnection()) {
            return new JdbcTemplate(new SingleConnectionDataSource(c, true)).query(sqlTemplate, (resultSet, i) -> {
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
        return findAllDistinctFromTableByField(RegistrationInformationTable.FIELD_CAR_BRAND);
    }

    private List<String> findAllDistinctFromTableByField(String field) {
        Map<String, String> params = new HashMap<>();
        params.put("table", RegistrationInformationTable.TABLE);
        params.put("field", field);
        StringSubstitutor StringSubstitutor = new StringSubstitutor(params);
        String sql = StringSubstitutor.replace("SELECT distinct ${field} FROM ${table} order by ${field}");
        try (Connection c = dataSource.getConnection()) {
            return new JdbcTemplate(new SingleConnectionDataSource(c, true)).query(sql, (resultSet, i) -> resultSet.getString(field));
        } catch (SQLException e) {
            log.error("Exception with SQL query", e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllModelsForBrand(String brand) {
        Map<String, String> params = new HashMap<>();
        params.put("table", RegistrationInformationTable.TABLE);
        params.put("fieldToSelect", RegistrationInformationTable.FIELD_CAR_MODEL);
        params.put("fieldToCompare", RegistrationInformationTable.FIELD_CAR_BRAND);
        params.put("value", brand);
        StringSubstitutor StringSubstitutor = new StringSubstitutor(params);
        String sql = StringSubstitutor.replace("SELECT distinct ${fieldToSelect} FROM ${table} where ${fieldToCompare}='${value}' order by ${fieldToSelect}");
        try (Connection c = dataSource.getConnection()) {
            return new JdbcTemplate(new SingleConnectionDataSource(c, true)).query(sql, (resultSet, i) -> resultSet.getString(RegistrationInformationTable.FIELD_CAR_MODEL));
        } catch (SQLException e) {
            log.error("Exception with SQL query", e);
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllColors() {
        return findAllDistinctFromTableByField(RegistrationInformationTable.FIELD_CAR_COLOR);
    }

    @Override
    public List<String> getAllFuelTypes() {
        return findAllDistinctFromTableByField(RegistrationInformationTable.FIELD_CAR_FUEL);
    }

    @Override
    public List<String> getAllCarKinds() {
        return findAllDistinctFromTableByField(RegistrationInformationTable.FIELD_CAR_KIND);
    }

    @Override
    public List<String> getAllRegions() {
        String sql = String.format("SELECT * FROM %s", RegionCodeTable.TABLE);
        try (Connection c = dataSource.getConnection()) {
            return new JdbcTemplate(new SingleConnectionDataSource(c, true)).query(sql, (resultSet, i) -> resultSet.getString(RegionCodeTable.FIELD_REGION));
        } catch (SQLException e) {
            log.error("Exception with SQL query", e);
        }
        return Collections.emptyList();
    }

    @Override
    public long countAllRegistrations() {
        Map<String, String> params = new HashMap<>();
        params.put("table", RegistrationInformationTable.TABLE);
        params.put("field", RegistrationInformationTable.FIELD_CAR_NEW_REG_NUMBER);
        StringSubstitutor StringSubstitutor = new StringSubstitutor(params);
        String sql = StringSubstitutor.replace("select count(${field}) from ${table}");
        try (Connection c = dataSource.getConnection()) {
            return new JdbcTemplate(new SingleConnectionDataSource(c, true)).queryForObject(sql, Long.class);
        } catch (NullPointerException | SQLException e) {
            log.error("Exception with SQL query", e);
        }
        return 0;
    }

    private long countForTableByField(String field, String value) {
        Map<String, String> params = new HashMap<>();
        params.put("table", RegistrationInformationTable.TABLE);
        params.put("field", field);
        params.put("value", value);
        StringSubstitutor StringSubstitutor = new StringSubstitutor(params);
        String sql = StringSubstitutor.replace("select count(${field}) from ${table} where ${field} = '${value}'");
        try (Connection c = dataSource.getConnection()) {
            return new JdbcTemplate(new SingleConnectionDataSource(c, true)).queryForObject(sql, Long.class);
        } catch (NullPointerException | SQLException e) {
            log.error("Exception with SQL query", e);
        }
        return 0;
    }

    @Override
    public long countAllByBrand(String brand) {
        return countForTableByField(RegistrationInformationTable.FIELD_CAR_BRAND, brand);
    }

    @Override
    public long countAllByCarBrandAndModel(String brand, String model) {
        Map<String, String> params = new HashMap<>();
        params.put("table", RegistrationInformationTable.TABLE);
        params.put("fieldModel", RegistrationInformationTable.FIELD_CAR_MODEL);
        params.put("fieldBrand", RegistrationInformationTable.FIELD_CAR_BRAND);
        params.put("valueBrand", brand);
        params.put("valueModel", model);
        StringSubstitutor StringSubstitutor = new StringSubstitutor(params);
        String sql = StringSubstitutor.replace("select count(${fieldBrand}) from ${table} where ${fieldBrand} = '${valueBrand}' and ${fieldModel} = '${valueModel}'");
        try (Connection c = dataSource.getConnection()) {
            return new JdbcTemplate(new SingleConnectionDataSource(c, true)).queryForObject(sql, Long.class);
        } catch (NullPointerException | SQLException e) {
            log.error("Exception with SQL query", e);
        }
        return 0;
    }

    @Override
    public long countAllByCarColor(String color) {
        return countForTableByField(RegistrationInformationTable.FIELD_CAR_COLOR, color);
    }

    @Override
    public long countAllByFuelType(String fuelType) {
        return countForTableByField(RegistrationInformationTable.FIELD_CAR_FUEL, fuelType);
    }

    @Override
    public long countAllByCarKind(String carKind) {
        return countForTableByField(RegistrationInformationTable.FIELD_CAR_KIND, carKind);
    }

    @Override
    public long countAllCarsByYear(int year) {
        return countForTableByField(RegistrationInformationTable.FIELD_CAR_MAKE_YEAR, "" + year);
    }

    @Override
    public long countAllCarsInRegion(String region) {
        final RegionCodeEntity codeEntity = regionCodeCRUD.findByRegion(region);
        Map<String, String> params = new HashMap<>();
        params.put("table", RegistrationInformationTable.TABLE);
        params.put("field", RegistrationInformationTable.FIELD_CAR_NEW_REG_NUMBER);
        params.put("value", codeEntity.getCode());
        StringSubstitutor StringSubstitutor = new StringSubstitutor(params);
        String sql = StringSubstitutor.replace("select count(distinct ${table}) from ${table} where ${field} like '${value}%'");
        try (Connection c = dataSource.getConnection()) {
            return new JdbcTemplate(new SingleConnectionDataSource(c, true)).queryForObject(sql, Long.class);
        } catch (NullPointerException | SQLException e) {
            log.error("Exception with SQL query", e);
        }
        return 0;
    }

}
