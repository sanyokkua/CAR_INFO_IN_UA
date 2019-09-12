package ua.kostenko.carinfo.common.api.records;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle implements Serializable, GenericRecord<String> {

    private static final long serialVersionUID = -5966526607340993948L;
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
