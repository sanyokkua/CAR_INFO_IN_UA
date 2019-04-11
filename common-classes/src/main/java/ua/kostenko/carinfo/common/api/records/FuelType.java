package ua.kostenko.carinfo.common.api.records;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuelType implements Serializable {
    public static final String FUEL_NAME = "fuelName";
    private Long fuelTypeId;
    private String fuelTypeName;
}
