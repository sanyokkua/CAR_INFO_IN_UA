package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
    public RegistrationOperationRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Operation> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public synchronized Operation create(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateInsert = "insert into carinfo.operation (op_code, op_name) values (:code, :name);";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam("code", entity.getOperationCode())
                .addParam("name", entity.getOperationName())
                .build();
        return create(jdbcTemplateInsert, Constants.RegistrationOperation.CODE, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Operation update(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateUpdate = "update carinfo.operation set op_name = :name where op_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam("code", entity.getOperationCode())
                .addParam("name", entity.getOperationName())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder searchParams = getParamsHolderBuilder().param(Operation.OPERATION_CODE, entity.getOperationCode()).build();
        return findOne(searchParams);
    }

    @Override
    public synchronized boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.operation where op_code = :code;";
        SqlParameterSource params = getSqlParamBuilder().addParam("code", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public synchronized boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(op_code) from carinfo.operation where op_code = :code;";
        SqlParameterSource params = getSqlParamBuilder().addParam("code", id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "operationCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public synchronized boolean exist(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateSelectCount = "select count(op_code) from carinfo.operation where op_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("code", entity.getOperationCode()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Operation findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.operation where op_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("code", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "operation", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public synchronized Operation findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        Long param = searchParams.getLong(Operation.OPERATION_CODE);
        String jdbcTemplateSelect = "select * from carinfo.operation where op_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("code", param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public synchronized List<Operation> find() {
        String jdbcTemplateSelect = "select * from carinfo.operation;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public synchronized Page<Operation> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.operation o ";
        Long code = searchParams.getLong(Operation.OPERATION_CODE);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("o.op_code", "code", code));
    }
}
