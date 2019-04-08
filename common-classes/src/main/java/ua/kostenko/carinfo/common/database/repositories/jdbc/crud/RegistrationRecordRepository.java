package ua.kostenko.carinfo.common.database.repositories.jdbc.crud;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.ParamsHolder;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Registration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@Slf4j
public class RegistrationRecordRepository extends JdbcRepository<Registration> implements PageableRepository<Registration> {
    private static final RowMapper<Registration> ROW_MAPPER = (resultSet, i) -> Registration.builder()
                                                                                            .id(resultSet.getLong(Constants.RegistrationRecord.ID))
                                                                                            .adminObjectId(resultSet.getLong(Constants.RegistrationRecord.ADMIN_OBJ_ID))
                                                                                            .vehicleId(resultSet.getLong(Constants.RegistrationRecord.VEHICLE_ID))
                                                                                            .opCode(resultSet.getLong(Constants.RegistrationRecord.OPERATION_CODE))
                                                                                            .depCode(resultSet.getLong(Constants.RegistrationRecord.DEPARTMENT_CODE))
                                                                                            .kindId(resultSet.getLong(Constants.RegistrationRecord.KIND))
                                                                                            .colorId(resultSet.getLong(Constants.RegistrationRecord.COLOR_ID))
                                                                                            .bodyTypeId(resultSet.getLong(Constants.RegistrationRecord.BODY_TYPE_ID))
                                                                                            .purposeId(resultSet.getLong(Constants.RegistrationRecord.PURPOSE_ID))
                                                                                            .fuelTypeId(resultSet.getLong(Constants.RegistrationRecord.FUEL_TYPE_ID))
                                                                                            .engineCapacity(resultSet.getLong(Constants.RegistrationRecord.ENGINE_CAPACITY))
                                                                                            .ownWeight(resultSet.getLong(Constants.RegistrationRecord.OWN_WEIGHT))
                                                                                            .totalWeight(resultSet.getLong(Constants.RegistrationRecord.TOTAL_WEIGHT))
                                                                                            .makeYear(resultSet.getLong(Constants.RegistrationRecord.MAKE_YEAR))
                                                                                            .registrationNumber(resultSet.getString(Constants.RegistrationRecord.REGISTRATION_NUMBER))
                                                                                            .registrationDate(resultSet.getDate(Constants.RegistrationRecord.REGISTRATION_DATE))
                                                                                            .adminObjName(Constants.AdminObject.NAME)
                                                                                            .adminObjType(Constants.AdminObject.TYPE)
                                                                                            .operation(Constants.RegistrationOperation.NAME)
                                                                                            .departmentName(Constants.RegistrationDepartment.NAME)
                                                                                            .departmentAddress(Constants.RegistrationDepartment.ADDRESS)
                                                                                            .departmentEmail(Constants.RegistrationDepartment.EMAIL)
                                                                                            .kind(Constants.RegistrationKind.NAME)
                                                                                            .brand(Constants.RegistrationBrand.NAME)
                                                                                            .model(Constants.RegistrationModel.NAME)
                                                                                            .color(Constants.RegistrationColor.NAME)
                                                                                            .bodyType(Constants.RegistrationBodyType.NAME)
                                                                                            .purpose(Constants.RegistrationPurpose.NAME)
                                                                                            .fuelType(Constants.RegistrationFuelType.NAME)
                                                                                            .build();

