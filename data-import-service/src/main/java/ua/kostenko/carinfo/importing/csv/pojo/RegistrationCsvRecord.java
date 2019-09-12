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
        Long operationCode = this.getOperationCode();
        String operationName = this.getOperationName();
        return Operation.builder().operationCode(operationCode).operationName(operationName).build();
    }

    public Brand getBrand() {
        String vehicleBrand = StringUtils.trim(this.getVehicleBrand());
        String vehicleModel = StringUtils.trim(getModel().getModelName());
        if (StringUtils.isNotBlank(vehicleBrand)
                && StringUtils.isNotBlank(vehicleModel)
                && !(vehicleBrand.equalsIgnoreCase(vehicleModel))
                && vehicleBrand.contains(vehicleModel)) {
            vehicleBrand = StringUtils.remove(vehicleBrand, vehicleModel);
            vehicleBrand = StringUtils.trim(vehicleBrand);
        }
        return Brand.builder().brandName(vehicleBrand).build();
    }

    public Model getModel() {
        String vehicleModel = StringUtils.trim(this.getVehicleModel());
        return Model.builder().modelName(vehicleModel).build();
    }

    public Color getColor() {
        String vehicleColor = this.getVehicleColor();
        return Color.builder().colorName(vehicleColor).build();
    }

    public Kind getKind() {
        String vehicleKind = this.getVehicleKind();
        return Kind.builder().kindName(vehicleKind).build();
    }

    public BodyType getBodyType() {
        String vehicleBodyType = this.getVehicleBodyType();
        return BodyType.builder().bodyTypeName(vehicleBodyType).build();
    }

    public Purpose getPurpose() {
        String vehiclePurpose = this.getVehiclePurpose();
        return Purpose.builder().purposeName(vehiclePurpose).build();
    }

    public FuelType getFuelType() {
        String vehicleFuelType = this.getVehicleFuelType();
        return FuelType.builder().fuelTypeName(Objects.nonNull(vehicleFuelType) ? vehicleFuelType : "—").build();
    }

    public AdministrativeObject getAdminObject() {
        Long administrativeObjectId = this.getAdministrativeObject();
        return AdministrativeObject.builder().adminObjId(administrativeObjectId).build();
    }

    public Department getDepartment() {
        Long departmentCode = this.getDepartmentCode();
        return Department.builder().departmentCode(departmentCode).build();
    }

    public Date getDate() {
        String registrationDate = this.getRegistrationDate();
        Date resultDate = null;
        try {
            LocalDate parse = LocalDate.parse(registrationDate, DATE_TIME_FORMATTER);
            resultDate = Date.valueOf(parse);
        } catch (Exception ex) {
            log.warn("Problem with parsing date with formatter: {}", registrationDate);
        }
        return resultDate;
    }
}
