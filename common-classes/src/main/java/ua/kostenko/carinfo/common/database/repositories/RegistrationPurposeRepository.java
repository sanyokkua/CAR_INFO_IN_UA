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
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class RegistrationPurposeRepository extends CommonDBRepository<Purpose> {
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

    @Nullable
    @Override
    public synchronized Purpose create(@NonNull @Nonnull Purpose entity) {
        String jdbcTemplateInsert = "insert into carinfo.purpose (purpose_name) values (:name);";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getPurposeName()).build();
        return create(jdbcTemplateInsert, Constants.RegistrationPurpose.ID, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Purpose update(@NonNull @Nonnull Purpose entity) {
        String jdbcTemplateUpdate = "update carinfo.purpose set purpose_name = :name where purpose_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getPurposeName())
                                                                 .addParam("id", entity.getPurposeId())
                                                                 .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder searchParams = getParamsHolderBuilder().param(Purpose.PURPOSE_NAME, entity.getPurposeName()).build();
        return findOne(searchParams);
    }

    @Override
    public synchronized boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.purpose where purpose_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public synchronized boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(purpose_id) from carinfo.purpose where purpose_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Override
    public synchronized boolean exist(@NonNull @Nonnull Purpose entity) {
        String jdbcTemplateSelectCount = "select count(purpose_id) from carinfo.purpose where purpose_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getPurposeName()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Purpose findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("id", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "purpose", unless = "#result != null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public synchronized Purpose findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Purpose.PURPOSE_NAME);
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public synchronized List<Purpose> find() {
        String jdbcTemplateSelect = "select * from carinfo.purpose;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public synchronized Page<Purpose> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.purpose p ";
        String name = searchParams.getString(Purpose.PURPOSE_NAME);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("p.purpose_name", "name", name));
    }
}
