package ua.kostenko.carinfo.common.api.records;

import java.io.Serializable;
import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registration implements Serializable, GenericRecord<String> {

    private static final long serialVersionUID = -6398041379070093599L;
    public static final String ADMIN_OBJ_NAME = "adminObjName";
    public static final String ADMIN_OBJ_TYPE = "adminObjType";
    public static final String OPERATION_CODE = "operationCode";
    public static final String OPERATION_NAME = "operationName";
    public static final String DEPARTMENT_CODE = "departmentCode";
    public static final String DEPARTMENT_ADDRESS = "departmentAddress";
    public static final String DEPARTMENT_EMAIL = "departmentEmail";
    public static final String KIND = "kindName";
    public static final String COLOR = "colorName";
    public static final String BODY_TYPE = "bodyTypeName";
    public static final String PURPOSE = "purposeName";
    public static final String BRAND = "brandName";
    public static final String MODEL = "modelName";
    public static final String FUEL_TYPE = "fuelTypeName";
    public static final String ENGINE_CAPACITY = "engineCapacity";
    public static final String MAKE_YEAR = "makeYear";
    public static final String OWN_WEIGHT = "ownWeight";
    public static final String TOTAL_WEIGHT = "totalWeight";
    public static final String PERSON_TYPE = "personType";
    public static final String REGISTRATION_NUMBER = "registrationNumber";
    public static final String REGISTRATION_DATE = "registrationDate";

    private Long id;

    private String adminObjName;// NULLABLE
    private String adminObjType;// NULLABLE
    @NonNull
    private Long operationCode;// non NULLABLE opName
    @NonNull
    private String operationName;// non NULLABLE opName
    @NonNull
    private Long departmentCode;// non NULLABLE
    private String departmentAddress;// non NULLABLE
    private String departmentEmail;// non NULLABLE
    @NonNull
    private String kindName;// non NULLABLE
    @NonNull
    private String colorName;// non NULLABLE
    private String bodyTypeName;// NULLABLE
    @NonNull
    private String purposeName;// non NULLABLE
    @NonNull
    private String brandName;// non NULLABLE
    @NonNull
    private String modelName;// non NULLABLE
    private String fuelTypeName;// NULLABLE
    private Long engineCapacity;// NULLABLE
    @NonNull
    private Long makeYear;// non NULLABLE
    private Long ownWeight;// NULLABLE
    private Long totalWeight;// NULLABLE
    @NonNull
    private String personType;// non NULLABLE
    private String registrationNumber;// NULLABLE
    @NonNull
    private Date registrationDate;// non NULLABLE

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getRegistrationNumber();
    }
}
