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
import ua.kostenko.carinfo.common.api.records.Color;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class RegistrationColorRepository extends CommonDBRepository<Color> {
    private static final RowMapper<Color> ROW_MAPPER = (resultSet, i) -> Color.builder()
                                                                              .colorId(resultSet.getLong(Constants.RegistrationColor.ID))
                                                                              .colorName(resultSet.getString(Constants.RegistrationColor.NAME))
                                                                              .build();

    @Autowired
    public RegistrationColorRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Color> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public synchronized Color create(@NonNull @Nonnull Color entity) {
        String jdbcTemplateInsert = "insert into carinfo.color (color_name) values (:name);";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getColorName()).build();
        return create(jdbcTemplateInsert, Constants.RegistrationColor.ID, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Color update(@NonNull @Nonnull Color entity) {
        String jdbcTemplateUpdate = "update carinfo.color set color_name = :name where color_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam("name", entity.getColorName())
                .addParam("id", entity.getColorId())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder holder = getParamsHolderBuilder().param(Color.COLOR_NAME, entity.getColorName()).build();
        return findOne(holder);
    }

    @Override
    public synchronized boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.color where color_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public synchronized boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Override
    public synchronized boolean exist(@NonNull @Nonnull Color entity) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getColorName()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Color findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.color where color_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("id", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "color", unless = "#result != null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public synchronized Color findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Color.COLOR_NAME);
        String jdbcTemplateSelect = "select * from carinfo.color where color_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public synchronized List<Color> find() {
        String jdbcTemplateSelect = "select * from carinfo.color;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public synchronized Page<Color> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.color c ";
        String name = searchParams.getString(Color.COLOR_NAME);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("c.color_name", "name", name));
    }
}
