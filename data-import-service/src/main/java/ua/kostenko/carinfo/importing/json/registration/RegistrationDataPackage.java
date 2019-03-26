package ua.kostenko.carinfo.importing.json.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDataPackage {
    private List<ResourceDataPackage> resources;
}
