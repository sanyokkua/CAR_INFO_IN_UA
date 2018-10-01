package ua.kostenko.carinfo.carinfoua.data;

import lombok.Data;

@Data
public class InfoData {
  private String person; // e.g. P // P (person) or J (juridical)
  private String reg_addr_koatuu; // e.g. 1210436900
  private String operationCode; // e.g. 440
  private String operationName; // e.g. 440 - ПЕРЕРЕЄСТРАЦIЯ ПРИ ВТРАТІ СВIДОЦТВА ПРО РЕЄСТРАЦIЮ
  private String registrationDate; // date of operation  yyy-mm-dd 2018-01-01
  private String departmentCode; // number 1244
  private String departmentName; // TSC - number ТСЦ 1244
  private String carBrand; // e.g. Mitsubishi
  private String carModel; // e.g. Lancer X
  private String carMakeYear; // date of creation current car 2007
  private String carColor; // e.g. БІЛИЙ
  private String carKind; // e.g. ЛЕГКОВИЙ
  private String carBody; // e.g. СЕДАН-B
  private String carPurpose; // e.g. ЗАГАЛЬНИЙ
  private String carFuel; // e.g. БЕНЗИН
  private String carEngineCapacity; // e.g. 1198
  private String carOwnWeight; // e.g. 955
  private String carTotalWeight; // e.g. 1355
  private String carNewRegistrationNumber; // AX3333EX
}
