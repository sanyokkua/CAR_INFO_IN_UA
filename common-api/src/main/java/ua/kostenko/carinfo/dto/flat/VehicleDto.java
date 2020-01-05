package ua.kostenko.carinfo.dto.flat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleDto {

    private String brand;
    private String model;
    private Integer makeYear;
    private String color;
    private String kind;
    private String body;
    private String fuel;
    private Integer capacity;
    private Integer ownWeight;
    private Integer totalWeight;
}