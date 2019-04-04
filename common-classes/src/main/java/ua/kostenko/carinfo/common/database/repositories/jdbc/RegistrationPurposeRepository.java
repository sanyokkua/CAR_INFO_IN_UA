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
import ua.kostenko.carinfo.common.database.raw.RegistrationPurpose;
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
public class RegistrationPurposeRepository extends CachingJdbcRepository<RegistrationPurpose> implements FieldSearchable<RegistrationPurpose> {
    private static final RowMapper<RegistrationPurpose> ROW_MAPPER = (resultSet, i) -> RegistrationPurpose.builder()
                                                                                                          .purposeId(resultSet.getLong(Constants.RegistrationPurpose.ID))
                                                                                                          .purposeName(resultSet.getString(Constants.RegistrationPurpose.NAME))
                                                                                                          .build();
    @Autowired
    public RegistrationPurposeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationPurpose> getCacheLoader() {
        return new CacheLoader<String, RegistrationPurpose>() {
            @Override
            public RegistrationPurpose load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationPurpose create(@NonNull @Nonnull RegistrationPurpose entity) {
        String jdbcTemplateInsert = "insert into carinfo.purpose (purpose_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getPurposeName());
        return getFromCache(entity.getPurposeName());
    }

    @Nullable
    @Override
    public RegistrationPurpose update(@NonNull @Nonnull RegistrationPurpose entity) {
        getCache().invalidate(entity.getPurposeName());
        String jdbcTemplateUpdate = "update carinfo.purpose set purpose_name = ? where purpose_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getPurposeName(), entity.getPurposeId());
        return findByField(entity.getPurposeName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        RegistrationPurpose purpose = find(id);
        if (nonNull(purpose)) {
            getCache().invalidate(purpose.getPurposeName());
        }
        String jdbcTemplateDelete = "delete from carinfo.purpose where purpose_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public RegistrationPurpose find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_id = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationPurpose> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.purpose;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(purpose_id) from carinfo.purpose where purpose_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationPurpose entity) {
        return nonNull(getFromCache(entity.getPurposeName()));
    }

    @Override
    public void createAll(Iterable<RegistrationPurpose> entities) {
        final int batchSize = 100;
        List<List<RegistrationPurpose>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationPurpose> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.purpose (purpose_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    RegistrationPurpose object = batch.get(i);
                    ps.setString(1, object.getPurposeName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public RegistrationPurpose findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_name = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect,
                                                  ROW_MAPPER, fieldValue));
    }
}
