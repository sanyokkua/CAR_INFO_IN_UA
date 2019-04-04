package ua.kostenko.carinfo.common.database.raw;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationColor implements Serializable {
    private Long colorId;
    private String colorName;
}
