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
public class Purpose implements Serializable, GenericRecord<String> {

    private static final long serialVersionUID = -323515011694399541L;
    public static final String PURPOSE_NAME = "purposeName";
    private Long purposeId;
    private String purposeName;

    @JsonIgnore
    @Override
    public Long getId() {
        return purposeId;
    }

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getPurposeName();
    }
}
