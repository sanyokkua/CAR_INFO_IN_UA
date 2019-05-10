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
import ua.kostenko.carinfo.common.api.records.*;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

import static ua.kostenko.carinfo.common.api.records.Registration.*;

@Repository
@Slf4j
class RegistrationRecordRepository extends CommonDBRepository<Registration, String> {
    protected static final String ADMIN_OBJ_NAME_PARAM = "admin_obj_name";
    protected static final String ADMIN_OBJ_TYPE_PARAM = "admin_obj_type";
    protected static final String OP_CODE_PARAM = "op_code";
    protected static final String OP_NAME_PARAM = "op_name";
    protected static final String DEP_CODE_PARAM = "dep_code";
    protected static final String DEP_ADDR_PARAM = "dep_addr";
    protected static final String DEP_EMAIL_PARAM = "dep_email";
    protected static final String KIND_NAME_PARAM = "kind_name";
    protected static final String COLOR_NAME_PARAM = "color_name";
    protected static final String BODY_TYPE_NAME_PARAM = "body_type_name";
    protected static final String PURPOSE_NAME_PARAM = "purpose_name";
    protected static final String BRAND_NAME_PARAM = "brand_name";
    protected static final String MODEL_NAME_PARAM = "model_name";
    protected static final String FUEL_TYPE_NAME_PARAM = "fuel_type_name";
    protected static final String ENGINE_CAPACITY_PARAM = "engine_capacity";
    protected static final String MAKE_YEAR_PARAM = "make_year";
    protected static final String OWN_WEIGHT_PARAM = "own_weight";
    protected static final String TOTAL_WEIGHT_PARAM = "total_weight";
    protected static final String PERSON_TYPE_PARAM = "person_type";
    protected static final String REGISTRATION_NUMBER_PARAM = "registration_number";
    protected static final String REGISTRATION_DATE_PARAM = "registration_date";
    private static final RowMapper<Registration> ROW_MAPPER = (resultSet, i) -> Registration.builder()
                                                                                            .adminObjName(resultSet.getString(Constants.AdminObject.NAME))
                                                                                            .adminObjType(resultSet.getString(Constants.AdminObject.TYPE))
                                                                                            .operationCode(resultSet.getLong(Constants.RegistrationOperation.CODE))
                                                                                            .operationName(resultSet.getString(Constants.RegistrationOperation.NAME))
                                                                                            .departmentCode(resultSet.getLong(Constants.RegistrationDepartment.CODE))
                                                                                            .departmentAddress(resultSet.getString(Constants.RegistrationDepartment.ADDRESS))
                                                                                            .departmentEmail(resultSet.getString(Constants.RegistrationDepartment.EMAIL))
                                                                                            .kindName(resultSet.getString(Constants.RegistrationKind.NAME))
                                                                                            .colorName(resultSet.getString(Constants.RegistrationColor.NAME))
                                                                                            .bodyTypeName(resultSet.getString(Constants.RegistrationBodyType.NAME))
                                                                                            .purposeName(resultSet.getString(Constants.RegistrationPurpose.NAME))
                                                                                            .brandName(resultSet.getString(Constants.RegistrationBrand.NAME))
                                                                                            .modelName(resultSet.getString(Constants.RegistrationModel.NAME))
                                                                                            .fuelTypeName(resultSet.getString(Constants.RegistrationFuelType.NAME))
                                                                                            .engineCapacity(resultSet.getLong(Constants.RegistrationRecord.ENGINE_CAPACITY))
                                                                                            .makeYear(resultSet.getLong(Constants.RegistrationRecord.MAKE_YEAR))
                                                                                            .ownWeight(resultSet.getLong(Constants.RegistrationRecord.OWN_WEIGHT))
                                                                                            .totalWeight(resultSet.getLong(Constants.RegistrationRecord.TOTAL_WEIGHT))
                                                                                            .personType(resultSet.getString(Constants.RegistrationRecord.PERSON_TYPE))
                                                                                            .registrationNumber(resultSet.getString(Constants.RegistrationRecord.REGISTRATION_NUMBER))
                                                                                            .registrationDate(resultSet.getDate(Constants.RegistrationRecord.REGISTRATION_DATE))
                                                                                            .id(resultSet.getLong(Constants.RegistrationRecord.ID))
                                                                                            .build();
    private final DBRepository<AdministrativeObject, String> administrativeObjectDBRepository;
    private final DBRepository<BodyType, String> bodyTypeDBRepository;
    private final DBRepository<Color, String> colorDBRepository;
    private final DBRepository<Department, Long> departmentDBRepository;
    private final DBRepository<FuelType, String> fuelTypeDBRepository;
    private final DBRepository<Kind, String> kindDBRepository;
    private final DBRepository<Operation, Long> operationDBRepository;
    private final DBRepository<Purpose, String> purposeDBRepository;
    private final DBRepository<Vehicle, String> vehicleDBRepository;

