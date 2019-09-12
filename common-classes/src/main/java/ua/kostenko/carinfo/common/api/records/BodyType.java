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
public class BodyType implements Serializable, GenericRecord<String> {

    private static final long serialVersionUID = 9020656440913295095L;
    public static final String BODY_TYPE_NAME = "bodyTypeName";
    private Long bodyTypeId;
    private String bodyTypeName;

    @JsonIgnore
    @Override
    public Long getId() {
        return bodyTypeId;
    }

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getBodyTypeName();
    }
}
