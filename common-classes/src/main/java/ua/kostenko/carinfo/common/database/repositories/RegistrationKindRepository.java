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
    public RegistrationKindRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public Kind create(@NonNull @Nonnull Kind entity) {
        String jdbcTemplateInsert = "insert into carinfo.kind (kind_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getKindName());
        ParamsHolder searchParams = getBuilder().param(Kind.KIND_NAME, entity.getKindName()).build();
        return findOne(searchParams);
    }

    @Nullable
    @Override
    public Kind update(@NonNull @Nonnull Kind entity) {
        String jdbcTemplateUpdate = "update carinfo.kind set kind_name = ? where kind_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getKindName(), entity.getKindId());
        ParamsHolder searchParams = getBuilder().param(Kind.KIND_NAME, entity.getKindName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.kind where kind_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(kind_id) from carinfo.kind where kind_id = ?;";
        long numberOfRows = jdbcTemplate.query(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id).stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull Kind entity) {
        String jdbcTemplateSelectCount = "select count(kind_id) from carinfo.kind where kind_name = ?;";
        long numberOfRows = jdbcTemplate.query(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), entity.getKindName())
                                        .stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    @Nullable
    @Override
    public Kind findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_id = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER, id).stream().findFirst().orElse(null));
    }

    @Cacheable(cacheNames = "kind", unless = "#result != null")
    @Nullable
    @Override
    public Kind findOne(@Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Kind.KIND_NAME);
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_name = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER, param)
                                                                      .stream()
                                                                      .findFirst()
                                                                      .orElse(null));
    }

    @Override
    public List<Kind> find() {
        String jdbcTemplateSelect = "select * from carinfo.kind;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public Page<Kind> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.kind k ";
        String name = searchParams.getString(Kind.KIND_NAME);

        String where = buildWhere().add("k.kind_name", name, true).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.kind k " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Kind> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