    @Autowired
    public RegistrationRecordRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public Registration create(@NonNull @Nonnull Registration entity) {
        String jdbcTemplateInsert = "insert into carinfo.record (" +
                "admin_obj_id, " +
                "op_code, " +
                "dep_code, " +
                "kind_id, " +
                "vehicle_id, " +
                "color_id, " +
                "body_type_id, " +
                "purpose_id, " +
                "fuel_type_id, " +
                "own_weight, " +
                "total_weight, " +
                "engine_capacity, " +
                "make_year, " +
                "registration_date, " +
                "registration_number " +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(jdbcTemplateInsert, Statement.RETURN_GENERATED_KEYS);
            setParamsForStatement(entity, statement);
            return statement;
        }, keyHolder);
        Object id = keyHolder.getKeys().get(Constants.RegistrationRecord.ID);
        return find((long) id);
    }

    private void setParamsForStatement(@Nonnull @NonNull Registration entity, @NonNull PreparedStatement statement) throws SQLException {
        statement.setLong(1, entity.getAdminObjectId());
        statement.setLong(2, entity.getOpCode());
        statement.setLong(3, entity.getDepCode());
        statement.setLong(4, entity.getKindId());
        statement.setLong(5, entity.getVehicleId());
        statement.setLong(6, entity.getColorId());
        statement.setLong(7, entity.getBodyTypeId());
        statement.setLong(8, entity.getPurposeId());
        statement.setLong(9, entity.getFuelTypeId());
        statement.setLong(10, entity.getOwnWeight());
        statement.setLong(11, entity.getTotalWeight());
        statement.setLong(12, entity.getEngineCapacity());
        statement.setLong(13, entity.getMakeYear());
        statement.setDate(14, entity.getRegistrationDate());
        statement.setString(15, entity.getRegistrationNumber());
    }

    @Nullable
    @Override
    public Registration update(@NonNull @Nonnull Registration entity) {
        String jdbcTemplateUpdate = "update carinfo.record set admin_obj_id = ?," +
                " op_code = ?, dep_code = ?, kind_id = ?, vehicle_id = ?," +
                " color_id = ?, body_type_id = ?, purpose_id = ?, fuel_type_id = ?," +
                " own_weight = ?, total_weight = ?, engine_capacity = ?, make_year = ?," +
                " registration_date = ?, registration_number = ? " +
                "where id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate,
                            entity.getAdminObjectId(),
                            entity.getOpCode(),
                            entity.getDepCode(),
                            entity.getKindId(),
                            entity.getVehicleId(),
                            entity.getColorId(),
                            entity.getBodyTypeId(),
                            entity.getPurposeId(),
                            entity.getFuelTypeId(),
                            entity.getOwnWeight(),
                            entity.getTotalWeight(),
                            entity.getEngineCapacity(),
                            entity.getMakeYear(),
                            entity.getRegistrationDate(),
                            entity.getRegistrationNumber(), entity.getId());
        return find(entity.getVehicleId());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        String jdbcTemplateDelete = "delete from carinfo.record where id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public Registration find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * " +
                "from carinfo.record r, carinfo.admin_object ao, carinfo.operation o, carinfo.department d, carinfo.kind k, carinfo.vehicle v, carinfo.color c, carinfo.body_type bt," +
                " carinfo.purpose p, carinfo.fuel_type ft, carinfo.brand b, carinfo.model m " +
                "where ao.admin_obj_id =  r.admin_obj_id and o.op_code = r.op_code and d.dep_code = r.dep_code and k.kind_id = r.kind_id and v.vehicle_id = r.vehicle_id " +
                "and c.color_id = r.color_id and bt.body_type_id = r.body_type_id and p.purpose_id = r.purpose_id and ft.fuel_type_id = r.fuel_type_id and b.brand_id = v.brand_id " +
                "and m.model_id = v.model_id " +
                "and r.id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<Registration> findAll() {
        String jdbcTemplateSelect = "select * " +
                "from carinfo.record r, carinfo.admin_object ao, carinfo.operation o, carinfo.department d, carinfo.kind k, carinfo.vehicle v, carinfo.color c, carinfo.body_type bt," +
                " carinfo.purpose p, carinfo.fuel_type ft, carinfo.brand b, carinfo.model m " +
                "where ao.admin_obj_id =  r.admin_obj_id and o.op_code = r.op_code and d.dep_code = r.dep_code and k.kind_id = r.kind_id and v.vehicle_id = r.vehicle_id " +
                "and c.color_id = r.color_id and bt.body_type_id = r.body_type_id and p.purpose_id = r.purpose_id and ft.fuel_type_id = r.fuel_type_id and b.brand_id = v.brand_id " +
                "and m.model_id = v.model_id;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(id) from carinfo.record where id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Registration entity) {
        String jdbcTemplateSelectCount = "select count(id) from carinfo.record where registration_number = ? and registration_date = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), entity.getRegistrationNumber(), entity.getRegistrationDate());
        return numberOfRows > 0;
    }

    @Override
    public void createAll(Iterable<Registration> entities) {
        final int batchSize = 100;
        List<List<Registration>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<Registration> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.record (" +
                    "admin_obj_id, " +
                    "op_code, " +
                    "dep_code, " +
                    "kind_id, " +
                    "vehicle_id, " +
                    "color_id, " +
                    "body_type_id, " +
                    "purpose_id, " +
                    "fuel_type_id, " +
                    "own_weight, " +
                    "total_weight, " +
                    "engine_capacity, " +
                    "make_year, " +
                    "registration_date, " +
                    "registration_number " +
                    ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i) throws SQLException {
                    Registration object = batch.get(i);
                    setParamsForStatement(object, ps);
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public Page<Registration> find(@Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * " +
                "from carinfo.record r, carinfo.admin_object ao, carinfo.operation o, carinfo.department d, carinfo.kind k, carinfo.vehicle v, carinfo.color c, carinfo.body_type bt," +
                " carinfo.purpose p, carinfo.fuel_type ft, carinfo.brand b, carinfo.model m " +
                "where ao.admin_obj_id =  r.admin_obj_id and o.op_code = r.op_code and d.dep_code = r.dep_code and k.kind_id = r.kind_id and v.vehicle_id = r.vehicle_id " +
                "and c.color_id = r.color_id and bt.body_type_id = r.body_type_id and p.purpose_id = r.purpose_id and ft.fuel_type_id = r.fuel_type_id and b.brand_id = v.brand_id " +
                "and m.model_id = v.model_id;";

        String modelName = searchParams.getString(Constants.RegistrationModel.NAME);
        String brandName = searchParams.getString(Constants.RegistrationBrand.NAME);

        buildWhere()
//                .put("v.model_id", "m.model_id")
//                .put("v.brand_id", "b.brand_id")
//                .put("m.model_name", modelName)
//                .put("b.brand_name", brandName)
.build();

        String where = buildWhere().build();

        String countQuery = "select count(1) as row_count " + " from carinfo.vehicle v, carinfo.brand b, carinfo.model m  " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        String querySql = select + where + " limit ? offset ?";
        List<Registration> result = jdbcTemplate.query(querySql, ROW_MAPPER, limit, offset);
        return new PageImpl<>(result, pageable, total);
    }
}
