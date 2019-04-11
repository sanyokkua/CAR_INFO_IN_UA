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
import ua.kostenko.carinfo.common.api.Constants;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationModelRepository extends CachingJdbcRepository<Model> implements PageableRepository<Model>, FieldSearchable<Model> {
    private static final RowMapper<Model> ROW_MAPPER = (resultSet, i) -> Model.builder()
                                                                              .modelId(resultSet.getLong(Constants.RegistrationModel.ID))
                                                                              .modelName(resultSet.getString(Constants.RegistrationModel.NAME))
                                                                              .build();

    @Autowired
    public RegistrationModelRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, Model> getCacheLoader() {
        return new CacheLoader<String, Model>() {
            @Override
            public Model load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Override
    public Model findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.model where model_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public Model find(@Nonnull Model entity) {
        return getFromCache(entity.getModelName());
    }

    @Nullable
    @Override
    public Model create(@NonNull @Nonnull Model entity) {
        String jdbcTemplateInsert = "insert into carinfo.model (model_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getModelName());
        return getFromCache(entity.getModelName());
    }

    @Nullable
    @Override
    public Model update(@NonNull @Nonnull Model entity) {
        getCache().invalidate(entity.getModelName());
        String jdbcTemplateUpdate = "update carinfo.model set model_name = ? where model_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getModelName(), entity.getModelId());
        return findByField(entity.getModelName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        Model model = find(id);
        if (nonNull(model)) {
            getCache().invalidate(model.getModelName());
        }
        String jdbcTemplateDelete = "delete from carinfo.model where model_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public Model find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.model where model_id = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(model_id) from carinfo.model where model_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public List<Model> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.model;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Model entity) {
        return nonNull(getFromCache(entity.getModelName()));
    }

    @Override
    public void createAll(Iterable<Model> entities) {
        final int batchSize = 100;
        List<List<Model>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<Model> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.model (model_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    Model object = batch.get(i);
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
    public Page<Model> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.model m ";
        String name = searchParams.getString(Constants.RegistrationModel.NAME);

        String where = buildWhere().add("m.model_name", name).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.model m " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Model> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
