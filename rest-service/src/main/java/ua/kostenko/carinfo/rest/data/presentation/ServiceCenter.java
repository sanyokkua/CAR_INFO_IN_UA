package ua.kostenko.carinfo.rest.data.presentation;

import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.records.Registration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
