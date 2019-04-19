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
import ua.kostenko.carinfo.common.Utils;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
class RegistrationBodyTypeRepository extends CommonDBRepository<BodyType> {
    private static final RowMapper<BodyType> ROW_MAPPER = (resultSet, i) -> BodyType.builder()
                                                                                    .bodyTypeId(resultSet.getLong(Constants.RegistrationBodyType.ID))
                                                                                    .bodyTypeName(resultSet.getString(Constants.RegistrationBodyType.NAME))
                                                                                    .build();

    @Autowired
    public RegistrationBodyTypeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public BodyType create(@NonNull @Nonnull BodyType entity) {
        String jdbcTemplateInsert = "insert into carinfo.body_type (body_type_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getBodyTypeName());
        ParamsHolder holder = getBuilder().param(BodyType.BODY_TYPE_NAME, entity.getBodyTypeName()).build();
        return findOne(holder);
    }

    @Nullable
    @Override
    public BodyType update(@NonNull @Nonnull BodyType entity) {
        String jdbcTemplateUpdate = "update carinfo.body_type set body_type_name = ? where body_type_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getBodyTypeName(), entity.getBodyTypeId());
        ParamsHolder holder = getBuilder().param(BodyType.BODY_TYPE_NAME, entity.getBodyTypeName()).build();
        return findOne(holder);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.body_type where body_type_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(body_type_id) from carinfo.body_type where body_type_id = ?;";
        long numberOfRows = jdbcTemplate.query(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id).stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull BodyType entity) {
        String jdbcTemplateSelectCount = "select count(body_type_id) from carinfo.body_type where body_type_name = ?;";
        long numberOfRows = jdbcTemplate.query(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), entity.getBodyTypeName())
                .stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    @Nullable
    @Override
    public BodyType findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_id = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER, id).stream().findFirst().orElse(null));
    }

    @Cacheable(cacheNames = "bodyType", unless = "#result != null")
    @Nullable
    @Override
    public BodyType findOne(@Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(BodyType.BODY_TYPE_NAME);
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_name = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER, param)
                                                                      .stream()
                                                                      .findFirst()
                                                                      .orElse(null));
    }

    @Override
    public List<BodyType> find() {
        String jdbcTemplateSelect = "select * from carinfo.body_type;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public Page<BodyType> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.body_type bt ";
        String name = searchParams.getString(BodyType.BODY_TYPE_NAME);

        String where = buildWhere().add("bt.body_type_name", name, true).build();

        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<BodyType> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
