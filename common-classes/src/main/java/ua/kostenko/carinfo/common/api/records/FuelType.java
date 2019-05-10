package ua.kostenko.carinfo.common.api.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelType implements Serializable, GenericRecord<String> {
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
