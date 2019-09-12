package ua.kostenko.carinfo.common.api.records;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color implements Serializable, GenericRecord<String> {

    private static final long serialVersionUID = 9213776721179650849L;
    public static final String COLOR_NAME = "colorName";
    private Long colorId;
    private String colorName;

    @JsonIgnore
    @Override
    public Long getId() {
        return colorId;
    }

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getColorName();
    }
}
