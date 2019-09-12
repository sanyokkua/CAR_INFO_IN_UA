package ua.kostenko.carinfo.common.database.repositories;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.api.records.Vehicle;
import ua.kostenko.carinfo.common.database.Constants;

@Repository
@Slf4j
class RegistrationVehicleRepository extends CommonDBRepository<Vehicle, String> {

    protected static final String BRAND_PARAM = "brand";
    protected static final String MODEL_PARAM = "model";
    private static final RowMapper<Vehicle> ROW_MAPPER = (resultSet, i) -> Vehicle.builder()
            .vehicleId(resultSet.getLong(Constants.RegistrationVehicle.ID))
            .modelName(resultSet.getString(Constants.RegistrationModel.NAME))
            .brandName(resultSet.getString(Constants.RegistrationBrand.NAME))
            .build();
    private final DBRepository<Brand, String> brandCommonDBRepository;
    private final DBRepository<Model, String> modelCommonDBRepository;

    @Autowired
    public RegistrationVehicleRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate,
            @NonNull @Nonnull DBRepository<Brand, String> brandCommonDBRepository,
            @NonNull @Nonnull DBRepository<Model, String> modelCommonDBRepository) {
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
        String jdbcTemplateInsert = "insert into carinfo.vehicle (brand_id, model_id) values (:brand, :model);";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam(BRAND_PARAM, brand.getBrandId())
                .addParam(MODEL_PARAM, model.getModelId())
                .build();
        return create(jdbcTemplateInsert, Constants.RegistrationVehicle.ID, parameterSource);
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
        String jdbcTemplateUpdate =
                "update carinfo.vehicle set brand_id = :brand, model_id = :model where vehicle_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam(BRAND_PARAM, brand.getBrandId())
                .addParam(MODEL_PARAM, model.getModelId())
                .addParam(ID_PARAM, entity.getVehicleId())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        return findOne(entity.getVehicleId());
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.vehicle where vehicle_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(vehicle_id) from carinfo.vehicle where vehicle_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "vehicleCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull Vehicle entity) {
        String jdbcTemplateSelectCount =
                "select count(vehicle_id) from carinfo.vehicle_view where model_name = :model and brand_name = :brand;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam(MODEL_PARAM, entity.getModelName())
                .addParam(BRAND_PARAM, entity.getBrandName())
                .build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public Vehicle findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.vehicle_view where vehicle_id = :id";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "vehicle", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public Vehicle findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String brandName = searchParams.getString(Vehicle.BRAND_NAME);
        String modelName = searchParams.getString(Vehicle.MODEL_NAME);
        if (StringUtils.isBlank(brandName) || StringUtils.isBlank(modelName)) {
            log.warn("Brand ({}) or Model ({}) is null", brandName, modelName);
            return null;
        }
        String jdbcTemplateSelect =
                "select * from carinfo.vehicle_view where brand_name = :brand and model_name = :model;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(MODEL_PARAM, modelName)
                .addParam(BRAND_PARAM, brandName)
                .build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public List<Vehicle> find() {
        String jdbcTemplateSelect = "select * from carinfo.vehicle_view ";
        return find(jdbcTemplateSelect);
    }

    @Cacheable(cacheNames = "vehicleIndex", unless = "#result == false ", key = "#indexField")
    @Override
    public boolean existsByIndex(@Nonnull @NonNull String indexField) {
        String jdbcTemplateSelectCount =
                "select count(vehicle_id) from carinfo.vehicle_view where model_name = :model;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam(MODEL_PARAM, indexField)
                .build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Override
    public Page<Vehicle> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.vehicle_view ";
        String modelName = searchParams.getString(Vehicle.MODEL_NAME);
        String brandName = searchParams.getString(Vehicle.BRAND_NAME);
        return findPage(searchParams, select, from, buildWhere()
                .addFieldParam(Constants.RegistrationModel.NAME, MODEL_PARAM, modelName)
                .addFieldParam(Constants.RegistrationBrand.NAME, BRAND_PARAM, brandName));
    }

    @Override
    RowMapper<Vehicle> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    WhereBuilder.BuildResult getWhereFromParams(ParamsHolder params) {
        String modelName = params.getString(Vehicle.MODEL_NAME);
        String brandName = params.getString(Vehicle.BRAND_NAME);
        return buildWhere()
                .addFieldParam(Constants.RegistrationModel.NAME, MODEL_PARAM, modelName)
                .addFieldParam(Constants.RegistrationBrand.NAME, BRAND_PARAM, brandName).build();
    }

    @Override
    String getTableName() {
        return Constants.RegistrationVehicle.TABLE_VIEW;
    }
}
