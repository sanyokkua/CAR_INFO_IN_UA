package ua.kostenko.carinfo.importing.importing.centers;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.importing.importing.Persist;

import javax.annotation.Nonnull;
import java.util.Optional;

@Slf4j
@Component
public class ServiceCenterPersist implements Persist<ServiceCenter> {
    private final DBService<Department> service;

    @Autowired
    public ServiceCenterPersist(@NonNull @Nonnull DBService<Department> service) {
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
        if (!service.exists(dep)) {
            Optional<Department> department = service.create(dep);
            if (department.isPresent()) {
                log.info("Created department: {}", department.get());
            } else {
                log.warn("Department is not created. {}", record);
            }
        } else {
            log.info("Department {} already exists", dep);
        }
    }
}
