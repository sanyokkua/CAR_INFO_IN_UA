package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.Utils;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
class RegistrationOperationRepository extends CommonDBRepository<Operation> {
    private static final RowMapper<Operation> ROW_MAPPER = (resultSet, i) -> Operation.builder()
                                                                                      .operationCode(resultSet.getLong(Constants.RegistrationOperation.CODE))
                                                                                      .operationName(resultSet.getString(Constants.RegistrationOperation.NAME))
                                                                                      .build();

    @Autowired
    public RegistrationOperationRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public Operation create(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateInsert = "insert into carinfo.operation (op_code, op_name) values (?, ?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getOperationCode(), entity.getOperationName());
        ParamsHolder searchParams = getBuilder().param(Operation.OPERATION_NAME, entity.getOperationName()).build();
        return findOne(searchParams);
    }

    @Nullable
    @Override
    public Operation update(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateUpdate = "update carinfo.operation set op_name = ? where op_code = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getOperationName(), entity.getOperationCode());
        ParamsHolder searchParams = getBuilder().param(Operation.OPERATION_NAME, entity.getOperationName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.operation where op_code = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(op_code) from carinfo.operation where op_code = ?;";
        Long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return Objects.nonNull(numberOfRows) && numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull Operation entity) {
        ParamsHolder searchParams = getBuilder().param(Operation.OPERATION_NAME, entity.getOperationName()).build();
        return nonNull(findOne(searchParams));
    }

    @Nullable
    @Override
    public Operation findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.operation where op_code = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Nullable
    @Override
    public Operation findOne(@Nonnull ParamsHolder searchParams) {
        String operationName = searchParams.getString(Operation.OPERATION_NAME);
        String jdbcTemplateSelect = "select * from carinfo.operation where op_name = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, operationName));
    }

    @Override
    public List<Operation> find() {
        String jdbcTemplateSelect = "select * from carinfo.operation;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public Page<Operation> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.operation o ";
        String name = searchParams.getString(Operation.OPERATION_NAME);

        String where = buildWhere().add("o.op_name", name).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.operation o " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Operation> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
