package ua.kostenko.carinfo.common.api.records;

import java.io.Serializable;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelType implements Serializable, GenericRecord<String> {

    private static final long serialVersionUID = -4529235748619272860L;
    public static final String FUEL_NAME = "fuelTypeName";
    private Long fuelTypeId;
    @Builder.Default
    private String fuelTypeName = "—";

    @JsonIgnore
    @Override
    public Long getId() {
        return fuelTypeId;
    }

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getFuelTypeName();
    }

    public String getFuelTypeName() {
        return StringUtils.isBlank(fuelTypeName) ? "—" : fuelTypeName;
    }
}
