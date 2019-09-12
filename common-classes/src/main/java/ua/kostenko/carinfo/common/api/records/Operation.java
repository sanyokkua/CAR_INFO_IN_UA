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
public class Operation implements Serializable, GenericRecord<Long> {

    private static final long serialVersionUID = -8038533703772819798L;
    public static final String OPERATION_CODE = "operationCode";
    public static final String OPERATION_NAME = "operationName";
    private Long operationCode;
    private String operationName;

    @JsonIgnore
    @Override
    public Long getId() {
        return operationCode;
    }

    @JsonIgnore
    @Override
    public Long getIndexField() {
        return getOperationCode();
    }
}
