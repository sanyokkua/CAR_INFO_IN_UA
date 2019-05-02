package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class RegistrationFuelTypeRepository extends CommonDBRepository<FuelType, String> {
    private static final RowMapper<FuelType> ROW_MAPPER = (resultSet, i) -> FuelType.builder()
                                                                                    .fuelTypeId(resultSet.getLong(Constants.RegistrationFuelType.ID))
                                                                                    .fuelTypeName(resultSet.getString(Constants.RegistrationFuelType.NAME))
                                                                                    .build();

    @Autowired
    public RegistrationFuelTypeRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<FuelType> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public FuelType create(@NonNull @Nonnull FuelType entity) {
        String jdbcTemplateInsert = "insert into carinfo.fuel_type (fuel_type_name) values (:name);";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getFuelTypeName()).build();
        return create(jdbcTemplateInsert, Constants.RegistrationFuelType.ID, parameterSource);
    }

    @Nullable
    @Override
    public FuelType update(@NonNull @Nonnull FuelType entity) {
        String jdbcTemplateUpdate = "update carinfo.fuel_type set fuel_type_name = :name where fuel_type_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam("name", entity.getFuelTypeName())
                .addParam("id", entity.getId())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder searchParams = getParamsHolderBuilder().param(FuelType.FUEL_NAME, entity.getFuelTypeName()).build();
        return findOne(searchParams);
    }

    @Cacheable(cacheNames = "fuelIndex", unless = "#result == false ", key = "#indexField")
    @Override
    public boolean existsByIndex(@Nonnull @NonNull String indexField) {
        String jdbcTemplateSelectCount = "select count(fuel_type_id) from carinfo.fuel_type where fuel_type_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", indexField).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.fuel_type where fuel_type_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(fuel_type_id) from carinfo.fuel_type where fuel_type_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "fuelCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull FuelType entity) {
        String jdbcTemplateSelectCount = "select count(fuel_type_id) from carinfo.fuel_type where fuel_type_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getFuelTypeName()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public FuelType findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.fuel_type ft where fuel_type_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("id", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "fuel", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public FuelType findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(FuelType.FUEL_NAME);
        String jdbcTemplateSelect = "select * from carinfo.fuel_type where fuel_type_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public List<FuelType> find() {
        String jdbcTemplateSelect = "select * from carinfo.fuel_type;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<FuelType> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.fuel_type ft ";
        String name = searchParams.getString(FuelType.FUEL_NAME);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("ft.fuel_type_name", "name", name));
    }
}
