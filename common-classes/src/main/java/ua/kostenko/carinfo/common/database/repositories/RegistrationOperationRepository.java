package ua.kostenko.carinfo.common.database.repositories;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.common.database.Constants;

@Repository
class RegistrationOperationRepository extends CommonDBRepository<Operation, Long> {

    protected static final String CODE_PARAM = "code";
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

    @Override
    WhereBuilder.BuildResult getWhereFromParams(ParamsHolder params) {
        return buildWhere()
                .addFieldParam(Constants.RegistrationOperation.CODE, ID_PARAM, params.getLong(Operation.OPERATION_CODE))
                .addFieldParam(Constants.RegistrationOperation.NAME, NAME_PARAM,
                        params.getString(Operation.OPERATION_NAME))
                .build();
    }

    @Override
    String getTableName() {
        return Constants.RegistrationOperation.TABLE;
    }

    @Nullable
    @Override
    public Operation create(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateInsert = "insert into carinfo.operation (op_code, op_name) values (:code, :name);";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam(CODE_PARAM, entity.getOperationCode())
                .addParam(NAME_PARAM, entity.getOperationName())
                .build();
        return create(jdbcTemplateInsert, Constants.RegistrationOperation.CODE, parameterSource);
    }

    @Nullable
    @Override
    public Operation update(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateUpdate = "update carinfo.operation set op_name = :name where op_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam(CODE_PARAM, entity.getOperationCode())
                .addParam(NAME_PARAM, entity.getOperationName())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder searchParams =
                getParamsHolderBuilder().param(Operation.OPERATION_CODE, entity.getOperationCode()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.operation where op_code = :code;";
        SqlParameterSource params = getSqlParamBuilder().addParam(CODE_PARAM, id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(op_code) from carinfo.operation where op_code = :code;";
        SqlParameterSource params = getSqlParamBuilder().addParam(CODE_PARAM, id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "operationCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull Operation entity) {
        String jdbcTemplateSelectCount = "select count(op_code) from carinfo.operation where op_code = :code;";
        SqlParameterSource parameterSource =
                getSqlParamBuilder().addParam(CODE_PARAM, entity.getOperationCode()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public Operation findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.operation where op_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(CODE_PARAM, id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "operation", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public Operation findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        Long param = searchParams.getLong(Operation.OPERATION_CODE);
        String jdbcTemplateSelect = "select * from carinfo.operation where op_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(CODE_PARAM, param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public List<Operation> find() {
        String jdbcTemplateSelect = "select * from carinfo.operation;";
        return find(jdbcTemplateSelect);
    }

    @Cacheable(cacheNames = "operationIndex", unless = "#result == false ", key = "#indexField")
    @Override
    public boolean existsByIndex(@Nonnull @NonNull Long indexField) {
        String jdbcTemplateSelectCount = "select count(op_code) from carinfo.operation where op_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(CODE_PARAM, indexField).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Override
    public Page<Operation> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.operation o ";
        Long code = searchParams.getLong(Operation.OPERATION_CODE);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("o.op_code", CODE_PARAM, code));
    }
}
