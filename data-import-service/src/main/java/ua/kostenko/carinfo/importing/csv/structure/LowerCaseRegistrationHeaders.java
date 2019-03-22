package ua.kostenko.carinfo.importing.csv.structure;

public class LowerCaseRegistrationHeaders implements RegistrationHeaders {
    @Override
    public String getPERSON() {
        return RegistrationCsvRecord.PERSON.getLowerCase();
    }

    @Override
    public String getREG_ADDR_KOATUU() {
        return RegistrationCsvRecord.REG_ADDR_KOATUU.getLowerCase();
    }

    @Override
    public String getOPER_CODE() {
        return RegistrationCsvRecord.OPER_CODE.getLowerCase();
    }

    @Override
    public String getOPER_NAME() {
        return RegistrationCsvRecord.OPER_NAME.getLowerCase();
    }

    @Override
    public String getD_REG() {
        return RegistrationCsvRecord.D_REG.getLowerCase();
    }

    @Override
    public String getDEP_CODE() {
        return RegistrationCsvRecord.DEP_CODE.getLowerCase();
    }

    @Override
    public String getDEP() {
        return RegistrationCsvRecord.DEP.getLowerCase();
    }

    @Override
    public String getBRAND() {
        return RegistrationCsvRecord.BRAND.getLowerCase();
    }

    @Override
    public String getMODEL() {
        return RegistrationCsvRecord.MODEL.getLowerCase();
    }

    @Override
    public String getMAKE_YEAR() {
        return RegistrationCsvRecord.MAKE_YEAR.getLowerCase();
    }

    @Override
    public String getCOLOR() {
        return RegistrationCsvRecord.COLOR.getLowerCase();
    }

    @Override
    public String getKIND() {
        return RegistrationCsvRecord.KIND.getLowerCase();
    }

    @Override
    public String getBODY() {
        return RegistrationCsvRecord.BODY.getLowerCase();
    }

    @Override
    public String getPURPOSE() {
        return RegistrationCsvRecord.PURPOSE.getLowerCase();
    }

    @Override
    public String getFUEL() {
        return RegistrationCsvRecord.FUEL.getLowerCase();
    }

    @Override
    public String getCAPACITY() {
        return RegistrationCsvRecord.CAPACITY.getLowerCase();
    }

    @Override
    public String getOWN_WEIGHT() {
        return RegistrationCsvRecord.OWN_WEIGHT.getLowerCase();
    }

    @Override
    public String getTOTAL_WEIGHT() {
        return RegistrationCsvRecord.TOTAL_WEIGHT.getLowerCase();
    }

    @Override
    public String getN_REG_NEW() {
        return RegistrationCsvRecord.N_REG_NEW.getLowerCase();
    }

    @Override
    public String getHeaderField() {
        return RegistrationCsvRecord.PERSON.getLowerCase();
    }
}
