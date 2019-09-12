package ua.kostenko.carinfo.rest.resources.resources;

import org.springframework.hateoas.ResourceSupport;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
