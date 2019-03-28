package ua.kostenko.carinfo.common.database;

public interface Constants {
    String SCHEMA = "carinfo";

    interface AdminObject {
        String TABLE = "admin_object";
        String ID = "admin_obj_id";
        String TYPE = "admin_obj_type";
        String NAME = "admin_obj_name";
    }

    interface RegistrationBodyType {
        String TABLE = "reg_body_type";
        String ID = "reg_body_type_id";
        String NAME = "reg_body_type_name";
    }

    interface RegistrationBrand {
        String TABLE = "reg_brand";
        String ID = "reg_brand_id";
        String NAME = "reg_brand_name";
    }

    interface RegistrationColor {
        String TABLE = "reg_color";
        String ID = "reg_color_id";
        String NAME = "reg_color_name";
    }

    interface RegistrationDepartment {
        String TABLE = "reg_dep";
        String CODE = "reg_dep_code";
        String NAME = "reg_dep_name";
        String ADDRESS = "reg_dep_addr";
        String EMAIL = "reg_dep_email";
    }

    interface RegistrationFuelType {
        String TABLE = "reg_fuel_type";
        String ID = "reg_fuel_type_id";
        String NAME = "reg_fuel_type_name";
    }

    interface RegistrationKind {
        String TABLE = "reg_kind";
        String ID = "reg_kind_id";
        String NAME = "reg_kind_name";
    }

    interface RegistrationModel {
        String TABLE = "reg_model";
        String ID = "reg_model_id";
        String NAME = "reg_model_name";
    }

    interface RegistrationOperation {
        String TABLE = "reg_op";
        String CODE = "reg_op_code";
        String NAME = "reg_op_name";
    }

    interface RegistrationPurpose {
        String TABLE = "reg_purpose";
        String ID = "reg_purpose_id";
        String NAME = "reg_purpose_name";
    }

    interface RegistrationRecord {
        String TABLE = "reg_record";
        String ID = "reg_id";
        String ADMIN_OBJ_ID = "admin_obj_id";
        String OPERATION_CODE = "reg_op_code";
        String DEPARTMENT_CODE = "reg_dep_code";
        String BRAND_ID = "reg_brand_id";
        String MODEL_ID = "reg_model_id";
        String COLOR_ID = "reg_color_id";
        String BODY_TYPE_ID = "reg_body_type_id";
        String PURPOSE_ID = "reg_purpose_id";
        String FUEL_TYPE_ID = "reg_fuel_type_id";
        String ENGINE_CAPACITY = "reg_engine_capacity";
        String OWN_WEIGHT = "reg_own_weight";
        String TOTAL_WEIGHT = "reg_total_weight";
        String REGISTRATION_NUMBER = "reg_registration_number";
        String REGISTRATION_DATE = "reg_registration_date";
        String VEHICLE_ID = "reg_vehicle_id";
        String MAKE_YEAR = "reg_make_year";
    }

    interface RegistrationVehicle {
        String TABLE = "reg_vehicle";
        String ID = "reg_vehicle_id";
        String BRAND_ID = "reg_brand_id";
        String MODEL_ID = "reg_model_id";
    }
}
