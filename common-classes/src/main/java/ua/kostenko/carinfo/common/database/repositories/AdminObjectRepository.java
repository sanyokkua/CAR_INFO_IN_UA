package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.api.Constants;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.CrudRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class AdminObjectRepository extends CommonDBRepository<AdministrativeObject> {
    private static final RowMapper<AdministrativeObject> ROW_MAPPER = (resultSet, i) -> AdministrativeObject.builder()
                                                                                                            .adminObjId(resultSet.getLong(Constants.AdminObject.ID))
                                                                                                            .adminObjType(resultSet.getString(Constants.AdminObject.TYPE))
                                                                                                            .adminObjName(resultSet.getString(Constants.AdminObject.NAME))
                                                                                                            .build();
    @Autowired
    public AdminObjectRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public AdministrativeObject create(@NonNull @Nonnull AdministrativeObject entity) {
        String jdbcTemplateInsert = "insert into carinfo.admin_object (admin_obj_id, admin_obj_type, admin_obj_name) values (?, ?, ?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getAdminObjId(), entity.getAdminObjType(), entity.getAdminObjName());
        ParamsHolder searchParams = getBuilder().param(AdministrativeObject.ADMIN_OBJ_NAME, entity.getAdminObjName()).build();
        return findOne(searchParams);
    }

    @Nullable
    @Override
    public AdministrativeObject update(@NonNull @Nonnull AdministrativeObject entity) {
        String jdbcTemplateUpdate = "update carinfo.admin_object set admin_obj_type = ?, admin_obj_name = ? where admin_obj_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getAdminObjType(), entity.getAdminObjName(), entity.getAdminObjId());
        ParamsHolder searchParams = getBuilder().param(AdministrativeObject.ADMIN_OBJ_NAME, entity.getAdminObjName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.admin_object where admin_obj_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(admin_obj_id) from carinfo.admin_object where admin_obj_id = ?;";
        Long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return Objects.nonNull(numberOfRows) && numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull AdministrativeObject entity) {
        ParamsHolder searchParams = getBuilder().param(AdministrativeObject.ADMIN_OBJ_NAME, entity.getAdminObjName()).build();
        return nonNull(findOne(searchParams));
    }

    @Nullable
    @Override
    public AdministrativeObject findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.admin_object where admin_obj_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Nullable
    @Override
    public AdministrativeObject findOne(@Nonnull ParamsHolder searchParams) {
        String name = searchParams.getString(AdministrativeObject.ADMIN_OBJ_NAME);
        String jdbcTemplateSelect = "select * from carinfo.admin_object where admin_obj_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, name));
    }

    @Override
    public List<AdministrativeObject> find() {
        String jdbcTemplateSelect = "select * from carinfo.admin_object;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public Page<AdministrativeObject> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.admin_object a ";
        String name = searchParams.getString(AdministrativeObject.ADMIN_OBJ_NAME);
        String where = StringUtils.isNoneBlank(name) ? " where a.admin_obj_name = " + name : "";
        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));
//
//        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
//        List<AdministrativeObject> result = jdbcTemplate.query(querySql, ROW_MAPPER);
//        return new PageImpl<>(result, pageable, total);

        return getPage(jdbcTemplate, ROW_MAPPER, pageable, select, from, where, total);
    }
}
