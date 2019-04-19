package ua.kostenko.carinfo.importing.csv.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.kostenko.carinfo.common.api.records.*;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationCsvRecord {
    private static final SimpleDateFormat FORMAT_FIRST = new SimpleDateFormat("yyyy-MM-dd");//2013-02-02
    private static final SimpleDateFormat FORMAT_SECOND = new SimpleDateFormat("dd-MM-yyyy");//19.02.2019

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

    public Model getModel() {
        String vehicleModel = StringUtils.trim(this.getVehicleModel());
        return Model.builder().modelName(vehicleModel).build();
    }

    public Brand getBrand() {
        String vehicleModel = StringUtils.trim(this.getVehicleModel());
        String vehicleBrand = StringUtils.trim(this.getVehicleBrand());
        if (vehicleBrand.contains(vehicleModel)) {
            vehicleBrand = StringUtils.remove(vehicleBrand, vehicleModel);
            vehicleBrand = StringUtils.trim(vehicleBrand);
        }
        return Brand.builder().brandName(vehicleBrand).build();
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
        return FuelType.builder().fuelTypeName(vehicleFuelType).build();
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
            java.util.Date date = FORMAT_FIRST.parse(registrationDate);
            resultDate = new Date(date.getTime());
        } catch (Exception ex) {
            log.warn("Problem with parsing date with first formatter: {}", registrationDate);
            try {
                java.util.Date date = FORMAT_SECOND.parse(registrationDate);
                resultDate = new Date(date.getTime());
            } catch (Exception e) {
                log.warn("Problem with parsing date with second formatter: {}", registrationDate);
            }
        }
        return resultDate;
    }
}