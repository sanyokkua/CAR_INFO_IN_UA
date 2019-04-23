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
    public RegistrationColorRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Color> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public Color create(@NonNull @Nonnull Color entity) {
        String jdbcTemplateInsert = "insert into carinfo.color (color_name) values (?);";
        return create(jdbcTemplateInsert, Constants.RegistrationColor.ID, entity.getColorName());
    }

    @Nullable
    @Override
    public Color update(@NonNull @Nonnull Color entity) {
        String jdbcTemplateUpdate = "update carinfo.color set color_name = ? where color_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getColorName(), entity.getColorId());
        ParamsHolder holder = getParamsHolderBuilder().param(Color.COLOR_NAME, entity.getColorName()).build();
        return findOne(holder);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.color where color_id = ?;";
        return delete(jdbcTemplateDelete, id);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_id = ?;";
        return exist(jdbcTemplateSelectCount, id);
    }

    @Override
    public boolean exist(@NonNull @Nonnull Color entity) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_name = ?;";
        return exist(jdbcTemplateSelectCount, entity.getColorName());
    }

    @Nullable
    @Override
    public Color findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.color where color_id = ?;";
        return findOne(jdbcTemplateSelect, id);
    }

    @Cacheable(cacheNames = "color", unless = "#result != null")
    @Nullable
    @Override
    public Color findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Color.COLOR_NAME);
        String jdbcTemplateSelect = "select * from carinfo.color where color_name = ?;";
        return findOne(jdbcTemplateSelect, param);
    }

    @Override
    public List<Color> find() {
        String jdbcTemplateSelect = "select * from carinfo.color;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<Color> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.color c ";
        String name = searchParams.getString(Color.COLOR_NAME);

        String where = buildWhere().add("c.color_name", name, true).build();

        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, FIND_TOTAL_MAPPER);

        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Color> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
