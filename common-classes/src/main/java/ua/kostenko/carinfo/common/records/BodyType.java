package ua.kostenko.carinfo.common.records;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodyType implements Serializable {
    private Long bodyTypeId;
    private String bodyTypeName;
}
