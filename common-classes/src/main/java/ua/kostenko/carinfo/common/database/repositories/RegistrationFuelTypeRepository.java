package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.Utils;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class RegistrationFuelTypeRepository extends CommonDBRepository<FuelType> {
    private static final RowMapper<FuelType> ROW_MAPPER = (resultSet, i) -> FuelType.builder()
                                                                                    .fuelTypeId(resultSet.getLong(Constants.RegistrationFuelType.ID))
                                                                                    .fuelTypeName(resultSet.getString(Constants.RegistrationFuelType.NAME))
                                                                                    .build();

    @Autowired
    public RegistrationFuelTypeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public FuelType create(@NonNull @Nonnull FuelType entity) {
        String jdbcTemplateInsert = "insert into carinfo.fuel_type (fuel_type_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getFuelTypeName());
        ParamsHolder searchParams = getBuilder().param(FuelType.FUEL_NAME, entity.getFuelTypeName()).build();
        return findOne(searchParams);
    }

    @Nullable
    @Override
    public FuelType update(@NonNull @Nonnull FuelType entity) {
        String jdbcTemplateUpdate = "update carinfo.fuel_type set fuel_type_name = ? where fuel_type_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getFuelTypeName(), entity.getFuelTypeId());
        ParamsHolder searchParams = getBuilder().param(FuelType.FUEL_NAME, entity.getFuelTypeName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.fuel_type where fuel_type_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(fuel_type_id) from carinfo.fuel_type where fuel_type_id = ?;";
        long numberOfRows = jdbcTemplate.query(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id).stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull FuelType entity) {
        String jdbcTemplateSelectCount = "select count(fuel_type_id) from carinfo.fuel_type where fuel_type_name = ?;";
        long numberOfRows = jdbcTemplate.query(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), entity.getFuelTypeName())
                                        .stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    @Nullable
    @Override
    public FuelType findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.fuel_type ft where fuel_type_id = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER, id).stream().findFirst().orElse(null));
    }

    @Cacheable(cacheNames = "fuel", unless = "#result != null")
    @Nullable
    @Override
    public FuelType findOne(@Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(FuelType.FUEL_NAME);
        String jdbcTemplateSelect = "select * from carinfo.fuel_type where fuel_type_name = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER, param)
                                                                      .stream()
                                                                      .findFirst()
                                                                      .orElse(null));
    }

    @Override
    public List<FuelType> find() {
        String jdbcTemplateSelect = "select * from carinfo.fuel_type;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public Page<FuelType> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.fuel_type ft ";
        String name = searchParams.getString(FuelType.FUEL_NAME);

        String where = buildWhere().add("ft.fuel_type_name", name, true).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.fuel_type ft " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<FuelType> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
