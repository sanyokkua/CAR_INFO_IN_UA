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
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
class RegistrationDepartmentRepository extends CommonDBRepository<Department> {
    private static final RowMapper<Department> ROW_MAPPER = (resultSet, i) -> Department.builder()
                                                                                        .departmentCode(resultSet.getLong(Constants.RegistrationDepartment.CODE))
                                                                                        .departmentAddress(resultSet.getString(Constants.RegistrationDepartment.ADDRESS))
                                                                                        .departmentEmail(resultSet.getString(Constants.RegistrationDepartment.EMAIL))
                                                                                        .build();

    @Autowired
    public RegistrationDepartmentRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public Department create(@NonNull @Nonnull Department entity) {
        String jdbcTemplateInsert = "insert into carinfo.department (dep_code, dep_addr, dep_email) values (?, ?, ?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getDepartmentCode(), entity.getDepartmentAddress(), entity.getDepartmentEmail());
        ParamsHolder holder = getBuilder().param(Department.DEPARTMENT_CODE, entity.getDepartmentCode()).build();
        return findOne(holder);
    }

    @Nullable
    @Override
    public Department update(@NonNull @Nonnull Department entity) {
        String jdbcTemplateUpdate = "update carinfo.department set dep_addr = ?, dep_email = ? where dep_code = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getDepartmentAddress(), entity.getDepartmentEmail(), entity.getDepartmentCode());
        ParamsHolder holder = getBuilder().param(Department.DEPARTMENT_CODE, entity.getDepartmentCode()).build();
        return findOne(holder);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.department where dep_code = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(dep_code) from carinfo.department where dep_code = ?;";
        long numberOfRows = jdbcTemplate.query(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id).stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull Department entity) {
        String jdbcTemplateSelectCount = "select count(dep_code) from carinfo.department where dep_code = ?;";
        long numberOfRows = jdbcTemplate.query(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), entity.getDepartmentCode())
                                        .stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    @Nullable
    @Override
    public Department findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.department where dep_code = ?;";
        return Utils.getResultOrWrapExceptionToNull(() -> jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER, id).stream().findFirst().orElse(null));
    }

    @Cacheable(cacheNames = "department", unless = "#result != null")
    @Nullable
    @Override
    public Department findOne(@Nonnull ParamsHolder searchParams) {
        Long depCode = searchParams.getLong(Department.DEPARTMENT_CODE);
        if (Objects.isNull(depCode)) {
            return null;
        }
        return findOne(depCode);
    }

    @Override
    public List<Department> find() {
        String jdbcTemplateSelect = "select * from carinfo.department;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public Page<Department> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.department d ";

        Long code = searchParams.getLong(Department.DEPARTMENT_CODE);
        String email = searchParams.getString(Department.DEPARTMENT_EMAIL);
        String address = searchParams.getString(Department.DEPARTMENT_ADDRESS);

        String where = buildWhere()
                .add("d.dep_code", code, true)
                .add("d.dep_email", email, true)
                .add("d.dep_addr", address, true)
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
