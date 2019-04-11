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
public class Model implements Serializable {
    public static final String MODEL_NAME = "modelName";
    private Long modelId;
    private String modelName;
}
