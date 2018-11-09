package ua.kostenko.carinfo.rest.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auto {
    private String carBrand;
    private String carModel;
    private Long carMakeYear;
    private String carColor;
    private String carKind;
    private String carBody;
    private String carPurpose;
    private String carFuel;
    private Long carEngineCapacity;
    private Long carOwnWeight;
    private Long carTotalWeight;
}
