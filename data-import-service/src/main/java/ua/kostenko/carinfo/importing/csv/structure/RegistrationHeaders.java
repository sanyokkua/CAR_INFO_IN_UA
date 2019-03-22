package ua.kostenko.carinfo.importing.csv.structure;

public interface RegistrationHeaders extends CsvHeaders {
    String getPERSON();
    String getREG_ADDR_KOATUU();
    String getOPER_CODE();
    String getOPER_NAME();
    String getD_REG();
    String getDEP_CODE();
    String getDEP();
    String getBRAND();
    String getMODEL();
    String getMAKE_YEAR();
    String getCOLOR();
    String getKIND();
    String getBODY();
    String getPURPOSE();
    String getFUEL();
    String getCAPACITY();
    String getOWN_WEIGHT();
    String getTOTAL_WEIGHT();
    String getN_REG_NEW();
}
