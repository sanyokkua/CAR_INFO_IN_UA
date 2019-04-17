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
import ua.kostenko.carinfo.common.api.Constants;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Department;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;


@Slf4j
public class RegistrationDepartmentRepository extends CachingJdbcRepository<Department> implements PageableRepository<Department>, FieldSearchable<Department> {
    private static final RowMapper<Department> ROW_MAPPER = (resultSet, i) -> Department.builder()
                                                                                        .departmentCode(resultSet.getLong(Constants.RegistrationDepartment.CODE))
                                                                                        .departmentAddress(resultSet.getString(Constants.RegistrationDepartment.ADDRESS))
                                                                                        .departmentEmail(resultSet.getString(Constants.RegistrationDepartment.EMAIL))
                                                                                        .build();

    @Autowired
    public RegistrationDepartmentRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, Department> getCacheLoader() {
        return new CacheLoader<>() {
          @Override
          public Department load(@NonNull @Nonnull String key) {
            return findByField(key);
          }
        };
    }

    @Override
    public Department findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.department where dep_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public Department find(@Nonnull Department entity) {
        return getFromCache(entity.getDepartmentCode()+"");
    }

    @Nullable
    @Override
    public Department create(@NonNull @Nonnull Department entity) {
        String jdbcTemplateInsert = "insert into carinfo.department (dep_code, dep_name, dep_addr, dep_email) values (?, ?, ?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getDepartmentCode(), entity.getDepartmentAddress(), entity.getDepartmentEmail());
        return getFromCache(entity.getDepartmentCode()+"");
    }

    @Nullable
    @Override
    public Department update(@NonNull @Nonnull Department entity) {
        getCache().invalidate(entity.getDepartmentCode()+ "");
        String jdbcTemplateUpdate = "update carinfo.department set dep_name = ?, dep_addr = ?, dep_email = ? where dep_code = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getDepartmentCode() + "", entity.getDepartmentAddress(), entity.getDepartmentEmail(), entity.getDepartmentCode());
        return findByField(entity.getDepartmentCode()+"");
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        Department department = find(id);
        if (nonNull(department)) {
            getCache().invalidate(department.getDepartmentCode().toString());
        }
        String jdbcTemplateDelete = "delete from carinfo.department where dep_code = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public Department find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.department where dep_code = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(dep_code) from carinfo.department where dep_code = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public List<Department> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.department;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Department entity) {
        return nonNull(getFromCache(entity.getDepartmentCode().toString()));
    }

    @Override
    public void createAll(Iterable<Department> entities) {
        final int batchSize = 100;
        List<List<Department>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<Department> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.department (dep_code, dep_name, dep_addr, dep_email) values (?, ?, ?, ?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i) throws SQLException {
                    Department object = batch.get(i);
                    ps.setLong(1, object.getDepartmentCode());

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
    public Page<Department> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.department d ";

        Long code = searchParams.getLong(Constants.RegistrationDepartment.CODE);
        String name = searchParams.getString(Constants.RegistrationDepartment.NAME);
        String email = searchParams.getString(Constants.RegistrationDepartment.EMAIL);
        String address = searchParams.getString(Constants.RegistrationDepartment.ADDRESS);

        String where = buildWhere()
                .add("d.dep_code", code)
                .add("d.dep_name", name)
                .add("d.dep_email", email)
                .add("d.dep_addr", address)
                .build();

        String countQuery = "select count(1) as row_count " + " from carinfo.department d " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        String querySql = select + where + " limit ? offset ?";
        List<Department> result = jdbcTemplate.query(querySql, ROW_MAPPER, limit, offset);
        return new PageImpl<>(result, pageable, total);
    }
}
