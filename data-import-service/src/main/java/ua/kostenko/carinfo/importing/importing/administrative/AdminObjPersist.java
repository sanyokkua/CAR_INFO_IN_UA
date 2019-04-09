package ua.kostenko.carinfo.importing.importing.administrative;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.records.AdministrativeObject;
import ua.kostenko.carinfo.common.services.CrudService;
import ua.kostenko.carinfo.importing.csv.pojo.AdministrativeObjectPojo;
import ua.kostenko.carinfo.importing.importing.Persist;

import javax.annotation.Nonnull;

@Component
@Slf4j
public class AdminObjPersist implements Persist<AdministrativeObjectPojo> {
    private final CrudService<AdministrativeObject> repository;

    @Autowired
    public AdminObjPersist(CrudService<AdministrativeObject> repository) {
        this.repository = repository;
    }

    @Override
    public void persist(@NonNull @Nonnull AdministrativeObjectPojo record) {
        log.info(record.toString());
        AdministrativeObject build = AdministrativeObject.builder()
                                                         .adminObjId(record.getId())
                                                         .adminObjType(record.getType())
                                                         .adminObjName(record.getName())
                                                         .build();
        if (!repository.isExists(build)) {
            AdministrativeObject adminObject = repository.create(build);
            log.info("Created: {}", adminObject.toString());
        } else {
            log.info("Object exists: {}", build.toString());
        }

    }
}
