package ua.kostenko.carinfo.common.api.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdministrativeObject implements Serializable, GenericRecord<String> {
    public static final String ADMIN_OBJ_TYPE = "adminObjType";
    public static final String ADMIN_OBJ_NAME = "adminObjName";
    private Long adminObjId;
    private String adminObjType;
    private String adminObjName;

    @JsonIgnore
    @Override
    public Long getId() {
        return adminObjId;
    }

    @JsonIgnore
    @Override
    public String getIndexField() {
        return getAdminObjName();
    }
}
