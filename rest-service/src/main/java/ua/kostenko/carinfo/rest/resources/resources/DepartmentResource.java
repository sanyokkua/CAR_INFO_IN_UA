package ua.kostenko.carinfo.rest.resources.resources;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DepartmentResource extends ResourceSupport {
    private Long departmentCode;
    private String departmentAddress;
    private String departmentEmail;
}
