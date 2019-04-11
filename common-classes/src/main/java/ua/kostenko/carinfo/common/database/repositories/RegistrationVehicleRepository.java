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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.api.Constants;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.api.records.Vehicle;
import ua.kostenko.carinfo.common.database.repositories.jdbc.crud.CrudRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationVehicleRepository extends CommonDBRepository<Vehicle> {
    private static final String BRAND = "brand";
    private static final String MODEL = "model";
    private static final RowMapper<Vehicle> ROW_MAPPER = (resultSet, i) -> Vehicle.builder()
                                                                                  .vehicleId(resultSet.getLong(Constants.RegistrationVehicle.ID))
                                                                                  .modelName(resultSet.getString(MODEL))
                                                                                  .brandName(resultSet.getString(BRAND))
                                                                                  .build();
    private final DBRepository<Brand> brandCommonDBRepository;
    private final DBRepository<Model> modelCommonDBRepository;

    @Autowired
    public RegistrationVehicleRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate,
                                         @NonNull @Nonnull DBRepository<Brand> brandCommonDBRepository,
                                         @NonNull @Nonnull DBRepository<Model> modelCommonDBRepository) {
        super(jdbcTemplate);
        this.brandCommonDBRepository = brandCommonDBRepository;
        this.modelCommonDBRepository = modelCommonDBRepository;
    }

    @Nullable
    @Override
    public Vehicle create(@NonNull @Nonnull Vehicle entity) {
        ParamsHolder brandSearch = getBuilder().param(Brand.BRAND_NAME, entity.getBrandName()).build();
        Brand brand = brandCommonDBRepository.findOne(brandSearch);
        if (Objects.isNull(brand)) {
            log.warn("create: brand is null. Return null instead of creating");
            return null;
        }
        ParamsHolder modelSearch = getBuilder().param(Model.MODEL_NAME, entity.getModelName()).build();
        Model model = modelCommonDBRepository.findOne(modelSearch);
        if (Objects.isNull(model)) {
            log.warn("create: model is null. Return null instead of creating");
            return null;
        }

        String jdbcTemplateInsert = "insert into carinfo.vehicle (brand_id, model_id) values (?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(jdbcTemplateInsert, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, brand.getBrandId());
            statement.setLong(2, model.getModelId());
            return statement;
        }, keyHolder);
        Object id = keyHolder.getKeys().get(Constants.RegistrationVehicle.ID);
        return findOne((long) id);
    }

    @Nullable
    @Override
    public Vehicle update(@NonNull @Nonnull Vehicle entity) {
        ParamsHolder brandSearch = getBuilder().param(Brand.BRAND_NAME, entity.getBrandName()).build();
        Brand brand = brandCommonDBRepository.findOne(brandSearch);
        if (Objects.isNull(brand)) {
            log.warn("update: brand is null. Return null instead of update");
            return null;
        }
        ParamsHolder modelSearch = getBuilder().param(Model.MODEL_NAME, entity.getModelName()).build();
        Model model = modelCommonDBRepository.findOne(modelSearch);
        if (Objects.isNull(model)) {
            log.warn("update: model is null. Return null instead of update");
            return null;
        }

        String jdbcTemplateUpdate = "update carinfo.vehicle set brand_id = ?, model_id = ? where vehicle_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, brand.getBrandId(), model.getModelId(), entity.getVehicleId());
        return findOne(entity.getVehicleId());
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.vehicle where vehicle_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(vehicle_id) from carinfo.vehicle where vehicle_id = ?;";
        Long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return Objects.nonNull(numberOfRows) && numberOfRows > 0;
    }

    @Override
    public boolean exist(@Nonnull Vehicle entity) {
        ParamsHolder searchParams = getBuilder()
                .param(Vehicle.BRAND_NAME, entity.getBrandName())
                .param(Vehicle.MODEL_NAME, entity.getModelName())
                .build();
        return nonNull(findOne(searchParams));
    }

    @Nullable
    @Override
    public Vehicle findOne(long id) {
        String jdbcTemplateSelect = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model " +
                "from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where vehicle_id = ? and v.brand_id = b.brand_id and m.model_id = v.model_id;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Nullable
    @Override
    public Vehicle findOne(@Nonnull ParamsHolder searchParams) {
        String brandName = searchParams.getString(Vehicle.BRAND_NAME);
        String modelName = searchParams.getString(Vehicle.MODEL_NAME);

        ParamsHolder brandSearch = getBuilder().param(Brand.BRAND_NAME, brandName).build();
        Brand brand = brandCommonDBRepository.findOne(brandSearch);
        if (Objects.isNull(brand)) {
            log.warn("findOne: brand is null. Return null instead of findOne");
            return null;
        }
        ParamsHolder modelSearch = getBuilder().param(Model.MODEL_NAME, modelName).build();
        Model model = modelCommonDBRepository.findOne(modelSearch);
        if (Objects.isNull(model)) {
            log.warn("findOne: model is null. Return null instead of findOne");
            return null;
        }

        String jdbcTemplateSelect = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model " +
                "from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where v.brand_id = b.brand_id and m.model_id = v.model_id and v.brand_id = ? and v.model_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, brand.getBrandId(), model.getModelId()));
    }

    @Override
    public List<Vehicle> find() {
        String jdbcTemplateSelect = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model " +
                "from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where v.brand_id = b.brand_id and m.model_id = v.model_id;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public Page<Vehicle> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model from carinfo.vehicle v, carinfo.brand b, carinfo.model m ";
        String modelName = searchParams.getString(Vehicle.MODEL_NAME);
        String brandName = searchParams.getString(Vehicle.BRAND_NAME);

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
