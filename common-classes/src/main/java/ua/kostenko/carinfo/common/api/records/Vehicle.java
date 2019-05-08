package ua.kostenko.carinfo.common.api.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle implements Serializable, GenericRecord<String> {
    public static final String BRAND_NAME = "brand_name";
    public static final String MODEL_NAME = "model_name";
    private Long vehicleId;
    @NonNull
    private String brandName;
    @NonNull
    private String modelName;

    @JsonIgnore
    @Override
    public Long getId() {
        return vehicleId;
    }

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getBrandName();
    }
}