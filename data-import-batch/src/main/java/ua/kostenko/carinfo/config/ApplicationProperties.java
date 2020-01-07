package ua.kostenko.carinfo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:additional.properties")
public class ApplicationProperties {

    @Value("${app.data.set.json.url}")
    public String appStructureDataPackageJsonUrl;
    @Value("${app.archive.name}")
    public String appArchiveName;
    @Value("${app.archive.dir}")
    public String appArchiveDirectory;
    @Value("${app.administrative.objects.file.path}")
    public String appAdministrativeObjectsFilePath;
    @Value("${app.data.service.center.url}")
    public String appDataServiceCenterUrl;
    @Value("${app.data.service.center.css.selector}")
    public String appDataServiceCenterCssSelector;
    @Value("${app.region.codes.file.path}")
    public String appRegionCodesFilePath;
    @Value("${app.region.codes.absent}")
    public String appRegionCodesAbsent;
}
