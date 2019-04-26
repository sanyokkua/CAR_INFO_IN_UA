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
class RegistrationRecordRepository extends CommonDBRepository<Registration> {
    private static final RowMapper<Registration> ROW_MAPPER = (resultSet, i) -> Registration.builder()
                                                                                            .adminObjName(resultSet.getString(Constants.AdminObject.NAME))
                                                                                            .adminObjType(resultSet.getString(Constants.AdminObject.TYPE))
                                                                                            .operationCode(resultSet.getLong(Constants.RegistrationOperation.CODE))
                                                                                            .operationName(resultSet.getString(Constants.RegistrationOperation.NAME))
                                                                                            .departmentCode(resultSet.getLong(Constants.RegistrationDepartment.CODE))
                                                                                            .departmentAddress(resultSet.getString(Constants.RegistrationDepartment.ADDRESS))
                                                                                            .departmentEmail(resultSet.getString(Constants.RegistrationDepartment.EMAIL))
                                                                                            .kind(resultSet.getString(Constants.RegistrationKind.NAME))
                                                                                            .color(resultSet.getString(Constants.RegistrationColor.NAME))
                                                                                            .bodyType(resultSet.getString(Constants.RegistrationBodyType.NAME))
                                                                                            .purpose(resultSet.getString(Constants.RegistrationPurpose.NAME))
                                                                                            .brand(resultSet.getString(Constants.RegistrationBrand.NAME))
                                                                                            .model(resultSet.getString(Constants.RegistrationModel.NAME))
                                                                                            .fuelType(resultSet.getString(Constants.RegistrationFuelType.NAME))
                                                                                            .engineCapacity(resultSet.getLong(Constants.RegistrationRecord.ENGINE_CAPACITY))
                                                                                            .makeYear(resultSet.getLong(Constants.RegistrationRecord.MAKE_YEAR))
                                                                                            .ownWeight(resultSet.getLong(Constants.RegistrationRecord.OWN_WEIGHT))
                                                                                            .totalWeight(resultSet.getLong(Constants.RegistrationRecord.TOTAL_WEIGHT))
                                                                                            .personType(resultSet.getString(Constants.RegistrationRecord.PERSON_TYPE))
                                                                                            .registrationNumber(resultSet.getString(Constants.RegistrationRecord.REGISTRATION_NUMBER))
                                                                                            .registrationDate(resultSet.getDate(Constants.RegistrationRecord.REGISTRATION_DATE))
                                                                                            .id(resultSet.getLong(Constants.RegistrationRecord.ID))
                                                                                            .build();
    private final DBRepository<AdministrativeObject> administrativeObjectDBRepository;
    private final DBRepository<BodyType> bodyTypeDBRepository;
    private final DBRepository<Color> colorDBRepository;
    private final DBRepository<Department> departmentDBRepository;
    private final DBRepository<FuelType> fuelTypeDBRepository;
    private final DBRepository<Kind> kindDBRepository;
    private final DBRepository<Operation> operationDBRepository;
    private final DBRepository<Purpose> purposeDBRepository;
    private final DBRepository<Vehicle> vehicleDBRepository;

