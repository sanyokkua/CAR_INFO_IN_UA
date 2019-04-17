package ua.kostenko.carinfo.importing.importing.centers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.common.services.CrudService;
import ua.kostenko.carinfo.importing.importing.Persist;

import javax.annotation.Nonnull;

@Slf4j
@Component
public class ServiceCenterPersist implements Persist<ServiceCenter> {
    private final CrudService<Department> service;

    @Autowired
    public ServiceCenterPersist(@NonNull @Nonnull CrudService<Department> service) {
        this.service = service;
    }

    @Override
    public void persist(@NonNull @Nonnull ServiceCenter record) {
        log.info(record.toString());
        Department dep = Department.builder()
                                   .departmentCode(record.getDepId())
                                   .departmentAddress(record.getAddress())
                                   .departmentEmail(record.getEmail())
                                   .build();
        boolean exists = service.isExists(dep);
        if (!exists){
            service.create(dep);
        }
    }
}
