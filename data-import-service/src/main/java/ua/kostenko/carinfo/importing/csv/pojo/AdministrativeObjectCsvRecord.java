package ua.kostenko.carinfo.importing.csv.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministrativeObjectCsvRecord {

    private Long id;
    private String type;
    private String name;
}
