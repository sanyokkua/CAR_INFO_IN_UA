package ua.kostenko.carinfo.importing.csv.pojo;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.common.api.records.Color;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.common.api.records.Purpose;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCsvRecord {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("[yyyy-MM-dd][dd.MM.yyyy]");// 2013-02-02 or 19.02.2019
    private String personType;
    private Long administrativeObject;
    private Long operationCode;
    private String operationName;
    private String registrationDate;
    private Long departmentCode;
    private String departmentName;
    private String vehicleBrand;
    private String vehicleModel;
    private Long vehicleMakeYear;
    private String vehicleColor;
    private String vehicleKind;
    private String vehicleBodyType;
    private String vehiclePurpose;
    private String vehicleFuelType;
    private Long vehicleEngineCapacity;
    private Long vehicleOwnWeight;
    private Long vehicleTotalWeight;
    private String vehicleRegistrationNumber;

    public Operation getOperation() {
        Long code = this.getOperationCode();
        String name = this.getOperationName();
        return Operation.builder().operationCode(code).operationName(name).build();
    }

    public Brand getBrand() {
        String brand = StringUtils.trim(this.getVehicleBrand());
        String model = StringUtils.trim(getModel().getModelName());
        if (StringUtils.isNotBlank(brand)
                && StringUtils.isNotBlank(model)
                && !(brand.equalsIgnoreCase(model))
                && brand.contains(model)) {
            brand = StringUtils.remove(brand, model);
            brand = StringUtils.trim(brand);
        }
        return Brand.builder().brandName(brand).build();
    }

    public Model getModel() {
        String model = StringUtils.trim(this.getVehicleModel());
        return Model.builder().modelName(model).build();
    }

    public Color getColor() {
        String color = this.getVehicleColor();
        return Color.builder().colorName(color).build();
    }

    public Kind getKind() {
        String kind = this.getVehicleKind();
        return Kind.builder().kindName(kind).build();
    }

    public BodyType getBodyType() {
        String bodyType = this.getVehicleBodyType();
        return BodyType.builder().bodyTypeName(bodyType).build();
    }

    public Purpose getPurpose() {
        String purpose = this.getVehiclePurpose();
        return Purpose.builder().purposeName(purpose).build();
    }

    public FuelType getFuelType() {
        String fuelType = this.getVehicleFuelType();
        return FuelType.builder().fuelTypeName(Objects.nonNull(fuelType) ? fuelType : "—").build();
    }

    public AdministrativeObject getAdminObject() {
        Long administrativeObjectId = this.getAdministrativeObject();
        return AdministrativeObject.builder().adminObjId(administrativeObjectId).build();
    }

    public Department getDepartment() {
        Long depCode = this.getDepartmentCode();
        return Department.builder().departmentCode(depCode).build();
    }

    public Date getDate() {
        String regDate = this.getRegistrationDate();
        Date resultDate = null;
        try {
            LocalDate parse = LocalDate.parse(regDate, DATE_TIME_FORMATTER);
            resultDate = Date.valueOf(parse);
        } catch (Exception ex) {
            log.warn("Problem with parsing date with formatter: {}", regDate);
        }
        return resultDate;
    }
}
