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
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class RegistrationKindRepository extends CommonDBRepository<Kind> {
    private static final RowMapper<Kind> ROW_MAPPER = (resultSet, i) -> Kind.builder()
                                                                            .kindId(resultSet.getLong(Constants.RegistrationKind.ID))
                                                                            .kindName(resultSet.getString(Constants.RegistrationKind.NAME))
                                                                            .build();

    @Autowired
    public RegistrationKindRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Kind> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public synchronized Kind create(@NonNull @Nonnull Kind entity) {
        String jdbcTemplateInsert = "insert into carinfo.kind (kind_name) values (:name);";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getKindName()).build();
        return create(jdbcTemplateInsert, Constants.RegistrationKind.ID, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Kind update(@NonNull @Nonnull Kind entity) {
        String jdbcTemplateUpdate = "update carinfo.kind set kind_name = :name where kind_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getKindName())
                                                                 .addParam("id", entity.getKindId())
                                                                 .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder searchParams = getParamsHolderBuilder().param(Kind.KIND_NAME, entity.getKindName()).build();
        return findOne(searchParams);
    }

    @Override
    public synchronized boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.kind where kind_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public synchronized boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(kind_id) from carinfo.kind where kind_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Override
    public synchronized boolean exist(@NonNull @Nonnull Kind entity) {
        String jdbcTemplateSelectCount = "select count(kind_id) from carinfo.kind where kind_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getKindName()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Kind findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("id", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "kind", unless = "#result != null")
    @Nullable
    @Override
    public synchronized Kind findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Kind.KIND_NAME);
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public synchronized List<Kind> find() {
        String jdbcTemplateSelect = "select * from carinfo.kind;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public synchronized Page<Kind> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.kind k ";
        String name = searchParams.getString(Kind.KIND_NAME);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("k.kind_name", "name", name));
    }
}
