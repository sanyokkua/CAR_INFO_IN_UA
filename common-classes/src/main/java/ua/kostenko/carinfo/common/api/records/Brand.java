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
public class Brand implements Serializable, GenericRecord<String> {
    public static final String BRAND_NAME = "brandName";
    private Long brandId;
    private String brandName;

    @Override
    public Long getId() {
        return brandId;
    }

    @Override
    public String getIndexField() {
        return getBrandName();
    }
}
