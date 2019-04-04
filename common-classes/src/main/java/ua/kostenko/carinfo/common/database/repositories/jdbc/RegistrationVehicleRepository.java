package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.raw.RegistrationVehicle;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationVehicleRepository extends JdbcRepository<RegistrationVehicle> {
    private static final RowMapper<RegistrationVehicle> ROW_MAPPER = (resultSet, i) -> RegistrationVehicle.builder()
                                                                                                          .vehicleId(resultSet.getLong(Constants.RegistrationVehicle.ID))
                                                                                                          .brandId(resultSet.getLong(Constants.RegistrationVehicle.BRAND_ID))
                                                                                                          .modelId(resultSet.getLong(Constants.RegistrationVehicle.MODEL_ID))
                                                                                                          .build();

    @Autowired
    public RegistrationVehicleRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public RegistrationVehicle create(@NonNull @Nonnull RegistrationVehicle entity) {
        String jdbcTemplateInsert = "insert into carinfo.vehicle (brand_id, model_id) values (?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(jdbcTemplateInsert, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, entity.getModelId());
            statement.setLong(2, entity.getBrandId());
            return statement;
        }, keyHolder);
        Object id = keyHolder.getKeys().get(Constants.RegistrationVehicle.ID);
        return find((long) id);
    }

    @Nullable
    @Override
    public RegistrationVehicle update(@NonNull @Nonnull RegistrationVehicle entity) {
        String jdbcTemplateUpdate = "update carinfo.vehicle set brand_id = ?, model_id = ? where vehicle_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getBrandId(), entity.getModelId(), entity.getVehicleId());
        return find(entity.getVehicleId());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        String jdbcTemplateDelete = "delete from carinfo.vehicle where vehicle_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    @Cacheable("vehicles_id")
    public RegistrationVehicle find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.vehicle v where vehicle_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationVehicle> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.vehicle;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(vehicle_id) from carinfo.vehicle where vehicle_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationVehicle entity) {
        return nonNull(findByFields(entity.getBrandId(), entity.getModelId()));
    }

    @Override
    public void createAll(Iterable<RegistrationVehicle> entities) {
        final int batchSize = 100;
        List<List<RegistrationVehicle>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationVehicle> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.vehicle (brand_id, model_id) values (?, ?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i) throws SQLException {
                    RegistrationVehicle object = batch.get(i);
                    ps.setLong(1, object.getBrandId());
                    ps.setLong(2, object.getModelId());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Cacheable("vehicles")
    public RegistrationVehicle findByFields(@NonNull Long brandId, @NonNull Long modelId) {
        String jdbcTemplateSelect = "select * from carinfo.vehicle v where brand_id = ? and model_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, brandId, modelId));
    }
}
