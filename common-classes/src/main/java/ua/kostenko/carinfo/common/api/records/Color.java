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
public class Color implements Serializable, GenericRecord<String> {
    public static final String COLOR_NAME = "colorName";
    private Long colorId;
    private String colorName;

    @Override
    public Long getId() {
        return colorId;
    }

    @Override
    public String getIndexField() {
        return colorName;
    }
}
