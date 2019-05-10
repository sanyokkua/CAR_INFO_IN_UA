package ua.kostenko.carinfo.rest.resources.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegistrationResource extends ResourceSupport {
    private String adminObjName;//NULLABLE
    private String adminObjType;//NULLABLE
    private Long operationCode;//non NULLABLE opName
    private String operationName;//non NULLABLE opName
    private Long departmentCode;//non NULLABLE
    private String departmentAddress;//non NULLABLE
    private String departmentEmail;//non NULLABLE
    private String kindName;//non NULLABLE
    private String colorName;//non NULLABLE
    private String bodyTypeName;//NULLABLE
    private String purposeName;//non NULLABLE
    private String brandName;//non NULLABLE
    private String modelName;//non NULLABLE
    private String fuelTypeName;//NULLABLE
    private Long engineCapacity;//NULLABLE
    private Long makeYear;//non NULLABLE
    private Long ownWeight;//NULLABLE
    private Long totalWeight;//NULLABLE
    private String personType;//non NULLABLE
    private String registrationNumber;//NULLABLE
    private Date registrationDate;//non NULLABLE
}