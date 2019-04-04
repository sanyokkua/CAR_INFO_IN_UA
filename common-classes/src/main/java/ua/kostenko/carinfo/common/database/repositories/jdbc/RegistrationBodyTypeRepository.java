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
import ua.kostenko.carinfo.common.database.raw.RegistrationBodyType;
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
public class RegistrationBodyTypeRepository extends CachingJdbcRepository<RegistrationBodyType> implements FieldSearchable<RegistrationBodyType> {
    private static final RowMapper<RegistrationBodyType> ROW_MAPPER = (resultSet, i) -> RegistrationBodyType.builder()
                                                                                                            .bodyTypeId(resultSet.getLong(Constants.RegistrationBodyType.ID))
                                                                                                            .bodyTypeName(resultSet.getString(Constants.RegistrationBodyType.NAME))
                                                                                                            .build();

    @Autowired
    public RegistrationBodyTypeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationBodyType> getCacheLoader() {
        return new CacheLoader<String, RegistrationBodyType>() {
            @Override
            public RegistrationBodyType load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Override
    public RegistrationBodyType findByField(@Nonnull @NonNull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public RegistrationBodyType create(@NonNull @Nonnull RegistrationBodyType entity) {
        String jdbcTemplateInsert = "insert into carinfo.body_type (body_type_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getBodyTypeName());
        return getFromCache(entity.getBodyTypeName());
    }

    @Nullable
    @Override
    public RegistrationBodyType update(@NonNull @Nonnull RegistrationBodyType entity) {
        getCache().invalidate(entity.getBodyTypeName());
        String jdbcTemplateUpdate = "update carinfo.body_type set body_type_name = ? where body_type_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getBodyTypeName(), entity.getBodyTypeId());
        return findByField(entity.getBodyTypeName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        RegistrationBodyType administrativeObject = find(id);
        if (nonNull(administrativeObject)) {
            getCache().invalidate(administrativeObject.getBodyTypeName());
        }
        String jdbcTemplateDelete = "delete from carinfo.body_type where body_type_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public RegistrationBodyType find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationBodyType> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.body_type;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(body_type_id) from carinfo.body_type where body_type_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationBodyType entity) {
        return nonNull(getFromCache(entity.getBodyTypeName()));
    }

    @Override
    public void createAll(Iterable<RegistrationBodyType> entities) {
        final int batchSize = 100;
        List<List<RegistrationBodyType>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationBodyType> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.body_type (body_type_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    RegistrationBodyType object = batch.get(i);
                    ps.setString(1, object.getBodyTypeName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }
}
