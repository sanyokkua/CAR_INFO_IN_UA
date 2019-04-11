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
public class AdministrativeObject implements Serializable {
    public static final String ADMIN_OBJ_TYPE = "adminObjType";
    public static final String ADMIN_OBJ_NAME = "adminObjName";
    private Long adminObjId;
    private String adminObjType;
    private String adminObjName;
}
