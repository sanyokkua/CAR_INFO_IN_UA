package ua.kostenko.carinfo.common.api.records;

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
    public static final String FUEL_NAME = "fuelName";
    private Long fuelTypeId;
    @Builder.Default
    private String fuelTypeName = "—";

    public String getFuelTypeName() {
        return StringUtils.isBlank(fuelTypeName) ? "—" : fuelTypeName;
    }

    @Override
    public Long getId() {
        return fuelTypeId;
    }

    @Override
    public String getIndexField() {
        return getFuelTypeName();
    }
}
