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
public class Purpose implements Serializable, GenericRecord<String> {
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
