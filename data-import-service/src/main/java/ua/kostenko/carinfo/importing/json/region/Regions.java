package ua.kostenko.carinfo.importing.json.region;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Regions {

    private Map<String, String> regions;
}
