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
public class VehicleResource extends ResourceSupport {

    private String brandName;
    private String modelName;

}
