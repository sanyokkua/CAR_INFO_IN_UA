package ua.kostenko.carinfo.importing.importing.administrative;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.importing.csv.pojo.AdministrativeObjectCsvRecord;
import ua.kostenko.carinfo.importing.importing.Persist;

import javax.annotation.Nonnull;
import java.util.Optional;

@Component
@Slf4j
public class AdminObjPersist implements Persist<AdministrativeObjectCsvRecord> {
    private final DBService<AdministrativeObject> service;

    @Autowired
    public AdminObjPersist(DBService<AdministrativeObject> service) {
        this.service = service;
    }

    @Override
    public void persist(@NonNull @Nonnull AdministrativeObjectCsvRecord record) {
        log.info(record.toString());
        AdministrativeObject build = AdministrativeObject.builder()
                                                         .adminObjId(record.getId())
                                                         .adminObjType(record.getType())
                                                         .adminObjName(record.getName())
                                                         .build();
        if (!service.exists(build)) {
            Optional<AdministrativeObject> created = service.create(build);
            if (created.isPresent()) {
                log.info("Created: {}", created.get());
            } else {
                log.warn("Not created: {}", record);
            }
        } else {
            log.info("Object exists: {}", build.toString());
        }

    }
}
