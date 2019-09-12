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
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.common.database.Constants;

@Repository
class RegistrationPurposeRepository extends CommonDBRepository<Purpose, String> {

    private static final RowMapper<Purpose> ROW_MAPPER = (resultSet, i) -> Purpose.builder()
            .purposeId(resultSet.getLong(Constants.RegistrationPurpose.ID))
            .purposeName(resultSet.getString(Constants.RegistrationPurpose.NAME))
            .build();

    @Autowired
    public RegistrationPurposeRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Purpose> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    WhereBuilder.BuildResult getWhereFromParams(ParamsHolder params) {
        return buildWhere()
                .addFieldParam(Constants.RegistrationPurpose.NAME, NAME_PARAM, params.getString(Purpose.PURPOSE_NAME))
                .build();
    }

    @Override
    String getTableName() {
        return Constants.RegistrationPurpose.TABLE;
    }

    @Nullable
    @Override
    public Purpose create(@NonNull @Nonnull Purpose entity) {
        String jdbcTemplateInsert = "insert into carinfo.purpose (purpose_name) values (:name);";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, entity.getPurposeName()).build();
        return create(jdbcTemplateInsert, Constants.RegistrationPurpose.ID, parameterSource);
    }

    @Nullable
    @Override
    public Purpose update(@NonNull @Nonnull Purpose entity) {
        String jdbcTemplateUpdate = "update carinfo.purpose set purpose_name = :name where purpose_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, entity.getPurposeName())
                .addParam(ID_PARAM, entity.getPurposeId())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder searchParams =
                getParamsHolderBuilder().param(Purpose.PURPOSE_NAME, entity.getPurposeName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.purpose where purpose_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(purpose_id) from carinfo.purpose where purpose_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "purposeCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull Purpose entity) {
        String jdbcTemplateSelectCount = "select count(purpose_id) from carinfo.purpose where purpose_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, entity.getPurposeName()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public Purpose findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "purpose", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public Purpose findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Purpose.PURPOSE_NAME);
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public List<Purpose> find() {
        String jdbcTemplateSelect = "select * from carinfo.purpose;";
        return find(jdbcTemplateSelect);
    }

    @Cacheable(cacheNames = "purposeIndex", unless = "#result == false ", key = "#indexField")
    @Override
    public boolean existsByIndex(@Nonnull @NonNull String indexField) {
        String jdbcTemplateSelectCount = "select count(purpose_id) from carinfo.purpose where purpose_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, indexField).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Override
    public Page<Purpose> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.purpose p ";
        String name = searchParams.getString(Purpose.PURPOSE_NAME);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("p.purpose_name", NAME_PARAM, name));
    }
}
