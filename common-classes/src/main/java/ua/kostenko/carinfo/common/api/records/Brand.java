package ua.kostenko.carinfo.common.api.records;

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
public class Brand implements Serializable, GenericRecord<String> {
    public static final String BRAND_NAME = "brandName";
    private Long brandId;
    private String brandName;

    @JsonIgnore
    @Override
    public Long getId() {
        return brandId;
    }

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getBrandName();
    }
}
