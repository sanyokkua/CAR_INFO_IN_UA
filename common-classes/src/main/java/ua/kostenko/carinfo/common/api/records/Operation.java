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
public class Operation implements Serializable, GenericRecord<Long> {
    public static final String OPERATION_CODE = "operationCode";
    private Long operationCode;
    private String operationName;

    @Override
    public Long getId() {
        return operationCode;
    }

    @Override
    public Long getIndexField() {
        return operationCode;
    }
}
