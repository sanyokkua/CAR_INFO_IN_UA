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
class RegistrationColorRepository extends CommonDBRepository<Color, String> {
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

    @Override
    WhereBuilder.BuildResult getWhereFromParams(ParamsHolder params) {
        return buildWhere()
                .addFieldParam(Constants.RegistrationColor.NAME, NAME_PARAM, params.getString(Color.COLOR_NAME))
                .build();
    }

    @Override
    String getTableName() {
        return Constants.RegistrationColor.TABLE;
    }

    @Nullable
    @Override
    public Color create(@NonNull @Nonnull Color entity) {
        String jdbcTemplateInsert = "insert into carinfo.color (color_name) values (:name);";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, entity.getColorName()).build();
        return create(jdbcTemplateInsert, Constants.RegistrationColor.ID, parameterSource);
    }

    @Nullable
    @Override
    public Color update(@NonNull @Nonnull Color entity) {
        String jdbcTemplateUpdate = "update carinfo.color set color_name = :name where color_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam(NAME_PARAM, entity.getColorName())
                .addParam(ID_PARAM, entity.getId())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder holder = getParamsHolderBuilder().param(Color.COLOR_NAME, entity.getColorName()).build();
        return findOne(holder);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.color where color_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "colorCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull Color entity) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, entity.getColorName()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public Color findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.color where color_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "color", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public Color findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Color.COLOR_NAME);
        String jdbcTemplateSelect = "select * from carinfo.color where color_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public List<Color> find() {
        String jdbcTemplateSelect = "select * from carinfo.color;";
        return find(jdbcTemplateSelect);
    }

    @Cacheable(cacheNames = "colorIndex", unless = "#result == false ", key = "#indexField")
    @Override
    public boolean existsByIndex(@Nonnull @NonNull String indexField) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(NAME_PARAM, indexField).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Override
    public Page<Color> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.color c ";
        String name = searchParams.getString(Color.COLOR_NAME);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("c.color_name", NAME_PARAM, name));
    }
}
