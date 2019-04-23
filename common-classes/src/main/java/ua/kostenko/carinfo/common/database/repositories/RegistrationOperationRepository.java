package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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

    @Override
    RowMapper<Operation> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public Operation create(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateInsert = "insert into carinfo.operation (op_code, op_name) values (?, ?);";
        return create(jdbcTemplateInsert, Constants.RegistrationOperation.CODE, entity.getOperationCode(), entity.getOperationName());
    }

    @Nullable
    @Override
    public Operation update(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateUpdate = "update carinfo.operation set op_name = ? where op_code = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getOperationName(), entity.getOperationCode());
        ParamsHolder searchParams = getParamsHolderBuilder().param(Operation.OPERATION_CODE, entity.getOperationCode()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.operation where op_code = ?;";
        return delete(jdbcTemplateDelete, id);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(op_code) from carinfo.operation where op_code = ?;";
        return exist(jdbcTemplateSelectCount, id);
    }

    @Override
    public boolean exist(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateSelectCount = "select count(op_code) from carinfo.operation where op_code = ?;";
        return exist(jdbcTemplateSelectCount, entity.getOperationCode());
    }

    @Nullable
    @Override
    public Operation findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.operation where op_code = ?;";
        return findOne(jdbcTemplateSelect, id);
    }

    @Cacheable(cacheNames = "operation", unless = "#result != null")
    @Nullable
    @Override
    public Operation findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        Long param = searchParams.getLong(Operation.OPERATION_CODE);
        String jdbcTemplateSelect = "select * from carinfo.operation where op_code = ?;";
        return findOne(jdbcTemplateSelect, param);
    }

    @Override
    public List<Operation> find() {
        String jdbcTemplateSelect = "select * from carinfo.operation;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<Operation> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.operation o ";
        Long code = searchParams.getLong(Operation.OPERATION_CODE);

        String where = buildWhere().add("o.op_code", code, true).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.operation o " + where;
        int total = jdbcTemplate.queryForObject(countQuery, FIND_TOTAL_MAPPER);

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Operation> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
