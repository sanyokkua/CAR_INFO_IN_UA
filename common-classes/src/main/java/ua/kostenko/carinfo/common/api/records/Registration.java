package ua.kostenko.carinfo.common.api.records;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registration implements Serializable {
    public static final String ADMIN_OBJ_NAME = "adminobjname";
    public static final String ADMIN_OBJ_TYPE = "adminobjtype";
    public static final String OPERATION_CODE = "operationcode";
    public static final String OPERATION_NAME = "operationname";
    public static final String DEPARTMENT_CODE = "departmentcode";
    public static final String DEPARTMENT_ADDRESS = "departmentaddress";
    public static final String DEPARTMENT_EMAIL = "departmentemail";
    public static final String KIND = "kind";
    public static final String COLOR = "color";
    public static final String BODY_TYPE = "bodytype";
    public static final String PURPOSE = "purpose";
    public static final String BRAND = "brand";
    public static final String MODEL = "model";
    public static final String FUEL_TYPE = "fueltype";
    public static final String ENGINE_CAPACITY = "enginecapacity";
    public static final String MAKE_YEAR = "makeyear";
    public static final String OWN_WEIGHT = "ownweight";
    public static final String TOTAL_WEIGHT = "totalweight";
    public static final String PERSON_TYPE = "persontype";
    public static final String REGISTRATION_NUMBER = "registrationnumber";
    public static final String REGISTRATION_DATE = "registrationdate";

    private Long id;

    private String adminObjName;//NULLABLE
    private String adminObjType;//NULLABLE
    @NonNull
    private Long operationCode;//non NULLABLE opName
    @NonNull
    private String operationName;//non NULLABLE opName
    @NonNull
    private Long departmentCode;//non NULLABLE
    private String departmentAddress;//non NULLABLE
    private String departmentEmail;//non NULLABLE
    @NonNull
    private String kind;//non NULLABLE
    @NonNull
    private String color;//non NULLABLE
    private String bodyType;//NULLABLE
    @NonNull
    private String purpose;//non NULLABLE
    @NonNull
    private String brand;//non NULLABLE
    @NonNull
    private String model;//non NULLABLE
    private String fuelType;//NULLABLE
    private Long engineCapacity;//NULLABLE
    @NonNull
    private Long makeYear;//non NULLABLE
    private Long ownWeight;//NULLABLE
    private Long totalWeight;//NULLABLE
    @NonNull
    private String personType;//non NULLABLE
    private String registrationNumber;//NULLABLE
    @NonNull
    private Date registrationDate;//non NULLABLE
}