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
import ua.kostenko.carinfo.common.records.Kind;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationKindRepository extends CachingJdbcRepository<Kind> implements PageableRepository<Kind>, FieldSearchable<Kind> {
    private static final RowMapper<Kind> ROW_MAPPER = (resultSet, i) -> Kind.builder()
                                                                            .kindId(resultSet.getLong(Constants.RegistrationKind.ID))
                                                                            .kindName(resultSet.getString(Constants.RegistrationKind.NAME))
                                                                            .build();

    @Autowired
    public RegistrationKindRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, Kind> getCacheLoader() {
        return new CacheLoader<String, Kind>() {
            @Override
            public Kind load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Override
    public Kind findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_nameody_type_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public Kind create(@NonNull @Nonnull Kind entity) {
        String jdbcTemplateInsert = "insert into carinfo.kind (kind_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getKindName());
        return getFromCache(entity.getKindName());
    }

    @Nullable
    @Override
    public Kind update(@NonNull @Nonnull Kind entity) {
        getCache().invalidate(entity.getKindName());
        String jdbcTemplateUpdate = "update carinfo.kind set kind_name = ? where kind_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getKindName(), entity.getKindId());
        return findByField(entity.getKindName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        Kind kind = find(id);
        if (nonNull(kind)) {
            getCache().invalidate(kind.getKindName());
        }
        String jdbcTemplateDelete = "delete from carinfo.kind where kind_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public Kind find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(kind_id) from carinfo.kind where kind_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public List<Kind> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.kind;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Kind entity) {
        return nonNull(getFromCache(entity.getKindName()));
    }

    @Override
    public void createAll(Iterable<Kind> entities) {
        final int batchSize = 100;
        List<List<Kind>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<Kind> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.kind (kind_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    Kind object = batch.get(i);
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
    public Page<Kind> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.kind k ";
        String name = searchParams.getString(Constants.RegistrationKind.NAME);

        String where = buildWhere().add("k.kind_name", name).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.kind k " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Kind> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
