package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.raw.RegistrationDepartment;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;
import ua.kostenko.carinfo.common.database.repositories.FieldSearchable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationDepartmentRepository extends CachingJdbcRepository<RegistrationDepartment> implements FieldSearchable<RegistrationDepartment> {
    private static final RowMapper<RegistrationDepartment> ROW_MAPPER = (resultSet, i) -> RegistrationDepartment.builder()
                                                                                                                .departmentCode(resultSet.getLong(Constants.RegistrationDepartment.CODE))
                                                                                                                .departmentName(resultSet.getString(Constants.RegistrationDepartment.NAME))
                                                                                                                .departmentAddress(resultSet.getString(Constants.RegistrationDepartment.ADDRESS))
                                                                                                                .departmentEmail(resultSet.getString(Constants.RegistrationDepartment.EMAIL))
                                                                                                                .build();
    @Autowired
    public RegistrationDepartmentRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationDepartment> getCacheLoader() {
        return new CacheLoader<String, RegistrationDepartment>() {
            @Override
            public RegistrationDepartment load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationDepartment create(@NonNull @Nonnull RegistrationDepartment entity) {
        String jdbcTemplateInsert = "insert into carinfo.department (dep_code, dep_name, dep_addr, dep_email) values (?, ?, ?, ?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getDepartmentCode(), entity.getDepartmentName(), entity.getDepartmentAddress(), entity.getDepartmentEmail());
        return getFromCache(entity.getDepartmentName());
    }

    @Nullable
    @Override
    public RegistrationDepartment update(@NonNull @Nonnull RegistrationDepartment entity) {
        getCache().invalidate(entity.getDepartmentName());
        String jdbcTemplateUpdate = "update carinfo.department set dep_name = ?, dep_addr = ?, dep_email = ? where dep_code = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getDepartmentName(), entity.getDepartmentAddress(), entity.getDepartmentEmail(), entity.getDepartmentCode());
        return findByField(entity.getDepartmentName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        RegistrationDepartment department = find(id);
        if (nonNull(department)) {
            getCache().invalidate(department.getDepartmentName());
        }
        String jdbcTemplateDelete = "delete from carinfo.department where dep_code = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public RegistrationDepartment find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.department where dep_code = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationDepartment> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.department;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(dep_code) from carinfo.department where dep_code = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationDepartment entity) {
        return nonNull(getFromCache(entity.getDepartmentName()));
    }

    @Override
    public void createAll(Iterable<RegistrationDepartment> entities) {
        final int batchSize = 100;
        List<List<RegistrationDepartment>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationDepartment> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.department (dep_code, dep_name, dep_addr, dep_email) values (?, ?, ?, ?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i) throws SQLException {
                    RegistrationDepartment object = batch.get(i);
                    ps.setLong(1, object.getDepartmentCode());
                    ps.setString(2, object.getDepartmentName());
                    ps.setString(3, object.getDepartmentAddress());
                    ps.setString(4, object.getDepartmentEmail());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public RegistrationDepartment findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.department where dep_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }
}
