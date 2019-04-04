package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.raw.RegistrationRecord;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
@Slf4j
public class RegistrationRecordRepository extends JdbcRepository<RegistrationRecord> {
    private static final RowMapper<RegistrationRecord> ROW_MAPPER = (resultSet, i) -> RegistrationRecord.builder()
                                                                                                        .id(resultSet.getLong(Constants.RegistrationRecord.ID))
                                                                                                        .adminObjectId(resultSet.getLong(Constants.RegistrationRecord.ADMIN_OBJ_ID))
                                                                                                        .opCode(resultSet.getLong(Constants.RegistrationRecord.OPERATION_CODE))
                                                                                                        .depCode(resultSet.getLong(Constants.RegistrationRecord.DEPARTMENT_CODE))
                                                                                                        .kindId(resultSet.getLong(Constants.RegistrationRecord.KIND))
                                                                                                        .vehicleId(resultSet.getLong(Constants.RegistrationRecord.VEHICLE_ID))
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
                                                                                                        .build();

    @Autowired
    public RegistrationRecordRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public RegistrationRecord create(@NonNull @Nonnull RegistrationRecord entity) {
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

    private void setParamsForStatement(@Nonnull @NonNull RegistrationRecord entity, @NonNull PreparedStatement statement) throws SQLException {
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
    public RegistrationRecord update(@NonNull @Nonnull RegistrationRecord entity) {
        String jdbcTemplateUpdate = "update carinfo.record set admin_obj_id = ?," +
                " op_code = ?, dep_code = ?, kind_id = ?, vehicle_id = ?," +
                " color_id = ?, body_type_id = ?, purpose_id = ?, fuel_type_id = ?," +
                " own_weight = ?, total_weight = ?, engine_capacity = ?, make_year = ?," +
                " registration_date = ?, registration_number = ? " +
                "where vehicle_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getAdminObjectId(),
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
    public RegistrationRecord find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.record where id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationRecord> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.record;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(id) from carinfo.record where id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationRecord entity) {
        String jdbcTemplateSelectCount = "select count(id) from carinfo.record where registration_number = ? and registration_date = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), entity.getRegistrationNumber(), entity.getRegistrationDate());
        return numberOfRows > 0;
    }

    @Override
    public void createAll(Iterable<RegistrationRecord> entities) {
        final int batchSize = 100;
        List<List<RegistrationRecord>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationRecord> batch : batchLists) {
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
                    RegistrationRecord object = batch.get(i);
                    setParamsForStatement(object, ps);
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }
}
