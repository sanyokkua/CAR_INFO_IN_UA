package ua.kostenko.carinfo.carinfoua.data.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auto {
    private String carBrand; // e.g. Mitsubishi
    private String carModel; // e.g. Lancer X
    private Long carMakeYear; // date of creation current car 2007
    private String carColor; // e.g. БІЛИЙ
    private String carKind; // e.g. ЛЕГКОВИЙ
    private String carBody; // e.g. СЕДАН-B
    private String carPurpose; // e.g. ЗАГАЛЬНИЙ
    private String carFuel; // e.g. БЕНЗИН
    private Long carEngineCapacity; // e.g. 1198
    private Long carOwnWeight; // e.g. 955
    private Long carTotalWeight; // e.g. 1355
}
