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
import ua.kostenko.carinfo.common.records.BodyType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationBodyTypeRepository extends CachingJdbcRepository<BodyType> implements PageableRepository<BodyType>, FieldSearchable<BodyType> {
    private static final RowMapper<BodyType> ROW_MAPPER = (resultSet, i) -> BodyType.builder()
                                                                                    .bodyTypeId(resultSet.getLong(Constants.RegistrationBodyType.ID))
                                                                                    .bodyTypeName(resultSet.getString(Constants.RegistrationBodyType.NAME))
                                                                                    .build();

    @Autowired
    public RegistrationBodyTypeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, BodyType> getCacheLoader() {
        return new CacheLoader<String, BodyType>() {
            @Override
            public BodyType load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Override
    public BodyType findByField(@Nonnull @NonNull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public BodyType find(@Nonnull BodyType entity) {
        return findByField(entity.getBodyTypeName());
    }

    @Nullable
    @Override
    public BodyType create(@NonNull @Nonnull BodyType entity) {
        String jdbcTemplateInsert = "insert into carinfo.body_type (body_type_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getBodyTypeName());
        return getFromCache(entity.getBodyTypeName());
    }

    @Nullable
    @Override
    public BodyType update(@NonNull @Nonnull BodyType entity) {
        getCache().invalidate(entity.getBodyTypeName());
        String jdbcTemplateUpdate = "update carinfo.body_type set body_type_name = ? where body_type_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getBodyTypeName(), entity.getBodyTypeId());
        return findByField(entity.getBodyTypeName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        BodyType administrativeObject = find(id);
        if (nonNull(administrativeObject)) {
            getCache().invalidate(administrativeObject.getBodyTypeName());
        }
        String jdbcTemplateDelete = "delete from carinfo.body_type where body_type_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public BodyType find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<BodyType> findAll() {
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
    public boolean isExists(@NonNull @Nonnull BodyType entity) {
        return nonNull(getFromCache(entity.getBodyTypeName()));
    }

    @Override
    public void createAll(Iterable<BodyType> entities) {
        final int batchSize = 100;
        List<List<BodyType>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<BodyType> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.body_type (body_type_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    BodyType object = batch.get(i);
                    ps.setString(1, object.getBodyTypeName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public Page<BodyType> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.body_type bt ";
        String name = searchParams.getString(Constants.RegistrationBodyType.NAME);

        String where = buildWhere().add("bt.body_type_name", name).build();

        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<BodyType> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
