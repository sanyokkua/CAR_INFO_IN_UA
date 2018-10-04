package ua.kostenko.carinfo.carinfoua.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:additional.properties")
public class ApplicationProperties {
  @Value("${app.data.set.json.url}")
  public String APP_DATA_SET_JSON_URL;
  @Value("${app.archive.name}")
  public String APP_ARCHIVE_NAME;
  @Value("${app.archive.dir}")
  public String APP_ARCHIVE_DIR;
  @Value("${app.koatuu.file.path}")
  public String APP_KOATUU_FILE_PATH;
  @Value("${app.data.service.center.url}")
  public String APP_DATA_SERVICE_CENTER_URL;
}
