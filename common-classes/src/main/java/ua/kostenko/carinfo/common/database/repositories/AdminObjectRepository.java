package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class AdminObjectRepository extends CommonDBRepository<AdministrativeObject> {
    private static final RowMapper<AdministrativeObject> ROW_MAPPER = (resultSet, i) -> AdministrativeObject.builder()
                                                                                                            .adminObjId(resultSet.getLong(Constants.AdminObject.ID))
                                                                                                            .adminObjType(resultSet.getString(Constants.AdminObject.TYPE))
                                                                                                            .adminObjName(resultSet.getString(Constants.AdminObject.NAME))
                                                                                                            .build();

    @Autowired
    public AdminObjectRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<AdministrativeObject> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public AdministrativeObject create(@NonNull @Nonnull AdministrativeObject entity) {
        String jdbcTemplateInsert = "insert into carinfo.admin_object (admin_obj_id, admin_obj_type, admin_obj_name) values (?, ?, ?);";
        return create(jdbcTemplateInsert, Constants.AdminObject.ID, entity.getAdminObjId(), entity.getAdminObjType(), entity.getAdminObjName());
    }

    @Nullable
    @Override
    public AdministrativeObject update(@NonNull @Nonnull AdministrativeObject entity) {
        String jdbcTemplateUpdate = "update carinfo.admin_object set admin_obj_type = ?, admin_obj_name = ? where admin_obj_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getAdminObjType(), entity.getAdminObjName(), entity.getAdminObjId());
        ParamsHolder searchParams = getParamsHolderBuilder().param(AdministrativeObject.ADMIN_OBJ_NAME, entity.getAdminObjName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.admin_object where admin_obj_id = ?;";
        return delete(jdbcTemplateDelete, id);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(admin_obj_id) from carinfo.admin_object where admin_obj_id = ?;";
        return exist(jdbcTemplateSelectCount, id);
    }

    @Override
    public boolean exist(@NonNull @Nonnull AdministrativeObject entity) {
        String name = entity.getAdminObjName();
        String jdbcTemplateSelectCount = "select count(admin_obj_id) from carinfo.admin_object where admin_obj_name = ?;";
        return exist(jdbcTemplateSelectCount, name);
    }

    @Nullable
    @Override
    public AdministrativeObject findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.admin_object where admin_obj_id = ?;";
        return findOne(jdbcTemplateSelect, id);
    }

    @Cacheable(cacheNames = "adminObj", unless = "#result != null")
    @Nullable
    @Override
    public AdministrativeObject findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(AdministrativeObject.ADMIN_OBJ_NAME);
        String jdbcTemplateSelect = "select * from carinfo.admin_object where admin_obj_name = ?;";
        return findOne(jdbcTemplateSelect, param);
    }

    @Override
    public List<AdministrativeObject> find() {
        String jdbcTemplateSelect = "select * from carinfo.admin_object;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<AdministrativeObject> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.admin_object a ";
        String name = searchParams.getString(AdministrativeObject.ADMIN_OBJ_NAME);
        String where = StringUtils.isNoneBlank(name) ? " where a.admin_obj_name = " + name : "";
        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, FIND_TOTAL_MAPPER);

//        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
//        List<AdministrativeObject> result = jdbcTemplate.query(querySql, ROW_MAPPER);
//        return new PageImpl<>(result, pageable, total);

        return getPage(jdbcTemplate, ROW_MAPPER, pageable, select, from, where, total);
    }
}
