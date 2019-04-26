package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
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
    public RegistrationDepartmentRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Department> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public synchronized Department create(@NonNull @Nonnull Department entity) {
        String jdbcTemplateInsert = "insert into carinfo.department (dep_code, dep_addr, dep_email) values (:code, :addr, :email);";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam("code", entity.getDepartmentCode())
                .addParam("addr", entity.getDepartmentAddress())
                .addParam("email", entity.getDepartmentEmail())
                .build();
        return create(jdbcTemplateInsert, Constants.RegistrationDepartment.CODE, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Department update(@NonNull @Nonnull Department entity) {
        String jdbcTemplateUpdate = "update carinfo.department set dep_addr = :addr, dep_email = :email where dep_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam("code", entity.getDepartmentCode())
                .addParam("addr", entity.getDepartmentAddress())
                .addParam("email", entity.getDepartmentEmail())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder holder = getParamsHolderBuilder().param(Department.DEPARTMENT_CODE, entity.getDepartmentCode()).build();
        return findOne(holder);
    }

    @Override
    public synchronized boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.department where dep_code = :code;";
        SqlParameterSource params = getSqlParamBuilder().addParam("code", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public synchronized boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(dep_code) from carinfo.department where dep_code = :code;";
        SqlParameterSource params = getSqlParamBuilder().addParam("code", id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Override
    public synchronized boolean exist(@NonNull @Nonnull Department entity) {
        String jdbcTemplateSelectCount = "select count(dep_code) from carinfo.department where dep_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("code", entity.getDepartmentCode()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public synchronized Department findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.department where dep_code = :code;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("code", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "department", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public synchronized Department findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        Long depCode = searchParams.getLong(Department.DEPARTMENT_CODE);
        if (Objects.isNull(depCode)) {
            return null;
        }
        return findOne(depCode);
    }

    @Override
    public synchronized List<Department> find() {
        String jdbcTemplateSelect = "select * from carinfo.department;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public synchronized Page<Department> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.department d ";
        Long code = searchParams.getLong(Department.DEPARTMENT_CODE);
        String email = searchParams.getString(Department.DEPARTMENT_EMAIL);
        String address = searchParams.getString(Department.DEPARTMENT_ADDRESS);
        return findPage(searchParams, select, from, buildWhere()
                .addFieldParam("d.dep_code", "code", code)
                .addFieldParam("d.dep_email", "addr", address)
                .addFieldParam("d.dep_addr", "email", email));
    }
}
