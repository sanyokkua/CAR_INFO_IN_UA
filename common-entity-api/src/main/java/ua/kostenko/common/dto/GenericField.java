package ua.kostenko.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericField<T> {

    private Long id;
    private String fieldName;
    private T value;
    private FieldType fieldType;
}
