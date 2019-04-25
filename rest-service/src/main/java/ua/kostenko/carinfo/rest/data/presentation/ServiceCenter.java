package ua.kostenko.carinfo.rest.data.presentation;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.common.api.records.Registration;

import javax.annotation.Nonnull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class ServiceCenter {
    long numberOfServiceCenter;
    String address;
    String email;
    String name;

    public static ServiceCenter map(@NonNull @Nonnull Registration registration) {
        return ServiceCenter.builder()
                            .numberOfServiceCenter(registration.getDepartmentCode())
                            .address(registration.getDepartmentAddress())
                            .email(registration.getDepartmentEmail())
                            .name(registration.getDepartmentAddress())
                            .build();
    }
}