    @Autowired
    public RegistrationRecordRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate,
                                        @NonNull @Nonnull DBRepository<AdministrativeObject, String> administrativeObjectDBRepository,
                                        @NonNull @Nonnull DBRepository<BodyType, String> bodyTypeDBRepository,
                                        @NonNull @Nonnull DBRepository<Color, String> colorDBRepository,
                                        @NonNull @Nonnull DBRepository<Department, Long> departmentDBRepository,
                                        @NonNull @Nonnull DBRepository<FuelType, String> fuelTypeDBRepository,
                                        @NonNull @Nonnull DBRepository<Kind, String> kindDBRepository,
                                        @NonNull @Nonnull DBRepository<Operation, Long> operationDBRepository,
                                        @NonNull @Nonnull DBRepository<Purpose, String> purposeDBRepository,
                                        @NonNull @Nonnull DBRepository<Vehicle, String> vehicleDBRepository) {
        super(jdbcTemplate);
        this.administrativeObjectDBRepository = administrativeObjectDBRepository;
        this.bodyTypeDBRepository = bodyTypeDBRepository;
        this.colorDBRepository = colorDBRepository;
        this.departmentDBRepository = departmentDBRepository;
        this.fuelTypeDBRepository = fuelTypeDBRepository;
        this.kindDBRepository = kindDBRepository;
        this.operationDBRepository = operationDBRepository;
        this.purposeDBRepository = purposeDBRepository;
        this.vehicleDBRepository = vehicleDBRepository;
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
                "registration_number, " +
                "person_type " +
                ") values (:adminObjId, :operationCode, " +
                ":departmentCode, :kindId, :vehicleId, " +
                ":colorId, :bodyTypeId, :purposeId, " +
                ":fuelTypeId, :ownWeight, :totalWeight, " +
                ":engineCapacity, :makeYear, :registrationDate, " +
                ":registrationNumber, :personType);";
        SqlParameterMap paramBuilder = getSqlParamBuilder();
        setParamsToBuilder(entity, paramBuilder);
        return create(jdbcTemplateInsert, Constants.RegistrationRecord.ID, paramBuilder.build());
    }

    private void setParamsToBuilder(@Nonnull @NonNull Registration entity, @NonNull @Nonnull SqlParameterMap builder) {
        AdministrativeObject administrativeObject = administrativeObjectDBRepository.findOne(getParamsHolderBuilder().param(AdministrativeObject.ADMIN_OBJ_NAME, entity.getAdminObjName()).build());
        BodyType bodyType = bodyTypeDBRepository.findOne(getParamsHolderBuilder().param(BodyType.BODY_TYPE_NAME, entity.getBodyTypeName()).build());
        Color color = colorDBRepository.findOne(getParamsHolderBuilder().param(Color.COLOR_NAME, entity.getColorName()).build());
        Department department = departmentDBRepository.findOne(getParamsHolderBuilder().param(Department.DEPARTMENT_CODE, entity.getDepartmentCode()).build());
        FuelType fuelType = fuelTypeDBRepository.findOne(getParamsHolderBuilder().param(FuelType.FUEL_NAME, entity.getFuelTypeName()).build());
        Kind kind = kindDBRepository.findOne(getParamsHolderBuilder().param(Kind.KIND_NAME, entity.getKindName()).build());
        Operation operation = operationDBRepository.findOne(getParamsHolderBuilder().param(Operation.OPERATION_CODE, entity.getOperationCode()).build());
        Purpose purpose = purposeDBRepository.findOne(getParamsHolderBuilder().param(Purpose.PURPOSE_NAME, entity.getPurposeName()).build());
        Vehicle vehicle = vehicleDBRepository.findOne(getParamsHolderBuilder().param(Vehicle.BRAND_NAME, entity.getBrandName()).param(Vehicle.MODEL_NAME, entity.getModelName()).build());
        Long adminObjId = Objects.nonNull(administrativeObject) ? administrativeObject.getAdminObjId() : null;
        Long operationCode = Objects.nonNull(operation) ? operation.getOperationCode() : null;
        Long departmentCode = Objects.nonNull(department) ? department.getDepartmentCode() : null;
        Long kindId = Objects.nonNull(kind) ? kind.getKindId() : null;
        Long vehicleId = Objects.nonNull(vehicle) ? vehicle.getVehicleId() : null;
        Long colorId = Objects.nonNull(color) ? color.getColorId() : null;
        Long bodyTypeId = Objects.nonNull(bodyType) ? bodyType.getBodyTypeId() : null;
        Long purposeId = Objects.nonNull(purpose) ? purpose.getPurposeId() : null;
        Long fuelTypeId = Objects.nonNull(fuelType) ? fuelType.getFuelTypeId() : null;

        builder.addParam("adminObjId", adminObjId)
               .addParam("operationCode", operationCode)
               .addParam("departmentCode", departmentCode)
               .addParam("kindId", kindId)
               .addParam("vehicleId", vehicleId)
               .addParam("colorId", colorId)
               .addParam("bodyTypeId", bodyTypeId)
               .addParam("purposeId", purposeId)
               .addParam("fuelTypeId", fuelTypeId)
               .addParam("ownWeight", entity.getOwnWeight())
               .addParam("totalWeight", entity.getTotalWeight())
               .addParam("engineCapacity", entity.getEngineCapacity())
               .addParam("makeYear", entity.getMakeYear())
               .addParam("registrationDate", entity.getRegistrationDate())
               .addParam("registrationNumber", entity.getRegistrationNumber())
               .addParam("personType", entity.getPersonType());
    }

    @Nullable
    @Override
    public Registration update(@NonNull @Nonnull Registration entity) {
        String jdbcTemplateUpdate = "update carinfo.record set admin_obj_id = :adminObjId," +
                " op_code = :operationCode, dep_code = :departmentCode, kind_id = :kindId, vehicle_id = :vehicleId," +
                " color_id = :colorId, body_type_id = :bodyTypeId, purpose_id = :purposeId, fuel_type_id = :fuelTypeId," +
                " own_weight = :ownWeight, total_weight = :totalWeight, engine_capacity = :engineCapacity, make_year = :makeYear," +
                " registration_date = :registrationDate, registration_number = :registrationNumber, person_type = :personType " +
                "where id = :id;";
        SqlParameterMap builder = getSqlParamBuilder();
        setParamsToBuilder(entity, builder);
        SqlParameterSource parameterSource = builder.addParam(ID_PARAM, entity.getId()).build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        return findOne(entity.getId());
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.record where id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(id) from carinfo.record where id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "registrationCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull Registration entity) {
        String select = "select count(id) ";
        String from = "from carinfo.record_view  ";
        WhereBuilder.BuildResult buildResult = buildWhere()
                .addFieldParam(ADMIN_OBJ_NAME_PARAM, ADMIN_OBJ_NAME, entity.getAdminObjName())
                .addFieldParam(ADMIN_OBJ_TYPE_PARAM, ADMIN_OBJ_TYPE, entity.getAdminObjType())
                .addFieldParam(OP_CODE_PARAM, OPERATION_CODE, entity.getOperationCode())
                .addFieldParam(OP_NAME_PARAM, OPERATION_NAME, entity.getOperationName())
                .addFieldParam(DEP_CODE_PARAM, DEPARTMENT_CODE, entity.getDepartmentCode())
                .addFieldParam(DEP_ADDR_PARAM, DEPARTMENT_ADDRESS, entity.getDepartmentAddress())
                .addFieldParam(DEP_EMAIL_PARAM, DEPARTMENT_EMAIL, entity.getDepartmentEmail())
                .addFieldParam(KIND_NAME_PARAM, KIND, entity.getKindName())
                .addFieldParam(COLOR_NAME_PARAM, COLOR, entity.getColorName())
                .addFieldParam(BODY_TYPE_NAME_PARAM, BODY_TYPE, entity.getBodyTypeName())
                .addFieldParam(PURPOSE_NAME_PARAM, PURPOSE, entity.getPurposeName())
                .addFieldParam(BRAND_NAME_PARAM, BRAND, entity.getBrandName())
                .addFieldParam(MODEL_NAME_PARAM, MODEL, entity.getModelName())
                .addFieldParam(FUEL_TYPE_NAME_PARAM, FUEL_TYPE, entity.getFuelTypeName())
                .addFieldParam(ENGINE_CAPACITY_PARAM, ENGINE_CAPACITY, entity.getEngineCapacity())
                .addFieldParam(MAKE_YEAR_PARAM, MAKE_YEAR, entity.getMakeYear())
                .addFieldParam(OWN_WEIGHT_PARAM, OWN_WEIGHT, entity.getOwnWeight())
                .addFieldParam(TOTAL_WEIGHT_PARAM, TOTAL_WEIGHT, entity.getTotalWeight())
                .addFieldParam(PERSON_TYPE_PARAM, PERSON_TYPE, entity.getPersonType())
                .addFieldParam(REGISTRATION_NUMBER_PARAM, REGISTRATION_NUMBER, entity.getRegistrationNumber())
                .addFieldParam(REGISTRATION_DATE_PARAM, REGISTRATION_DATE, entity.getRegistrationDate())
                .build();
        String where = buildResult.getWhereSql();
        String selectSqlQuery = String.format("%s %s %s", select, from, where);
        return exist(selectSqlQuery, buildResult.getSqlParameters());
    }

    @Nullable
    @Override
    public Registration findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.record_view where id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam(ID_PARAM, id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "registration", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public Registration findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String jdbcTemplateSelect = "select * from carinfo.record_view ";
        WhereBuilder.BuildResult buildResult = buildWhereForFind(searchParams).build();
        String where = buildResult.getWhereSql();
        return findOne(jdbcTemplateSelect + where, buildResult.getSqlParameters());
    }

    @Override
    public List<Registration> find() {
        String jdbcTemplateSelect = "select * from carinfo.record_view ";
        return find(jdbcTemplateSelect);
    }

    @Cacheable(cacheNames = "recordIndex", unless = "#result == false ", key = "#indexField")
    @Override
    public boolean existsByIndex(@Nonnull @NonNull String indexField) {
        String select = "select count(id) ";
        String from = "from carinfo.record_view  ";
        WhereBuilder.BuildResult buildResult = buildWhere()
                .addFieldParam(REGISTRATION_NUMBER_PARAM, REGISTRATION_NUMBER, indexField)
                .build();
        String where = buildResult.getWhereSql();
        String selectSqlQuery = String.format("%s %s %s", select, from, where);
        return exist(selectSqlQuery, buildResult.getSqlParameters());
    }

    private WhereBuilder buildWhereForFind(@Nonnull @NonNull ParamsHolder searchParams) {
        Long opCode = searchParams.getLong(OPERATION_CODE);
        String opName = searchParams.getString(Registration.OPERATION_NAME);
        Long depCode = searchParams.getLong(Registration.DEPARTMENT_CODE);
        String kind = searchParams.getString(Registration.KIND);
        String color = searchParams.getString(Registration.COLOR);
        String purpose = searchParams.getString(Registration.PURPOSE);
        String brand = searchParams.getString(Registration.BRAND);
        String model = searchParams.getString(Registration.MODEL);
        Long makeYear = searchParams.getLong(Registration.MAKE_YEAR);
        String personType = searchParams.getString(Registration.PERSON_TYPE);
        Date regDate = searchParams.getDate(Registration.REGISTRATION_DATE);
        String admObjName = searchParams.getString(ADMIN_OBJ_NAME);
        String admObjType = searchParams.getString(ADMIN_OBJ_TYPE);
        String depAddress = searchParams.getString(Registration.DEPARTMENT_ADDRESS);
        String depEmail = searchParams.getString(Registration.DEPARTMENT_EMAIL);
        String bodyType = searchParams.getString(Registration.BODY_TYPE);
        String fuelType = searchParams.getString(Registration.FUEL_TYPE);
        Long engineCapacity = searchParams.getLong(Registration.ENGINE_CAPACITY);
        Long ownWeight = searchParams.getLong(Registration.OWN_WEIGHT);
        Long totalWeight = searchParams.getLong(Registration.TOTAL_WEIGHT);
        String regNumber = searchParams.getString(Registration.REGISTRATION_NUMBER);
        return buildWhere()
                .addFieldParam(ADMIN_OBJ_NAME_PARAM, ADMIN_OBJ_NAME, admObjName)
                .addFieldParam(ADMIN_OBJ_TYPE_PARAM, ADMIN_OBJ_TYPE, admObjType)
                .addFieldParam(OP_CODE_PARAM, OPERATION_CODE, opCode)
                .addFieldParam(OP_NAME_PARAM, OPERATION_NAME, opName)
                .addFieldParam(DEP_CODE_PARAM, DEPARTMENT_CODE, depCode)
                .addFieldParam(DEP_ADDR_PARAM, DEPARTMENT_ADDRESS, depAddress)
                .addFieldParam(DEP_EMAIL_PARAM, DEPARTMENT_EMAIL, depEmail)
                .addFieldParam(KIND_NAME_PARAM, KIND, kind)
                .addFieldParam(COLOR_NAME_PARAM, COLOR, color)
                .addFieldParam(BODY_TYPE_NAME_PARAM, BODY_TYPE, bodyType)
                .addFieldParam(PURPOSE_NAME_PARAM, PURPOSE, purpose)
                .addFieldParam(BRAND_NAME_PARAM, BRAND, brand)
                .addFieldParam(MODEL_NAME_PARAM, MODEL, model)
                .addFieldParam(FUEL_TYPE_NAME_PARAM, FUEL_TYPE, fuelType)
                .addFieldParam(ENGINE_CAPACITY_PARAM, ENGINE_CAPACITY, engineCapacity)
                .addFieldParam(MAKE_YEAR_PARAM, MAKE_YEAR, makeYear)
                .addFieldParam(OWN_WEIGHT_PARAM, OWN_WEIGHT, ownWeight)
                .addFieldParam(TOTAL_WEIGHT_PARAM, TOTAL_WEIGHT, totalWeight)
                .addFieldParam(PERSON_TYPE_PARAM, PERSON_TYPE, personType)
                .addFieldParam(REGISTRATION_NUMBER_PARAM, REGISTRATION_NUMBER, regNumber)
                .addFieldParam(REGISTRATION_DATE_PARAM, REGISTRATION_DATE, regDate);
    }

    @Override
    public Page<Registration> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.record_view  ";
        WhereBuilder buildWhere = buildWhereForFind(searchParams);
        return findPage(searchParams, select, from, buildWhere);
    }

    @Override
    RowMapper<Registration> getRowMapper() {
        return ROW_MAPPER;
    }

    @Override
    WhereBuilder.BuildResult getWhereFromParams(ParamsHolder params) {
        return buildWhereForFind(params).build();
    }

    @Override
    String getTableName() {
        return Constants.RegistrationRecord.TABLE_VIEW;
    }
}