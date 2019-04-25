package ua.kostenko.carinfo.rest.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.kostenko.carinfo.common.api.records.Registration;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
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

    public static Auto map(Registration registration) {
        return Auto.builder()
                   .carBrand(registration.getBrand())
                   .carModel(registration.getModel())
                   .carMakeYear(registration.getMakeYear())
                   .carColor(registration.getColor())
                   .carKind(registration.getKind())
                   .carBody(registration.getBodyType())
                   .carPurpose(registration.getPurpose())
                   .carFuel(registration.getFuelType())
                   .carEngineCapacity(registration.getEngineCapacity())
                   .carOwnWeight(registration.getOwnWeight())
                   .carTotalWeight(registration.getTotalWeight())
                   .build();
    }
}
