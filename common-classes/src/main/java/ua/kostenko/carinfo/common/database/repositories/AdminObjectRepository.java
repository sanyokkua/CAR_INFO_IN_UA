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
    public AdminObjectRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<AdministrativeObject> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public AdministrativeObject create(@NonNull @Nonnull AdministrativeObject entity) {
        String jdbcTemplateInsert = "insert into carinfo.admin_object (admin_obj_id, admin_obj_type, admin_obj_name) values (:id, :type, :name);";
        SqlParameterSource params = getSqlParamBuilder()
                .addParam("id", entity.getAdminObjId())
                .addParam("type", entity.getAdminObjType())
                .addParam("name", entity.getAdminObjName())
                .build();
        return create(jdbcTemplateInsert, Constants.AdminObject.ID, params);
    }

    @Nullable
    @Override
    public AdministrativeObject update(@NonNull @Nonnull AdministrativeObject entity) {
        String jdbcTemplateUpdate = "update carinfo.admin_object set admin_obj_type = :type, admin_obj_name = :name where admin_obj_id = :id;";
        SqlParameterSource params = getSqlParamBuilder()
                .addParam("id", entity.getAdminObjId())
                .addParam("type", entity.getAdminObjType())
                .addParam("name", entity.getAdminObjName())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, params);
        ParamsHolder searchParams = getParamsHolderBuilder().param(AdministrativeObject.ADMIN_OBJ_NAME, entity.getAdminObjName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.admin_object where admin_obj_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(admin_obj_id) from carinfo.admin_object where admin_obj_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "adminObjCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull AdministrativeObject entity) {
        String jdbcTemplateSelectCount = "select count(admin_obj_id) from carinfo.admin_object where admin_obj_name = :name;";
        SqlParameterSource params = getSqlParamBuilder().addParam("name", entity.getAdminObjName()).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Nullable
    @Override
    public AdministrativeObject findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.admin_object where admin_obj_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return findOne(jdbcTemplateSelect, params);
    }

    @Cacheable(cacheNames = "adminObj", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public AdministrativeObject findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(AdministrativeObject.ADMIN_OBJ_NAME);
        String jdbcTemplateSelect = "select * from carinfo.admin_object where admin_obj_name = :name;";
        SqlParameterSource params = getSqlParamBuilder().addParam("name", param).build();
        return findOne(jdbcTemplateSelect, params);
    }

    @Override
    public List<AdministrativeObject> find() {
        String jdbcTemplateSelect = "select * from carinfo.admin_object;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<AdministrativeObject> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.admin_object a ";
        String name = searchParams.getString(AdministrativeObject.ADMIN_OBJ_NAME);
        return findPage(searchParams, select, from, buildWhere()
                .addFieldParam("a.admin_obj_name", "name", name));
    }
}
