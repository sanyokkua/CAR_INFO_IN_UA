package ua.kostenko.carinfo.rest.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCenter {
    long numberOfServiceCenter;
    String address;
    String email;
    private String name;
}
