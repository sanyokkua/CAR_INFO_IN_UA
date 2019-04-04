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
import ua.kostenko.carinfo.common.database.raw.RegistrationColor;
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
public class RegistrationColorRepository extends CachingJdbcRepository<RegistrationColor> implements FieldSearchable<RegistrationColor> {
    private static final RowMapper<RegistrationColor> ROW_MAPPER = (resultSet, i) -> RegistrationColor.builder()
                                                                                                      .colorId(resultSet.getLong(Constants.RegistrationColor.ID))
                                                                                                      .colorName(resultSet.getString(Constants.RegistrationColor.NAME))
                                                                                                      .build();
    @Autowired
    public RegistrationColorRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationColor> getCacheLoader() {
        return new CacheLoader<String, RegistrationColor>() {
            @Override
            public RegistrationColor load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationColor create(@NonNull @Nonnull RegistrationColor entity) {
        String jdbcTemplateInsert = "insert into carinfo.color (color_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getColorName());
        return getFromCache(entity.getColorName());
    }

    @Nullable
    @Override
    public RegistrationColor update(@NonNull @Nonnull RegistrationColor entity) {
        getCache().invalidate(entity.getColorName());
        String jdbcTemplateUpdate = "update carinfo.color set color_name = ? where color_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getColorName(), entity.getColorId());
        return findByField(entity.getColorName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        RegistrationColor color = find(id);
        if (nonNull(color)) {
            getCache().invalidate(color.getColorName());
        }
        String jdbcTemplateDelete = "delete from carinfo.color where color_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public RegistrationColor find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.color where color_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationColor> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.color;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationColor entity) {
        return nonNull(getFromCache(entity.getColorName()));
    }

    @Override
    public void createAll(Iterable<RegistrationColor> entities) {
        final int batchSize = 100;
        List<List<RegistrationColor>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationColor> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.color (color_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    RegistrationColor object = batch.get(i);
                    ps.setString(1, object.getColorName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public RegistrationColor findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.color where color_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }
}
