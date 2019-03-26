package ua.kostenko.carinfo.importing.json.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDataPackage {
    private String mimetype;
    private String profile;
    private String name;
    private String format;
    private String encoding;
    private String path;
}
