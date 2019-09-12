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
public class Brand implements Serializable, GenericRecord<String> {

    private static final long serialVersionUID = -2282999642583149777L;
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