    @Autowired
    public RegistrationRecordRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate,
                                        @NonNull @Nonnull DBRepository<AdministrativeObject> administrativeObjectDBRepository,
                                        @NonNull @Nonnull DBRepository<BodyType> bodyTypeDBRepository,
                                        @NonNull @Nonnull DBRepository<Color> colorDBRepository,
                                        @NonNull @Nonnull DBRepository<Department> departmentDBRepository,
                                        @NonNull @Nonnull DBRepository<FuelType> fuelTypeDBRepository,
                                        @NonNull @Nonnull DBRepository<Kind> kindDBRepository,
                                        @NonNull @Nonnull DBRepository<Operation> operationDBRepository,
                                        @NonNull @Nonnull DBRepository<Purpose> purposeDBRepository,
                                        @NonNull @Nonnull DBRepository<Vehicle> vehicleDBRepository) {
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
    public synchronized Registration create(@NonNull @Nonnull Registration entity) {
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
        BodyType bodyType = bodyTypeDBRepository.findOne(getParamsHolderBuilder().param(BodyType.BODY_TYPE_NAME, entity.getBodyType()).build());
        Color color = colorDBRepository.findOne(getParamsHolderBuilder().param(Color.COLOR_NAME, entity.getColor()).build());
        Department department = departmentDBRepository.findOne(getParamsHolderBuilder().param(Department.DEPARTMENT_CODE, entity.getDepartmentCode()).build());
        FuelType fuelType = fuelTypeDBRepository.findOne(getParamsHolderBuilder().param(FuelType.FUEL_NAME, entity.getFuelType()).build());
        Kind kind = kindDBRepository.findOne(getParamsHolderBuilder().param(Kind.KIND_NAME, entity.getKind()).build());
        Operation operation = operationDBRepository.findOne(getParamsHolderBuilder().param(Operation.OPERATION_CODE, entity.getOperationCode()).build());
        Purpose purpose = purposeDBRepository.findOne(getParamsHolderBuilder().param(Purpose.PURPOSE_NAME, entity.getPurpose()).build());
        Vehicle vehicle = vehicleDBRepository.findOne(getParamsHolderBuilder().param(Vehicle.BRAND_NAME, entity.getBrand()).param(Vehicle.MODEL_NAME, entity.getModel()).build());
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
    public synchronized Registration update(@NonNull @Nonnull Registration entity) {
        String jdbcTemplateUpdate = "update carinfo.record set admin_obj_id = :adminObjId," +
                " op_code = :operationCode, dep_code = :departmentCode, kind_id = :kindId, vehicle_id = :vehicleId," +
                " color_id = :colorId, body_type_id = :bodyTypeId, purpose_id = :purposeId, fuel_type_id = :fuelTypeId," +
                " own_weight = :ownWeight, total_weight = :totalWeight, engine_capacity = :engineCapacity, make_year = :makeYear," +
                " registration_date = :registrationDate, registration_number = :registrationNumber, person_type = :personType " +
                "where id = :id;";
        SqlParameterMap builder = getSqlParamBuilder();
        setParamsToBuilder(entity, builder);
        SqlParameterSource parameterSource = builder.addParam("id", entity.getId()).build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        return findOne(entity.getId());
    }

    @Override
    public synchronized boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.record where id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public synchronized boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(id) from carinfo.record where id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Override
    public synchronized boolean exist(@NonNull @Nonnull Registration entity) {
        String select = "select count(id) ";
        String from = "from carinfo.record_view  ";
        WhereBuilder.BuildResult buildResult = buildWhere()
                .addFieldParam("admin_obj_name", ADMIN_OBJ_NAME, entity.getAdminObjName())
                .addFieldParam("admin_obj_type", ADMIN_OBJ_TYPE, entity.getAdminObjType())
                .addFieldParam("op_code", OPERATION_CODE, entity.getOperationCode())
                .addFieldParam("op_name", OPERATION_NAME, entity.getOperationName())
                .addFieldParam("dep_code", DEPARTMENT_CODE, entity.getDepartmentCode())
                .addFieldParam("dep_addr", DEPARTMENT_ADDRESS, entity.getDepartmentAddress())
                .addFieldParam("dep_email", DEPARTMENT_EMAIL, entity.getDepartmentEmail())
                .addFieldParam("kind_name", KIND, entity.getKind())
                .addFieldParam("color_name", COLOR, entity.getColor())
                .addFieldParam("body_type_name", BODY_TYPE, entity.getBodyType())
                .addFieldParam("purpose_name", PURPOSE, entity.getPurpose())
                .addFieldParam("brand_name", BRAND, entity.getBrand())
                .addFieldParam("model_name", MODEL, entity.getModel())
                .addFieldParam("fuel_type_name", FUEL_TYPE, entity.getFuelType())
                .addFieldParam("engine_capacity", ENGINE_CAPACITY, entity.getEngineCapacity())
                .addFieldParam("make_year", MAKE_YEAR, entity.getMakeYear())
                .addFieldParam("own_weight", OWN_WEIGHT, entity.getOwnWeight())
                .addFieldParam("total_weight", TOTAL_WEIGHT, entity.getTotalWeight())
                .addFieldParam("person_type", PERSON_TYPE, entity.getPersonType())
                .addFieldParam("registration_number", REGISTRATION_NUMBER, entity.getRegistrationNumber())
                .addFieldParam("registration_date", REGISTRATION_DATE, entity.getRegistrationDate())
                .build();
        String where = buildResult.getWhereSql();
        String selectSqlQuery = String.format("%s %s %s", select, from, where);
        return exist(selectSqlQuery, buildResult.getSqlParameters());
    }

    @Nullable
    @Override
    public synchronized Registration findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.record_view where id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("id", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "registration", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public synchronized Registration findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String jdbcTemplateSelect = "select * from carinfo.record_view ";
        WhereBuilder.BuildResult buildResult = buildWhereForFind(searchParams).build();
        String where = buildResult.getWhereSql();
        return findOne(jdbcTemplateSelect + where, buildResult.getSqlParameters());
    }

    @Override
    public synchronized List<Registration> find() {
        String jdbcTemplateSelect = "select * from carinfo.record_view ";
        return find(jdbcTemplateSelect);
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
                .addFieldParam("admin_obj_name", ADMIN_OBJ_NAME, admObjName)
                .addFieldParam("admin_obj_type", ADMIN_OBJ_TYPE, admObjType)
                .addFieldParam("op_code", OPERATION_CODE, opCode)
                .addFieldParam("op_name", OPERATION_NAME, opName)
                .addFieldParam("dep_code", DEPARTMENT_CODE, depCode)
                .addFieldParam("dep_addr", DEPARTMENT_ADDRESS, depAddress)
                .addFieldParam("dep_email", DEPARTMENT_EMAIL, depEmail)
                .addFieldParam("kind_name", KIND, kind)
                .addFieldParam("color_name", COLOR, color)
                .addFieldParam("body_type_name", BODY_TYPE, bodyType)
                .addFieldParam("purpose_name", PURPOSE, purpose)
                .addFieldParam("brand_name", BRAND, brand)
                .addFieldParam("model_name", MODEL, model)
                .addFieldParam("fuel_type_name", FUEL_TYPE, fuelType)
                .addFieldParam("engine_capacity", ENGINE_CAPACITY, engineCapacity)
                .addFieldParam("make_year", MAKE_YEAR, makeYear)
                .addFieldParam("own_weight", OWN_WEIGHT, ownWeight)
                .addFieldParam("total_weight", TOTAL_WEIGHT, totalWeight)
                .addFieldParam("person_type", PERSON_TYPE, personType)
                .addFieldParam("registration_number", REGISTRATION_NUMBER, regNumber)
                .addFieldParam("registration_date", REGISTRATION_DATE, regDate);
    }

    @Override
    public synchronized Page<Registration> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.record_view  ";
        WhereBuilder buildWhere = buildWhereForFind(searchParams);
        return findPage(searchParams, select, from, buildWhere);
    }

    @Override
    RowMapper<Registration> getRowMapper() {
        return ROW_MAPPER;
    }
}