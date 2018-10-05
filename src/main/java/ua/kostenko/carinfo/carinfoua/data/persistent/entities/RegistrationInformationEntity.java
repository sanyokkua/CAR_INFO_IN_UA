package ua.kostenko.carinfo.carinfoua.data.persistent.entities;

import com.google.common.base.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RegistrationInformation")
public class RegistrationInformationEntity { //Number of records 8 653 790
  @Id
  @Column(name = "id", nullable = false)
  private String id;
  @Column(name = "person")
  private String person; // e.g. P // P (person) or J (juridical)
  @Column(name = "reg_addr_koatuu")
  private Long administrativeObjectCode; // e.g. 1210436900
  @Column(name = "oper_code")
  private Long operationCode; // e.g. 440
  @Column(name = "oper_name")
  private String operationName; // e.g. 440 - ПЕРЕРЕЄСТРАЦIЯ ПРИ ВТРАТІ СВIДОЦТВА ПРО РЕЄСТРАЦIЮ
  @Column(name = "d_reg")
  private String registrationDate; // date of operation  yyy-mm-dd 2018-01-01
  @Column(name = "dep_code")
  private Long departmentCode; // number 1244
  @Column(name = "dep")
  private String departmentName; // TSC - number ТСЦ 1244
  @Column(name = "brand")
  private String carBrand; // e.g. Mitsubishi
  @Column(name = "model")
  private String carModel; // e.g. Lancer X
  @Column(name = "make_year")
  private Long carMakeYear; // date of creation current car 2007
  @Column(name = "color")
  private String carColor; // e.g. БІЛИЙ
  @Column(name = "kind")
  private String carKind; // e.g. ЛЕГКОВИЙ
  @Column(name = "body")
  private String carBody; // e.g. СЕДАН-B
  @Column(name = "purpose")
  private String carPurpose; // e.g. ЗАГАЛЬНИЙ
  @Column(name = "fuel")
  private String carFuel; // e.g. БЕНЗИН
  @Column(name = "capacity")
  private Long carEngineCapacity; // e.g. 1198
  @Column(name = "own_weight")
  private Long carOwnWeight; // e.g. 955
  @Column(name = "total_weight")
  private Long carTotalWeight; // e.g. 1355
  @Column(name = "n_reg_new")
  private String carNewRegistrationNumber; // AX3333EX
  @Column(name = "dataSetYear")
  private String dataSetYear;

  public static String createId(RegistrationInformationEntity object) {
    String stringBuilder = object.getPerson() + object.getRegistrationDate() + object.getDepartmentName() + object.getCarBrand() + object.getCarModel() + object.getCarColor() + object.getCarMakeYear()
        + object.getCarBody() + object.getCarNewRegistrationNumber() + object.getCarOwnWeight() + object.getCarTotalWeight() + object.getCarEngineCapacity();
    return DigestUtils.sha512Hex(stringBuilder);
  }

  public enum InfoDataFields {
    PERSON("person"),
    ADMINISTRATIVE_OBJECT("reg_addr_koatuu"),
    OPERATION_CODE("oper_code"),
    OPERATION_NAME("oper_name"),
    REGISTRATION_DATE("d_reg"),
    DEPARTMENT_CODE("dep_code"),
    DEPARTMENT_NAME("dep"),
    CAR_BRAND("brand"),
    CAR_MODEL("model"),
    CAR_MAKE_YEAR("make_year"),
    CAR_COLOR("color"),
    CAR_KIND("kind"),
    CAR_BODY("body"),
    CAR_PURPOSE("purpose"),
    CAR_FUEL("fuel"),
    CAR_ENGINE_CAPACITY("capacity"),
    CAR_OWN_WEIGHT("own_weight"),
    CAR_TOTAL_WEIGHT("total_weight"),
    CAR_NEW_REGISTRATION_NUMBER("n_reg_new"),
    DATA_SET_YEAR("dataSetYear");

    private final String fieldName;

    InfoDataFields(String fieldName) {
      this.fieldName = fieldName;
    }

    public static InfoDataFields getInfoDataFieldByName(String fieldName) {
      Preconditions.checkState(StringUtils.isNotBlank(fieldName));
      return Stream.of(InfoDataFields.values()).filter(infoDataFields -> fieldName.equalsIgnoreCase(infoDataFields.getFieldName())).findFirst().get();
    }

    public String getFieldName() {
      return fieldName;
    }
  }
}
