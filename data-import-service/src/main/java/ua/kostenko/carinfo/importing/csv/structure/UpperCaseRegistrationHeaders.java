package ua.kostenko.carinfo.importing.csv.structure;

public class UpperCaseRegistrationHeaders implements RegistrationHeaders {
    @Override
    public String getPERSON() {
        return RegistrationCsvRecord.PERSON.getUpperCase();
    }

    @Override
    public String getREG_ADDR_KOATUU() {
        return RegistrationCsvRecord.REG_ADDR_KOATUU.getUpperCase();
    }

    @Override
    public String getOPER_CODE() {
        return RegistrationCsvRecord.OPER_CODE.getUpperCase();
    }

    @Override
    public String getOPER_NAME() {
        return RegistrationCsvRecord.OPER_NAME.getUpperCase();
    }

    @Override
    public String getD_REG() {
        return RegistrationCsvRecord.D_REG.getUpperCase();
    }

    @Override
    public String getDEP_CODE() {
        return RegistrationCsvRecord.DEP_CODE.getUpperCase();
    }

    @Override
    public String getDEP() {
        return RegistrationCsvRecord.DEP.getUpperCase();
    }

    @Override
    public String getBRAND() {
        return RegistrationCsvRecord.BRAND.getUpperCase();
    }

    @Override
    public String getMODEL() {
        return RegistrationCsvRecord.MODEL.getUpperCase();
    }

    @Override
    public String getMAKE_YEAR() {
        return RegistrationCsvRecord.MAKE_YEAR.getUpperCase();
    }

    @Override
    public String getCOLOR() {
        return RegistrationCsvRecord.COLOR.getUpperCase();
    }

    @Override
    public String getKIND() {
        return RegistrationCsvRecord.KIND.getUpperCase();
    }

    @Override
    public String getBODY() {
        return RegistrationCsvRecord.BODY.getUpperCase();
    }

    @Override
    public String getPURPOSE() {
        return RegistrationCsvRecord.PURPOSE.getUpperCase();
    }

    @Override
    public String getFUEL() {
        return RegistrationCsvRecord.FUEL.getUpperCase();
    }

    @Override
    public String getCAPACITY() {
        return RegistrationCsvRecord.CAPACITY.getUpperCase();
    }

    @Override
    public String getOWN_WEIGHT() {
        return RegistrationCsvRecord.OWN_WEIGHT.getUpperCase();
    }

    @Override
    public String getTOTAL_WEIGHT() {
        return RegistrationCsvRecord.TOTAL_WEIGHT.getUpperCase();
    }

    @Override
    public String getN_REG_NEW() {
        return RegistrationCsvRecord.N_REG_NEW.getUpperCase();
    }

    @Override
    public String getHeaderField() {
        return RegistrationCsvRecord.PERSON.getUpperCase();
    }
}
