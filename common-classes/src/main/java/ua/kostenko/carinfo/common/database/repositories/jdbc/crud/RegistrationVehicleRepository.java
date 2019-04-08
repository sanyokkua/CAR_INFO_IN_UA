package ua.kostenko.carinfo.common.database.repositories.jdbc.crud;

import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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
import ua.kostenko.carinfo.common.records.Vehicle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationVehicleRepository extends JdbcRepository<Vehicle> implements PageableRepository<Vehicle> {
    private static final String BRAND = "brand";
    private static final String MODEL = "model";
    private static final RowMapper<Vehicle> ROW_MAPPER = (resultSet, i) -> Vehicle.builder()
                                                                                  .vehicleId(resultSet.getLong(Constants.RegistrationVehicle.ID))
                                                                                  .brandId(resultSet.getLong(Constants.RegistrationVehicle.BRAND_ID))
                                                                                  .modelId(resultSet.getLong(Constants.RegistrationVehicle.MODEL_ID))
                                                                                  .registrationModel(resultSet.getString(MODEL))
                                                                                  .registrationBrand(resultSet.getString(BRAND))
                                                                                  .build();

    @Autowired
    public RegistrationVehicleRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Nullable
    @Override
    public Vehicle create(@NonNull @Nonnull Vehicle entity) {
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
    public Vehicle update(@NonNull @Nonnull Vehicle entity) {
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
    public Vehicle find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model " +
                "from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where vehicle_id = ? and v.brand_id = b.brand_id and m.model_id = v.model_id;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<Vehicle> findAll() {
        String jdbcTemplateSelect = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model " +
                "from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where v.brand_id = b.brand_id and m.model_id = v.model_id;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(vehicle_id) from carinfo.vehicle where vehicle_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull Vehicle entity) {
        return nonNull(findByFields(entity.getBrandId(), entity.getModelId()));
    }

    @Override
    public void createAll(Iterable<Vehicle> entities) {
        final int batchSize = 100;
        List<List<Vehicle>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<Vehicle> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.vehicle (brand_id, model_id) values (?, ?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i) throws SQLException {
                    Vehicle object = batch.get(i);
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
    public Vehicle findByFields(@NonNull Long brandId, @NonNull Long modelId) {
        String jdbcTemplateSelect = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model " +
                "from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where v.brand_id = b.brand_id and m.model_id = v.model_id and v.brand_id = ? and v.model_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, brandId, modelId));
    }

    @Override
    public Page<Vehicle> find(@Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.vehicle v, carinfo.brand b, carinfo.model m ";

        String modelName = searchParams.getString(Constants.RegistrationModel.NAME);
        String brandName = searchParams.getString(Constants.RegistrationBrand.NAME);

        String where = buildWhere()
                .add("v.model_id", "m.model_id")
                .add("v.brand_id", "b.brand_id")
                .add("m.model_name", modelName)
                .add("b.brand_name", brandName)
                .build();

        String countQuery = "select count(1) as row_count " + " from carinfo.vehicle v, carinfo.brand b, carinfo.model m  " + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        String querySql = select + where + " limit ? offset ?";
        List<Vehicle> result = jdbcTemplate.query(querySql, ROW_MAPPER, limit, offset);
        return new PageImpl<>(result, pageable, total);
    }
}
