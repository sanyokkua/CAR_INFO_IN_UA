package ua.kostenko.carinfo.common.database.repositories.jdbc.crud;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.ParamsHolder;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;
import ua.kostenko.carinfo.common.database.repositories.FieldSearchable;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Color;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationColorRepository extends CachingJdbcRepository<Color> implements PageableRepository<Color>, FieldSearchable<Color> {
    private static final RowMapper<Color> ROW_MAPPER = (resultSet, i) -> Color.builder()
                                                                              .colorId(resultSet.getLong(Constants.RegistrationColor.ID))
                                                                              .colorName(resultSet.getString(Constants.RegistrationColor.NAME))
                                                                              .build();

    @Autowired
    public RegistrationColorRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, Color> getCacheLoader() {
        return new CacheLoader<String, Color>() {
            @Override
            public Color load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Override
    public Color findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.color where color_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public Color create(@NonNull @Nonnull Color entity) {
        String jdbcTemplateInsert = "insert into carinfo.color (color_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getColorName());
        return getFromCache(entity.getColorName());
    }

    @Nullable
    @Override
    public Color update(@NonNull @Nonnull Color entity) {
        getCache().invalidate(entity.getColorName());
        String jdbcTemplateUpdate = "update carinfo.color set color_name = ? where color_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getColorName(), entity.getColorId());
        return findByField(entity.getColorName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        Color color = find(id);
        if (nonNull(color)) {
            getCache().invalidate(color.getColorName());
        }
        String jdbcTemplateDelete = "delete from carinfo.color where color_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public Color find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.color where color_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public List<Color> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.color;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Color entity) {
        return nonNull(getFromCache(entity.getColorName()));
    }

    @Override
    public void createAll(Iterable<Color> entities) {
        final int batchSize = 100;
        List<List<Color>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<Color> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.color (color_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    Color object = batch.get(i);
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
    public Page<Color> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.color c ";
        String name = searchParams.getString(Constants.RegistrationBrand.NAME);

        String where = buildWhere().add("c.color_name", name).build();

        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Color> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
