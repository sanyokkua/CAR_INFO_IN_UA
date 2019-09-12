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
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.database.Constants;

@Repository
class RegistrationModelRepository extends CommonDBRepository<Model, String> {

    private static final RowMapper<Model> ROW_MAPPER = (resultSet, i) -> Model.builder()
            .modelId(resultSet.getLong(Constants.RegistrationModel.ID))
            .modelName(resultSet.getString(Constants.RegistrationModel.NAME))
            .build();

    @Autowired
    public RegistrationModelRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Model> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    WhereBuilder.BuildResult getWhereFromParams(ParamsHolder params) {
        return buildWhere()
                .addFieldParam(Constants.RegistrationModel.NAME, NAME_PARAM, params.getString(Model.MODEL_NAME))
                .build();
    }

    @Override
    String getTableName() {
        return Constants.RegistrationModel.TABLE;
    }

    @Nullable
    @Override
    public Model create(@NonNull @Nonnull Model entity) {
        String jdbcTemplateInsert = "insert into carinfo.model (model_name) values (:name);";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, entity.getModelName()).build();
        return create(jdbcTemplateInsert, Constants.RegistrationModel.ID, parameterSource);
    }

    @Nullable
    @Override
    public Model update(@NonNull @Nonnull Model entity) {
        String jdbcTemplateUpdate = "update carinfo.model set model_name = :name where model_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, entity.getModelName())
                .addParam(ID_PARAM, entity.getModelId())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder searchParams = getParamsHolderBuilder().param(Model.MODEL_NAME, entity.getModelName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.model where model_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(model_id) from carinfo.model where model_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "modelCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull Model entity) {
        String jdbcTemplateSelectCount = "select count(model_id) from carinfo.model where model_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, entity.getModelName()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public Model findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.model where model_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "model", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public Model findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Model.MODEL_NAME);
        String jdbcTemplateSelect = "select * from carinfo.model where model_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public List<Model> find() {
        String jdbcTemplateSelect = "select * from carinfo.model;";
        return find(jdbcTemplateSelect);
    }

    @Cacheable(cacheNames = "modelIndex", unless = "#result == false ", key = "#indexField")
    @Override
    public boolean existsByIndex(@Nonnull @NonNull String indexField) {
        String jdbcTemplateSelectCount = "select count(model_id) from carinfo.model where model_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, indexField).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Override
    public Page<Model> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.model m ";
        String name = searchParams.getString(Model.MODEL_NAME);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("m.model_name", NAME_PARAM, name));
    }
}
