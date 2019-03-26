package ua.kostenko.carinfo.importing.json.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Regions {
    private Map<String, String> regions;
}
