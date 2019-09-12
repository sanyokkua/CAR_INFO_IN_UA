package ua.kostenko.carinfo.importing.csv.structure.headers.registration;

import ua.kostenko.carinfo.importing.csv.structure.enums.RegistrationCsvRecord;

public class UpperCaseRegistrationHeaders implements RegistrationHeaders {

    @Override
    public String getPersonType() {
        return RegistrationCsvRecord.PERSON.getUpperCase();
    }

    @Override
    public String getAdministrativeObject() {
        return RegistrationCsvRecord.REG_ADDR_KOATUU.getUpperCase();
    }

    @Override
    public String getOperationCode() {
        return RegistrationCsvRecord.OPER_CODE.getUpperCase();
    }

    @Override
    public String getOperationName() {
        return RegistrationCsvRecord.OPER_NAME.getUpperCase();
    }

    @Override
    public String getRegistrationDate() {
        return RegistrationCsvRecord.D_REG.getUpperCase();
    }

    @Override
    public String getDepartmentCode() {
        return RegistrationCsvRecord.DEP_CODE.getUpperCase();
    }

    @Override
    public String getDepartmentName() {
        return RegistrationCsvRecord.DEP.getUpperCase();
    }

    @Override
    public String getVehicleBrand() {
        return RegistrationCsvRecord.BRAND.getUpperCase();
    }

    @Override
    public String getVehicleModel() {
        return RegistrationCsvRecord.MODEL.getUpperCase();
    }

    @Override
    public String getVehicleMakeYear() {
        return RegistrationCsvRecord.MAKE_YEAR.getUpperCase();
    }

    @Override
    public String getVehicleColor() {
        return RegistrationCsvRecord.COLOR.getUpperCase();
    }

    @Override
    public String getVehicleKind() {
        return RegistrationCsvRecord.KIND.getUpperCase();
    }

    @Override
    public String getVehicleBodyType() {
        return RegistrationCsvRecord.BODY.getUpperCase();
    }

    @Override
    public String getVehiclePurpose() {
        return RegistrationCsvRecord.PURPOSE.getUpperCase();
    }

    @Override
    public String getVehicleFuelType() {
        return RegistrationCsvRecord.FUEL.getUpperCase();
    }

    @Override
    public String getVehicleEngineCapacity() {
        return RegistrationCsvRecord.CAPACITY.getUpperCase();
    }

    @Override
    public String getVehicleOwnWeight() {
        return RegistrationCsvRecord.OWN_WEIGHT.getUpperCase();
    }

    @Override
    public String getVehicleTotalWeight() {
        return RegistrationCsvRecord.TOTAL_WEIGHT.getUpperCase();
    }

    @Override
    public String getVehicleRegistrationNumber() {
        return RegistrationCsvRecord.N_REG_NEW.getUpperCase();
    }

    @Override
    public String getHeaderField() {
        return RegistrationCsvRecord.PERSON.getUpperCase();
    }
}
