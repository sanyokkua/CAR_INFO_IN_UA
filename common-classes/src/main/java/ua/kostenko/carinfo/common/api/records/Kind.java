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
public class Kind implements Serializable {
    public static final String KIND_NAME = "kindName";
    private Long kindId;
    private String kindName;
}
