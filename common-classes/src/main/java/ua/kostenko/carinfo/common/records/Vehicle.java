package ua.kostenko.carinfo.common.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle implements Serializable {
    private Long vehicleId;
    @JsonIgnore
    private Long brandId;
    @JsonIgnore
    private Long modelId;
    private String brandName;
    private String modelName;

}
