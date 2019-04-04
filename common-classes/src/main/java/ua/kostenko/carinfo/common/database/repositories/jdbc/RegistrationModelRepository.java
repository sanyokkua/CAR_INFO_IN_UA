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
import ua.kostenko.carinfo.common.database.raw.RegistrationModel;
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
public class RegistrationModelRepository extends CachingJdbcRepository<RegistrationModel> implements FieldSearchable<RegistrationModel> {
    private static final RowMapper<RegistrationModel> ROW_MAPPER = (resultSet, i) -> RegistrationModel.builder()
                                                                                                      .modelId(resultSet.getLong(Constants.RegistrationModel.ID))
                                                                                                      .modelName(resultSet.getString(Constants.RegistrationModel.NAME))
                                                                                                      .build();
    @Autowired
    public RegistrationModelRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationModel> getCacheLoader() {
        return new CacheLoader<String, RegistrationModel>() {
            @Override
            public RegistrationModel load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationModel create(@NonNull @Nonnull RegistrationModel entity) {
        String jdbcTemplateInsert = "insert into carinfo.model (model_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getModelName());
        return getFromCache(entity.getModelName());
    }

    @Nullable
    @Override
    public RegistrationModel update(@NonNull @Nonnull RegistrationModel entity) {
        getCache().invalidate(entity.getModelName());
        String jdbcTemplateUpdate = "update carinfo.model set model_name = ? where model_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getModelName(), entity.getModelId());
        return findByField(entity.getModelName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        RegistrationModel model = find(id);
        if (nonNull(model)) {
            getCache().invalidate(model.getModelName());
        }
        String jdbcTemplateDelete = "delete from carinfo.model where model_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public RegistrationModel find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.model where model_id = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationModel> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.model;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(model_id) from carinfo.model where model_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationModel entity) {
        return nonNull(getFromCache(entity.getModelName()));
    }

    @Override
    public void createAll(Iterable<RegistrationModel> entities) {
        final int batchSize = 100;
        List<List<RegistrationModel>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationModel> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.model (model_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    RegistrationModel object = batch.get(i);
                    ps.setString(1, object.getModelName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public RegistrationModel findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.model where model_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }
}
