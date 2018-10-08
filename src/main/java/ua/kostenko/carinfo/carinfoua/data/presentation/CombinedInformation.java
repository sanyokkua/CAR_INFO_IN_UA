package ua.kostenko.carinfo.carinfoua.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CombinedInformation {
    private Auto auto;
    private Registration registration;
    private ServiceCenter serviceCenter;
}
