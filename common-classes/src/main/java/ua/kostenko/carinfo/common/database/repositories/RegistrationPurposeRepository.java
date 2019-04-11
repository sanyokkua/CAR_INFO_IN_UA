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
import ua.kostenko.carinfo.common.api.Constants;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.CrudRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationPurposeRepository extends CommonDBRepository<Purpose> {
    private static final RowMapper<Purpose> ROW_MAPPER = (resultSet, i) -> Purpose.builder()
                                                                                  .purposeId(resultSet.getLong(Constants.RegistrationPurpose.ID))
                                                                                  .purposeName(resultSet.getString(Constants.RegistrationPurpose.NAME))
                                                                                  .build();

    @Autowired
    public RegistrationPurposeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public Purpose create(@NonNull @Nonnull Purpose entity) {
        String jdbcTemplateInsert = "insert into carinfo.purpose (purpose_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getPurposeName());
        ParamsHolder searchParams = getBuilder().param(Purpose.PURPOSE_NAME, entity.getPurposeName()).build();
        return findOne(searchParams);
    }

    @Nullable
    @Override
    public Purpose update(@NonNull @Nonnull Purpose entity) {
        String jdbcTemplateUpdate = "update carinfo.purpose set purpose_name = ? where purpose_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getPurposeName(), entity.getPurposeId());
        ParamsHolder searchParams = getBuilder().param(Purpose.PURPOSE_NAME, entity.getPurposeName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.purpose where purpose_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(purpose_id) from carinfo.purpose where purpose_id = ?;";
        Long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return Objects.nonNull(numberOfRows) && numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull Purpose entity) {
        ParamsHolder searchParams = getBuilder().param(Purpose.PURPOSE_NAME, entity.getPurposeName()).build();
        return nonNull(findOne(searchParams));
    }

    @Nullable
    @Override
    public Purpose findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Nullable
    @Override
    public Purpose findOne(@Nonnull ParamsHolder searchParams) {
        String purposeName = searchParams.getString(Purpose.PURPOSE_NAME);
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, purposeName));
    }

    @Override
    public List<Purpose> find() {
        String jdbcTemplateSelect = "select * from carinfo.purpose;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public Page<Purpose> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.purpose p ";
        String name = searchParams.getString(Purpose.PURPOSE_NAME);

        String where = buildWhere().add("p.purpose_name", name).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.purpose p " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Purpose> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
