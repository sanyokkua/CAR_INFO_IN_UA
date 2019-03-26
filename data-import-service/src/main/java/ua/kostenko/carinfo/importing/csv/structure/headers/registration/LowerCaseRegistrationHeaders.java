package ua.kostenko.carinfo.importing.csv.structure.headers.registration;

import ua.kostenko.carinfo.importing.csv.structure.enums.RegistrationCsvRecord;

public class LowerCaseRegistrationHeaders implements RegistrationHeaders {
    @Override
    public String getPersonType() {
        return RegistrationCsvRecord.PERSON.getLowerCase();
    }

    @Override
    public String getAdministrativeObject() {
        return RegistrationCsvRecord.REG_ADDR_KOATUU.getLowerCase();
    }

    @Override
    public String getOperationCode() {
        return RegistrationCsvRecord.OPER_CODE.getLowerCase();
    }

    @Override
    public String getOperationName() {
        return RegistrationCsvRecord.OPER_NAME.getLowerCase();
    }

    @Override
    public String getRegistrationDate() {
        return RegistrationCsvRecord.D_REG.getLowerCase();
    }

    @Override
    public String getDepartmentCode() {
        return RegistrationCsvRecord.DEP_CODE.getLowerCase();
    }

    @Override
    public String getDepartmentName() {
        return RegistrationCsvRecord.DEP.getLowerCase();
    }

    @Override
    public String getVehicleBrand() {
        return RegistrationCsvRecord.BRAND.getLowerCase();
    }

    @Override
    public String getVehicleModel() {
        return RegistrationCsvRecord.MODEL.getLowerCase();
    }

    @Override
    public String getVehicleMakeYear() {
        return RegistrationCsvRecord.MAKE_YEAR.getLowerCase();
    }

    @Override
    public String getVehicleColor() {
        return RegistrationCsvRecord.COLOR.getLowerCase();
    }

    @Override
    public String getVehicleKind() {
        return RegistrationCsvRecord.KIND.getLowerCase();
    }

    @Override
    public String getVehicleBodyType() {
        return RegistrationCsvRecord.BODY.getLowerCase();
    }

    @Override
    public String getVehiclePurpose() {
        return RegistrationCsvRecord.PURPOSE.getLowerCase();
    }

    @Override
    public String getVehicleFuelType() {
        return RegistrationCsvRecord.FUEL.getLowerCase();
    }

    @Override
    public String getVehicleEngineCapacity() {
        return RegistrationCsvRecord.CAPACITY.getLowerCase();
    }

    @Override
    public String getVehicleOwnWeight() {
        return RegistrationCsvRecord.OWN_WEIGHT.getLowerCase();
    }

    @Override
    public String getVehicleTotalWeight() {
        return RegistrationCsvRecord.TOTAL_WEIGHT.getLowerCase();
    }

    @Override
    public String getVehicleRegistrationNumber() {
        return RegistrationCsvRecord.N_REG_NEW.getLowerCase();
    }

    @Override
    public String getHeaderField() {
        return RegistrationCsvRecord.PERSON.getLowerCase();
    }
}
