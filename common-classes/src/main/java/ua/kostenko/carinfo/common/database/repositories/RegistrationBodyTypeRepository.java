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
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class RegistrationBodyTypeRepository extends CommonDBRepository<BodyType, String> {
    private static final RowMapper<BodyType> ROW_MAPPER = (resultSet, i) -> BodyType.builder()
                                                                                    .bodyTypeId(resultSet.getLong(Constants.RegistrationBodyType.ID))
                                                                                    .bodyTypeName(resultSet.getString(Constants.RegistrationBodyType.NAME))
                                                                                    .build();

    @Autowired
    public RegistrationBodyTypeRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<BodyType> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public BodyType create(@NonNull @Nonnull BodyType entity) {
        String jdbcTemplateInsert = "insert into carinfo.body_type (body_type_name) values (:name);";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getBodyTypeName()).build();
        return create(jdbcTemplateInsert, Constants.RegistrationBodyType.ID, parameterSource);
    }

    @Nullable
    @Override
    public BodyType update(@NonNull @Nonnull BodyType entity) {
        String jdbcTemplateUpdate = "update carinfo.body_type set body_type_name = :name where body_type_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam("id", entity.getId())
                .addParam("name", entity.getBodyTypeName())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder holder = getParamsHolderBuilder().param(BodyType.BODY_TYPE_NAME, entity.getBodyTypeName()).build();
        return findOne(holder);
    }

    @Cacheable(cacheNames = "bodyIndex", unless = "#result == false ", key = "#indexField")
    @Override
    public boolean existsByIndex(@Nonnull @NonNull String indexField) {
        String jdbcTemplateSelectCount = "select count(body_type_id) from carinfo.body_type where body_type_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", indexField).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.body_type where body_type_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(body_type_id) from carinfo.body_type where body_type_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("id", id).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Cacheable(cacheNames = "bodyTypeCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull BodyType entity) {
        String jdbcTemplateSelectCount = "select count(body_type_id) from carinfo.body_type where body_type_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getBodyTypeName()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public BodyType findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("id", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "bodyType", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public BodyType findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(BodyType.BODY_TYPE_NAME);
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public List<BodyType> find() {
        String jdbcTemplateSelect = "select * from carinfo.body_type;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<BodyType> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.body_type bt ";
        String name = searchParams.getString(BodyType.BODY_TYPE_NAME);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("bt.body_type_name", "name", name));
    }
}
