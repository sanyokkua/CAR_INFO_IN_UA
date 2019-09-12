package ua.kostenko.carinfo.importing.csv.structure.headers.registration;

import ua.kostenko.carinfo.importing.csv.structure.headers.CsvHeaders;

public interface RegistrationHeaders extends CsvHeaders {

    String getPersonType();// PERSON = J

    String getAdministrativeObject();// REG_ADDR_KOATUU = 3210600000

    String getOperationCode();// OPER_CODE = 315

    String getOperationName();// OPER_NAME = Перереєстрація ТЗ на нов. власн. по договору укладеному в ТСЦ

    String getRegistrationDate();// D_REG = 08.02.2019

    String getDepartmentCode();// DEP_CODE = 12293

    String getDepartmentName();// DEP = ТСЦ 8044

    String getVehicleBrand();// BRAND = VOLKSWAGEN

    String getVehicleModel();// MODEL = JETTA

    String getVehicleMakeYear();// MAKE_YEAR = 2008

    String getVehicleColor();// COLOR = СІРИЙ

    String getVehicleKind();// KIND = ЛЕГКОВИЙ

    String getVehicleBodyType();// BODY = СЕДАН-B

    String getVehiclePurpose();// PURPOSE = ЗАГАЛЬНИЙ

    String getVehicleFuelType();// FUEL = БЕНЗИН

    String getVehicleEngineCapacity();// CAPACITY = 1595

    String getVehicleOwnWeight();// OWN_WEIGHT = 1275

    String getVehicleTotalWeight();// TOTAL_WEIGHT = 1680

    String getVehicleRegistrationNumber();// N_REG_NEW = АІ0843НР
}
