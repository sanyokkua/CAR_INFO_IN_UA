package ua.kostenko.carinfo.common.database.repositories.jdbc.crud;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.ParamsHolder;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;
import ua.kostenko.carinfo.common.database.repositories.FieldSearchable;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Purpose;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationPurposeRepository extends CachingJdbcRepository<Purpose> implements PageableRepository<Purpose>, FieldSearchable<Purpose> {
    private static final RowMapper<Purpose> ROW_MAPPER = (resultSet, i) -> Purpose.builder()
                                                                                  .purposeId(resultSet.getLong(Constants.RegistrationPurpose.ID))
                                                                                  .purposeName(resultSet.getString(Constants.RegistrationPurpose.NAME))
                                                                                  .build();

    @Autowired
    public RegistrationPurposeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, Purpose> getCacheLoader() {
        return new CacheLoader<String, Purpose>() {
            @Override
            public Purpose load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Override
    public Purpose findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_name = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect,
                                                  ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public Purpose find(@Nonnull Purpose entity) {
        return getFromCache(entity.getPurposeName());
    }

    @Nullable
    @Override
    public Purpose create(@NonNull @Nonnull Purpose entity) {
        String jdbcTemplateInsert = "insert into carinfo.purpose (purpose_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getPurposeName());
        return getFromCache(entity.getPurposeName());
    }

    @Nullable
    @Override
    public Purpose update(@NonNull @Nonnull Purpose entity) {
        getCache().invalidate(entity.getPurposeName());
        String jdbcTemplateUpdate = "update carinfo.purpose set purpose_name = ? where purpose_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getPurposeName(), entity.getPurposeId());
        return findByField(entity.getPurposeName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        Purpose purpose = find(id);
        if (nonNull(purpose)) {
            getCache().invalidate(purpose.getPurposeName());
        }
        String jdbcTemplateDelete = "delete from carinfo.purpose where purpose_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public Purpose find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.purpose where purpose_id = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(purpose_id) from carinfo.purpose where purpose_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public List<Purpose> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.purpose;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Purpose entity) {
        return nonNull(getFromCache(entity.getPurposeName()));
    }

    @Override
    public void createAll(Iterable<Purpose> entities) {
        final int batchSize = 100;
        List<List<Purpose>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<Purpose> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.purpose (purpose_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    Purpose object = batch.get(i);
                    ps.setString(1, object.getPurposeName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public Page<Purpose> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.purpose p ";
        String name = searchParams.getString(Constants.RegistrationPurpose.NAME);

        String where = buildWhere().add("p.purpose_name", name).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.purpose p " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Purpose> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
