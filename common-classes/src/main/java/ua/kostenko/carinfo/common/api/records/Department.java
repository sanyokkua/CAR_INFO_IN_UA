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
public class Department implements Serializable, GenericRecord<Long> {

    private static final long serialVersionUID = 52413530239464409L;
    public static final String DEPARTMENT_CODE = "departmentCode";
    public static final String DEPARTMENT_ADDRESS = "departmentAddress";
    public static final String DEPARTMENT_EMAIL = "departmentEmail";
    private Long departmentCode;
    private String departmentAddress;
    private String departmentEmail;

    @JsonIgnore
    @Override
    public Long getId() {
        return departmentCode;
    }

    @JsonIgnore
    @Override
    public Long getIndexField() {
        return getDepartmentCode();
    }
}
