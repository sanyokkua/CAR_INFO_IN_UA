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
public class Purpose implements Serializable {
    public static final String PURPOSE_NAME = "purposeName";
    private Long purposeId;
    private String purposeName;
}
