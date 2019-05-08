package ua.kostenko.carinfo.rest.controllers.rest.common;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public final class Param {
    private String key;
    private String value;
}
