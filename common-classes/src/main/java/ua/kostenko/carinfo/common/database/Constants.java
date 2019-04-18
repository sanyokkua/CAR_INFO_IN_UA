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
        String TABLE = "body_type";
        String ID = "body_type_id";
        String NAME = "body_type_name";
    }

    interface RegistrationBrand {
        String TABLE = "brand";
        String ID = "brand_id";
        String NAME = "brand_name";
    }

    interface RegistrationColor {
        String TABLE = "color";
        String ID = "color_id";
        String NAME = "color_name";
    }

    interface RegistrationDepartment {
        String TABLE = "department";
        String CODE = "dep_code";
        String NAME = "dep_name";
        String ADDRESS = "dep_addr";
        String EMAIL = "dep_email";
    }

    interface RegistrationFuelType {
        String TABLE = "fuel_type";
        String ID = "fuel_type_id";
        String NAME = "fuel_type_name";
    }

    interface RegistrationKind {
        String TABLE = "kind";
        String ID = "kind_id";
        String NAME = "kind_name";
    }

    interface RegistrationModel {
        String TABLE = "model";
        String ID = "model_id";
        String NAME = "model_name";
    }

    interface RegistrationOperation {
        String TABLE = "operation";
        String CODE = "op_code";
        String NAME = "op_name";
    }

    interface RegistrationPurpose {
        String TABLE = "purpose";
        String ID = "purpose_id";
        String NAME = "purpose_name";
    }

    interface RegistrationRecord {
        String TABLE = "record";
        String ID = "id";
        String ADMIN_OBJ_ID = "admin_obj_id";
        String OPERATION_CODE = "op_code";
        String DEPARTMENT_CODE = "dep_code";
        String COLOR_ID = "color_id";
        String BODY_TYPE_ID = "body_type_id";
        String PURPOSE_ID = "purpose_id";
        String FUEL_TYPE_ID = "fuel_type_id";
        String ENGINE_CAPACITY = "engine_capacity";
        String OWN_WEIGHT = "own_weight";
        String TOTAL_WEIGHT = "total_weight";
        String REGISTRATION_NUMBER = "registration_number";
        String REGISTRATION_DATE = "registration_date";
        String VEHICLE_ID = "vehicle_id";
        String KIND = "kind_id";
        String MAKE_YEAR = "make_year";
        String PERSON_TYPE = "person_type";
    }

    interface RegistrationVehicle {
        String TABLE = "vehicle";
        String ID = "vehicle_id";
        String BRAND_ID = "brand_id";
        String MODEL_ID = "model_id";
    }
}
