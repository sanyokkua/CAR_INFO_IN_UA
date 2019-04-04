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
import ua.kostenko.carinfo.common.database.raw.RegistrationKind;
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
public class RegistrationKindRepository extends CachingJdbcRepository<RegistrationKind> implements FieldSearchable<RegistrationKind> {
    private static final RowMapper<RegistrationKind> ROW_MAPPER = (resultSet, i) -> RegistrationKind.builder()
                                                                                                    .kindId(resultSet.getLong(Constants.RegistrationKind.ID))
                                                                                                    .kindName(resultSet.getString(Constants.RegistrationKind.NAME))
                                                                                                    .build();
    @Autowired
    public RegistrationKindRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationKind> getCacheLoader() {
        return new CacheLoader<String, RegistrationKind>() {
            @Override
            public RegistrationKind load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationKind create(@NonNull @Nonnull RegistrationKind entity) {
        String jdbcTemplateInsert = "insert into carinfo.kind (kind_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getKindName());
        return getFromCache(entity.getKindName());
    }

    @Nullable
    @Override
    public RegistrationKind update(@NonNull @Nonnull RegistrationKind entity) {
        getCache().invalidate(entity.getKindName());
        String jdbcTemplateUpdate = "update carinfo.kind set kind_name = ? where kind_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getKindName(), entity.getKindId());
        return findByField(entity.getKindName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        RegistrationKind kind = find(id);
        if (nonNull(kind)) {
            getCache().invalidate(kind.getKindName());
        }
        String jdbcTemplateDelete = "delete from carinfo.kind where kind_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public RegistrationKind find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationKind> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.kind;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(kind_id) from carinfo.kind where kind_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationKind entity) {
        return nonNull(getFromCache(entity.getKindName()));
    }

    @Override
    public void createAll(Iterable<RegistrationKind> entities) {
        final int batchSize = 100;
        List<List<RegistrationKind>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationKind> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.kind (kind_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    RegistrationKind object = batch.get(i);
                    ps.setString(1, object.getKindName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public RegistrationKind findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_nameody_type_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }
}
