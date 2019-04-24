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

@Repository
@Slf4j
class RegistrationRecordRepository extends CommonDBRepository<Registration> { //TODO: fix problems with join tables due the search
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

        String select = "select count(r.id) ";
        String from = "from carinfo.record r ";
        WhereBuilder.BuildResult buildResult = buildWhere()
                .addFieldParam("engine_capacity", "EngineCapacity", entity.getEngineCapacity())
                .addFieldParam("make_year", "MakeYear", entity.getMakeYear())
                .addFieldParam("own_weight", "OwnWeight", entity.getOwnWeight())
                .addFieldParam("person_type", "PersonType", entity.getPersonType())
                .addFieldParam("registration_date", "RegistrationDate", entity.getRegistrationDate())
                .addFieldParam("registration_number", "RegistrationNumber", entity.getRegistrationNumber())
                .addFieldParam("total_weight", "TotalWeight", entity.getTotalWeight())
                .addFieldParam("admin_obj_id", "adminObjId", adminObjId)
                .addFieldParam("body_type_id", "bodyTypeId", bodyTypeId)
                .addFieldParam("color_id", "colorId", colorId)
                .addFieldParam("dep_code", "departmentCode", departmentCode)
                .addFieldParam("fuel_type_id", "fuelTypeId", fuelTypeId)
                .addFieldParam("kind_id", "kindId", kindId)
                .addFieldParam("op_code", "operationCode", operationCode)
                .addFieldParam("purpose_id", "purposeId", purposeId)
                .addFieldParam("vehicle_id", "vehicleId", vehicleId)
                .build();
        String where = buildResult.getWhereSql();
        String selectSqlQuery = String.format("%s %s %s", select, from, where);
        return exist(selectSqlQuery, buildResult.getSqlParameters());
    }

    @Nullable
    @Override
    public synchronized Registration findOne(long id) {
        String jdbcTemplateSelect = "select * " +
                "from carinfo.record r, carinfo.admin_object ao, carinfo.operation o, carinfo.department d, carinfo.kind k, carinfo.vehicle v, carinfo.color c, carinfo.body_type bt," +
                " carinfo.purpose p, carinfo.fuel_type ft, carinfo.brand b, carinfo.model m " +
                "where ao.admin_obj_id =  r.admin_obj_id and o.op_code = r.op_code and d.dep_code = r.dep_code and k.kind_id = r.kind_id and v.vehicle_id = r.vehicle_id " +
                "and c.color_id = r.color_id and bt.body_type_id = r.body_type_id and p.purpose_id = r.purpose_id and ft.fuel_type_id = r.fuel_type_id and b.brand_id = v.brand_id " +
                "and m.model_id = v.model_id " +
                "and r.id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("id", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "registration", unless = "#result != null")
    @Nullable
    @Override
    public synchronized Registration findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String jdbcTemplateSelect = "select * " +
                "from carinfo.record r, carinfo.admin_object ao, carinfo.operation o, carinfo.department d, carinfo.kind k, carinfo.vehicle v, carinfo.color c, carinfo.body_type bt," +
                " carinfo.purpose p, carinfo.fuel_type ft, carinfo.brand b, carinfo.model m ";
        WhereBuilder.BuildResult buildResult = buildWhereForFind(searchParams).build();
        String where = buildResult.getWhereSql();
        return findOne(jdbcTemplateSelect + where, buildResult.getSqlParameters());
    }

    @Override
    public synchronized List<Registration> find() {
        String jdbcTemplateSelect = "select * " +
                "from carinfo.record r, carinfo.admin_object ao, carinfo.operation o, carinfo.department d, carinfo.kind k, carinfo.vehicle v, carinfo.color c, carinfo.body_type bt," +
                " carinfo.purpose p, carinfo.fuel_type ft, carinfo.brand b, carinfo.model m " +
                "where ao.admin_obj_id =  r.admin_obj_id and o.op_code = r.op_code and d.dep_code = r.dep_code and k.kind_id = r.kind_id and v.vehicle_id = r.vehicle_id " +
                "and c.color_id = r.color_id and bt.body_type_id = r.body_type_id and p.purpose_id = r.purpose_id and ft.fuel_type_id = r.fuel_type_id and b.brand_id = v.brand_id " +
                "and m.model_id = v.model_id;";
        return find(jdbcTemplateSelect);
    }

    private WhereBuilder buildWhereForFind(@Nonnull @NonNull ParamsHolder searchParams) {
        Long opCode = searchParams.getLong(Registration.OPERATION_CODE);
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
        String admObjName = searchParams.getString(Registration.ADMIN_OBJ_NAME);
        String admObjType = searchParams.getString(Registration.ADMIN_OBJ_TYPE);
        String depAddress = searchParams.getString(Registration.DEPARTMENT_ADDRESS);
        String depEmail = searchParams.getString(Registration.DEPARTMENT_EMAIL);
        String bodyType = searchParams.getString(Registration.BODY_TYPE);
        String fuelType = searchParams.getString(Registration.FUEL_TYPE);
        Long engineCapacity = searchParams.getLong(Registration.ENGINE_CAPACITY);
        Long ownWeight = searchParams.getLong(Registration.OWN_WEIGHT);
        Long totalWeight = searchParams.getLong(Registration.TOTAL_WEIGHT);
        String regNumber = searchParams.getString(Registration.REGISTRATION_NUMBER);
        return buildWhere()
                .addEqualFields("ao.admin_obj_id", "r.admin_obj_id")
                .addEqualFields("o.op_code", "r.op_code")
                .addEqualFields("d.dep_code", "r.dep_code")
                .addEqualFields("k.kind_id", "r.kind_id")
                .addEqualFields("v.vehicle_id", "r.vehicle_id")
                .addEqualFields("c.color_id", "r.color_id")
                .addEqualFields("bt.body_type_id", "r.body_type_id")
                .addEqualFields("p.purpose_id", "r.purpose_id")
                .addEqualFields("ft.fuel_type_id", "r.fuel_type_id")
                .addEqualFields("b.brand_id", "v.brand_id")
                .addEqualFields("m.model_id", "v.model_id")
                .addFieldParam("o.op_code", "opCode", opCode)
                .addFieldParam("o.op_name", "opName", opName)
                .addFieldParam("d.dep_code", "depCode", depCode)
                .addFieldParam("c.color_name", "color", color)
                .addFieldParam("p.purpose_name", "purpose", purpose)
                .addFieldParam("k.kind_name", "kind", kind)
                .addFieldParam("b.brand_name", "brand", brand)
                .addFieldParam("m.model_name", "model", model)
                .addFieldParam("r.make_year", "makeYear", makeYear)
                .addFieldParam("r.person_type", "personType", personType)
                .addFieldParam("r.registration_date", "regDate", regDate)
                .addFieldParam("ao.admin_obj_name", "admObjName", admObjName)
                .addFieldParam("ao.admin_obj_type", "admObjType", admObjType)
                .addFieldParam("d.dep_addr", "depAddress", depAddress)
                .addFieldParam("d.dep_email", "depEmail", depEmail)
                .addFieldParam("bt.body_type_name", "bodyType", bodyType)
                .addFieldParam("ft.fuel_type_name", "fuelType", fuelType)
                .addFieldParam("r.engine_capacity", "engineCapacity", engineCapacity)
                .addFieldParam("r.own_weight", "ownWeight", ownWeight)
                .addFieldParam("r.total_weight", "totalWeight", totalWeight)
                .addFieldParam("r.registration_number", "regNumber", regNumber);
    }

    @Override
    public synchronized Page<Registration> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.record r, carinfo.admin_object ao, carinfo.operation o, carinfo.department d," +
                " carinfo.kind k, carinfo.vehicle v, carinfo.color c, carinfo.body_type bt," +
                " carinfo.purpose p, carinfo.fuel_type ft, carinfo.brand b, carinfo.model m ";
        WhereBuilder buildWhere = buildWhereForFind(searchParams);
        return findPage(searchParams, select, from, buildWhere);
    }

    @Override
    RowMapper<Registration> getRowMapper() {
        return ROW_MAPPER;
    }
}