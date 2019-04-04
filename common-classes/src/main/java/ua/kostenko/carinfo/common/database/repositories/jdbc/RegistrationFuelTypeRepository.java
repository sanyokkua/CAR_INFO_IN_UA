package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.raw.RegistrationFuelType;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;
import ua.kostenko.carinfo.common.database.repositories.FieldSearchable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationFuelTypeRepository extends CachingJdbcRepository<RegistrationFuelType> implements FieldSearchable<RegistrationFuelType> {
    private static final RowMapper<RegistrationFuelType> ROW_MAPPER = (resultSet, i) -> RegistrationFuelType.builder()
                                                                                                            .fuelTypeId(resultSet.getLong(Constants.RegistrationFuelType.ID))
                                                                                                            .fuelTypeName(resultSet.getString(Constants.RegistrationFuelType.NAME))
                                                                                                            .build();
    @Autowired
    public RegistrationFuelTypeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationFuelType> getCacheLoader() {
        return new CacheLoader<String, RegistrationFuelType>() {
            @Override
            public RegistrationFuelType load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationFuelType create(@NonNull @Nonnull RegistrationFuelType entity) {
        String jdbcTemplateInsert = "insert into carinfo.fuel_type (fuel_type_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getFuelTypeName());
        return getFromCache(entity.getFuelTypeName());
    }

    @Nullable
    @Override
    public RegistrationFuelType update(@NonNull @Nonnull RegistrationFuelType entity) {
        getCache().invalidate(entity.getFuelTypeName());
        String jdbcTemplateUpdate = "update carinfo.fuel_type set fuel_type_name = ? where fuel_type_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getFuelTypeName(), entity.getFuelTypeId());
        return findByField(entity.getFuelTypeName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        RegistrationFuelType fuelType = find(id);
        if (nonNull(fuelType)) {
            getCache().invalidate(fuelType.getFuelTypeName());
        }
        String jdbcTemplateDelete = "delete from carinfo.fuel_type where fuel_type_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public RegistrationFuelType find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.fuel_type ft where fuel_type_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationFuelType> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.fuel_type;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(fuel_type_id) from carinfo.fuel_type where fuel_type_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationFuelType entity) {
        return nonNull(getFromCache(entity.getFuelTypeName()));
    }

    @Override
    public void createAll(Iterable<RegistrationFuelType> entities) {
        final int batchSize = 100;
        List<List<RegistrationFuelType>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationFuelType> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.fuel_type (fuel_type_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    RegistrationFuelType object = batch.get(i);
                    ps.setString(1, object.getFuelTypeName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public RegistrationFuelType findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.fuel_type where fuel_type_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }
}
