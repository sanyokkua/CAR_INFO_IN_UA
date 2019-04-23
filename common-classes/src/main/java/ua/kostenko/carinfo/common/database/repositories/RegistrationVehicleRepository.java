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
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.api.records.Vehicle;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
class RegistrationVehicleRepository extends CommonDBRepository<Vehicle> {
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
        ParamsHolder brandSearch = getParamsHolderBuilder().param(Brand.BRAND_NAME, entity.getBrandName()).build();
        Brand brand = brandCommonDBRepository.findOne(brandSearch);
        if (Objects.isNull(brand)) {
            log.warn("create: brand is null. Return null instead of creating");
            return null;
        }
        ParamsHolder modelSearch = getParamsHolderBuilder().param(Model.MODEL_NAME, entity.getModelName()).build();
        Model model = modelCommonDBRepository.findOne(modelSearch);
        if (Objects.isNull(model)) {
            log.warn("create: model is null. Return null instead of creating");
            return null;
        }
        String jdbcTemplateInsert = "insert into carinfo.vehicle (brand_id, model_id) values (?, ?);";
        return create(jdbcTemplateInsert, Constants.RegistrationVehicle.ID, brand.getBrandId(), model.getModelId());
    }

    @Nullable
    @Override
    public Vehicle update(@NonNull @Nonnull Vehicle entity) {
        ParamsHolder brandSearch = getParamsHolderBuilder().param(Brand.BRAND_NAME, entity.getBrandName()).build();
        Brand brand = brandCommonDBRepository.findOne(brandSearch);
        if (Objects.isNull(brand)) {
            log.warn("update: brand is null. Return null instead of update");
            return null;
        }
        ParamsHolder modelSearch = getParamsHolderBuilder().param(Model.MODEL_NAME, entity.getModelName()).build();
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
        return delete(jdbcTemplateDelete, id);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(vehicle_id) from carinfo.vehicle where vehicle_id = ?;";
        return exist(jdbcTemplateSelectCount, id);
    }

    @Override
    public boolean exist(@NonNull @Nonnull Vehicle entity) {
        String jdbcTemplateSelectCount = "select count(v.vehicle_id) from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where v.brand_id = b.brand_id and m.model_id = v.model_id and m.model_name = ? and b.brand_name = ?;";
        return exist(jdbcTemplateSelectCount, entity.getModelName(), entity.getBrandName());
    }

    @Nullable
    @Override
    public Vehicle findOne(long id) {
        String jdbcTemplateSelect = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model " +
                "from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where vehicle_id = ? and v.brand_id = b.brand_id and m.model_id = v.model_id;";
        return findOne(jdbcTemplateSelect, id);
    }

    @Cacheable(cacheNames = "vehicle", unless = "#result != null")
    @Nullable
    @Override
    public Vehicle findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String brandName = searchParams.getString(Vehicle.BRAND_NAME);
        String modelName = searchParams.getString(Vehicle.MODEL_NAME);

        ParamsHolder brandSearch = getParamsHolderBuilder().param(Brand.BRAND_NAME, brandName).build();
        Brand brand = brandCommonDBRepository.findOne(brandSearch);
        if (Objects.isNull(brand)) {
            log.warn("findOne: brand is null. Return null instead of findOne");
            return null;
        }
        ParamsHolder modelSearch = getParamsHolderBuilder().param(Model.MODEL_NAME, modelName).build();
        Model model = modelCommonDBRepository.findOne(modelSearch);
        if (Objects.isNull(model)) {
            log.warn("findOne: model is null. Return null instead of findOne");
            return null;
        }

        String jdbcTemplateSelect = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model " +
                "from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where v.brand_id = b.brand_id and m.model_id = v.model_id and v.brand_id = ? and v.model_id = ?;";
        return findOne(jdbcTemplateSelect, brand.getBrandId(), model.getModelId());
    }

    @Override
    public List<Vehicle> find() {
        String jdbcTemplateSelect = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model " +
                "from carinfo.vehicle v, carinfo.brand b, carinfo.model m " +
                "where v.brand_id = b.brand_id and m.model_id = v.model_id;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<Vehicle> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select v.vehicle_id, v.model_id, v.brand_id, b.brand_name as brand, m.model_name as model from carinfo.vehicle v, carinfo.brand b, carinfo.model m ";
        String modelName = searchParams.getString(Vehicle.MODEL_NAME);
        String brandName = searchParams.getString(Vehicle.BRAND_NAME);

        String where = buildWhere()
                .add("v.model_id", "m.model_id", false)
                .add("v.brand_id", "b.brand_id", false)
                .add("m.model_name", modelName, true)
                .add("b.brand_name", brandName, true)
                .build();

        String countQuery = "select count(1) as row_count " + " from carinfo.vehicle v, carinfo.brand b, carinfo.model m  " + where;
        int total = jdbcTemplate.queryForObject(countQuery, FIND_TOTAL_MAPPER);

        int limit = pageable.getPageSize();
        long offset = pageable.getOffset();
        String querySql = select + where + " limit ? offset ?";
        List<Vehicle> result = jdbcTemplate.query(querySql, ROW_MAPPER, limit, offset);
        return new PageImpl<>(result, pageable, total);
    }

    @Override
    RowMapper<Vehicle> getRowMapper() {
        return ROW_MAPPER;
    }
}
