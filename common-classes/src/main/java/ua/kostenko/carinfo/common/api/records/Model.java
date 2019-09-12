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
public class Model implements Serializable, GenericRecord<String> {

    private static final long serialVersionUID = -7044559719550832711L;
    public static final String MODEL_NAME = "modelName";
    private Long modelId;
    @Builder.Default
    private String modelName = "—";

    public String getModelName() {
        return StringUtils.isBlank(modelName) ? "—" : modelName;
    }

    @JsonIgnore
    @Override
    public Long getId() {
        return modelId;
    }

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getModelName();
    }
}
