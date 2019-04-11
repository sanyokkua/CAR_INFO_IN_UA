package ua.kostenko.carinfo.common.api.records;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle implements Serializable {
    public static final String BRAND_NAME = "brand_name";
    public static final String MODEL_NAME = "model_name";
    private Long vehicleId;
    @NonNull
    private String brandName;
    @NonNull
    private String modelName;

}
