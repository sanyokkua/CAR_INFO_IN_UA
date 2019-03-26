package ua.kostenko.carinfo.common.database;

public interface Constants {
    String SCHEMA = "carinfo";

    interface AdminObject {
        String TABLE = "admin_object";
        String ADMIN_OBJ_ID = "admin_obj_id";
        String ADMIN_OBJ_TYPE = "admin_obj_type";
        String ADMIN_OBJ_NAME = "admin_obj_name";
    }

    interface RegistrationBodyType {
        String TABLE = "reg_body_type";
        String BODY_TYPE_ID = "reg_body_type_id";
        String BODY_TYPE_NAME = "reg_body_type_name";
    }

    interface RegistrationBrand {
        String TABLE = "reg_brand";
        String BRAND_ID = "reg_brand_id";
        String BRAND_NAME = "reg_brand_name";
    }

    interface RegistrationColor {
        String TABLE = "reg_color";
        String COLOR_ID = "reg_color_id";
        String COLOR_NAME = "reg_color_name";
    }

    interface RegistrationDepartment {
        String TABLE = "reg_dep";
        String DEPARTMENT_CODE = "reg_dep_code";
        String DEPARTMENT_NAME = "reg_dep_name";
        String DEPARTMENT_ADDRESS = "reg_dep_addr";
        String DEPARTMENT_EMAIL = "reg_dep_email";
    }

    interface RegistrationFuelType {
        String TABLE = "reg_fuel_type";
        String FUEL_TYPE_ID = "reg_fuel_type_id";
        String FUEL_TYPE_NAME = "reg_fuel_type_name";
    }

    interface RegistrationKind {
        String TABLE = "reg_kind";
        String KIND_ID = "reg_kind_id";
        String KIND_NAME = "reg_kind_name";
    }

    interface RegistrationModel {
        String TABLE = "reg_model";
        String MODEL_ID = "reg_model_id";
        String BRAND_ID = "reg_brand_id";
        String MODEL_NAME = "reg_model_name";
    }

    interface RegistrationOperation {
        String TABLE = "reg_op";
        String OPERATION_CODE = "reg_op_code";
        String OPERATION_NAME = "reg_op_name";
    }

    interface RegistrationPurpose {
        String TABLE = "reg_purpose";
        String PURPOSE_ID = "reg_purpose_id";
        String PURPOSE_NAME = "reg_purpose_name";
    }

    interface RegistrationRecord {
        String TABLE = "reg_record";
        String REGISTRATION_ID = "reg_record_id";
        String ADMIN_OBJECT_ID = "reg_record_adm_obj_id";
        String OPERATION_CODE = "reg_record_op_code";
        String DEPARTMENT_CODE = "reg_record_dep_code";
        String BRAND_ID = "reg_record_brand_id";
        String MODEL_ID = "reg_record_model_id";
        String COLOR_ID = "reg_record_color_id";
        String BODY_TYPE_ID = "reg_record_body_type_id";
        String PURPOSE_ID = "reg_record_purpose_id";
        String FUEL_TYPE_ID = "reg_record_fuel_type_id";
        String ENGINE_CAPACITY = "reg_record_engine_capacity";
        String OWN_WEIGHT = "reg_record_own_weight";
        String TOTAL_WEIGHT = "reg_record_total_weight";
        String REGISTRATION_NUMBER = "reg_record_registration_number";
        String REGISTRATION_DATE = "reg_record_registration_date";
        String VEHICLE_ID = "reg_record_vehicle_id";
        String MAKE_YEAR = "reg_record_make_year";
    }

    interface RegistrationVehicle {
        String TABLE = "reg_vehicle";
        String VEHICLE_ID = "reg_vehicle_id";
        String VEHICLE_BRAND_ID = "reg_vehicle_brand_id";
        String VEHICLE_MODEL_ID = "reg_vehicle_model_id";
    }
}
