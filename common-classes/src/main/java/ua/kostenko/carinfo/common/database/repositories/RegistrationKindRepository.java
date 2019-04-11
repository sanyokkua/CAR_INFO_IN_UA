package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.api.Constants;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.CrudRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationKindRepository extends CommonDBRepository<Kind> {
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
        Long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return Objects.nonNull(numberOfRows) && numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull Kind entity) {
        ParamsHolder searchParams = getBuilder().param(Kind.KIND_NAME, entity.getKindName()).build();
        return nonNull(findOne(searchParams));
    }

    @Nullable
    @Override
    public Kind findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Nullable
    @Override
    public Kind findOne(@Nonnull ParamsHolder searchParams) {
        String kindName = searchParams.getString(Kind.KIND_NAME);
        String jdbcTemplateSelect = "select * from carinfo.kind where kind_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, kindName));
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

        String where = buildWhere().add("k.kind_name", name).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.kind k " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Kind> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
