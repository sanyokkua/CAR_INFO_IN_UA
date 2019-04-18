package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.Utils;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Color;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

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

    @Nullable
    @Override
    public Color create(@NonNull @Nonnull Color entity) {
        String jdbcTemplateInsert = "insert into carinfo.color (color_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getColorName());
        ParamsHolder holder = getBuilder().param(Color.COLOR_NAME, entity.getColorName()).build();
        return findOne(holder);
    }

    @Nullable
    @Override
    public Color update(@NonNull @Nonnull Color entity) {
        String jdbcTemplateUpdate = "update carinfo.color set color_name = ? where color_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getColorName(), entity.getColorId());
        ParamsHolder holder = getBuilder().param(Color.COLOR_NAME, entity.getColorName()).build();
        return findOne(holder);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.color where color_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(color_id) from carinfo.color where color_id = ?;";
        Long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return Objects.nonNull(numberOfRows) && numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull Color entity) {
        ParamsHolder searchParams = getBuilder().param(Color.COLOR_NAME, entity.getColorName()).build();
        return nonNull(findOne(searchParams));
    }

    @Nullable
    @Override
    public Color findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.color where color_id = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Nullable
    @Override
    public Color findOne(@Nonnull ParamsHolder searchParams) {
        String colorName = searchParams.getString(Color.COLOR_NAME);
        String jdbcTemplateSelect = "select * from carinfo.color where color_name = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, colorName));
    }

    @Override
    public List<Color> find() {
        String jdbcTemplateSelect = "select * from carinfo.color;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public Page<Color> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.color c ";
        String name = searchParams.getString(Color.COLOR_NAME);

        String where = buildWhere().add("c.color_name", name).build();

        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Color> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
