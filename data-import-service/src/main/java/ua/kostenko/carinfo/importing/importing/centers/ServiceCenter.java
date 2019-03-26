package ua.kostenko.carinfo.importing.importing.centers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCenter {
    public Long depId;
    public String address;
    public String email;
}
