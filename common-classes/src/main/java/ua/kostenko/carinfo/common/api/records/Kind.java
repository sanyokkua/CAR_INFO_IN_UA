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
public class Kind implements Serializable, GenericRecord<String> {

    private static final long serialVersionUID = 383726643041346919L;
    public static final String KIND_NAME = "kindName";
    private Long kindId;
    private String kindName;

    @JsonIgnore
    @Override
    public Long getId() {
        return kindId;
    }

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getKindName();
    }
}
