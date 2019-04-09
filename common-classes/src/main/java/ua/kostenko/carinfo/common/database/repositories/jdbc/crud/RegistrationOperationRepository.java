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
import ua.kostenko.carinfo.common.records.Operation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationOperationRepository extends CachingJdbcRepository<Operation> implements PageableRepository<Operation>, FieldSearchable<Operation> {
    private static final RowMapper<Operation> ROW_MAPPER = (resultSet, i) -> Operation.builder()
                                                                                      .operationCode(resultSet.getLong(Constants.RegistrationOperation.CODE))
                                                                                      .operationName(resultSet.getString(Constants.RegistrationOperation.NAME))
                                                                                      .build();

    @Autowired
    public RegistrationOperationRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, Operation> getCacheLoader() {
        return new CacheLoader<String, Operation>() {
            @Override
            public Operation load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Override
    public Operation findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.operation where op_name = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect,
                                                  ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public Operation create(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateInsert = "insert into carinfo.operation (op_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getOperationName());
        return getFromCache(entity.getOperationName());
    }

    @Nullable
    @Override
    public Operation update(@NonNull @Nonnull Operation entity) {
        getCache().invalidate(entity.getOperationName());
        String jdbcTemplateUpdate = "update carinfo.operation set op_name = ? where op_code = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getOperationName(), entity.getOperationCode());
        return findByField(entity.getOperationName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        Operation operation = find(id);
        if (nonNull(operation)) {
            getCache().invalidate(operation.getOperationName());
        }
        String jdbcTemplateDelete = "delete from carinfo.operation where op_code = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public Operation find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.operation where op_code = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(op_code) from carinfo.operation where op_code = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public List<Operation> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.operation;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Operation entity) {
        return nonNull(getFromCache(entity.getOperationName()));
    }

    @Override
    public void createAll(Iterable<Operation> entities) {
        final int batchSize = 100;
        List<List<Operation>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<Operation> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.operation (op_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    Operation object = batch.get(i);
                    ps.setString(1, object.getOperationName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public Page<Operation> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.operation o ";
        String name = searchParams.getString(Constants.RegistrationOperation.NAME);

        String where = buildWhere().add("o.op_name", name).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.operation o " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Operation> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
