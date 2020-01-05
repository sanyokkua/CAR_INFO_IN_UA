package ua.kostenko.carinfo.dto.flat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDto {

    private Long code;
    private String name;
}