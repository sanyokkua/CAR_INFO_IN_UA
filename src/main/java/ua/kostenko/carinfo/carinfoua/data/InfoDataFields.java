package ua.kostenko.carinfo.carinfoua.data;

public enum InfoDataFields {
  PERSON("person"),
  REG_ADDR_KOATUU("reg_addr_koatuu"),
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

  private String fieldName;

  InfoDataFields(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldName() {
    return fieldName;
  }
}
