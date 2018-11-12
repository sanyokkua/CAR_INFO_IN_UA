package ua.kostenko.carinfo.common;

public interface TablesAndFieldsConstants {

    interface RegistrationInformationTable {
        String TABLE = "registration_information";
        String FIELD_ID = "id";
        String FIELD_ADMIN_OBJECT_CODE = "administrative_object_code";
        String FIELD_CAR_BODY = "car_body";
        String FIELD_CAR_BRAND = "car_brand";
        String FIELD_CAR_COLOR = "car_color";
        String FIELD_CAR_ENGINE_CAPACITY = "car_engine_capacity";
        String FIELD_CAR_FUEL = "car_fuel";
        String FIELD_CAR_KIND = "car_kind";
        String FIELD_CAR_MAKE_YEAR = "car_make_year";
        String FIELD_CAR_MODEL = "car_model";
        String FIELD_CAR_NEW_REG_NUMBER = "car_new_registration_number";
        String FIELD_CAR_OWN_WEIGHT = "car_own_weight";
        String FIELD_CAR_PURPOSE = "car_purpose";
        String FIELD_CAR_TOTAL_WEIGHT = "car_total_weight";
        String FIELD_DATA_SET_LABEL = "data_set_label";
        String FIELD_DATA_SET_YEAR = "data_set_year";
        String FIELD_DEPARTMENT_CODE = "department_code";
        String FIELD_DEPARTMENT_NAME = "department_name";
        String FIELD_OPERATION_CODE = "operation_code";
        String FIELD_OPERATION_NAME = "operation_name";
        String FIELD_PERSON = "person";
        String FIELD_REGISTRATION_DATE = "registration_date";
    }

    interface AdministrativeObjectTable {
        String TABLE = "administrative_object";
        String FIELD_ID = "id";
        String FIELD_NAME = "name";
        String FIELD_TYPE_NAME = "type_name";
    }

    interface ServiceCenterTable {
        String TABLE = "service_center";
        String FIELD_DEPARTMENT_CODE = "dep_code";
        String FIELD_ADDRESS = "address";
        String FIELD_EMAIL = "email";
    }

    interface RegionCodeTable {
        String TABLE = "region_code";
        String FIELD_CODE = "code";
        String FIELD_REGION = "region";
    }
}
