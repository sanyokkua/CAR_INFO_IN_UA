package ua.kostenko.carinfo.consumer.consuming.persistent.services;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.entities.ServiceCenterEntity;
import ua.kostenko.carinfo.consumer.consuming.persistent.SaveService;
import ua.kostenko.carinfo.consumer.consuming.persistent.repositories.ServiceCenterCrudRepository;

import java.util.Collection;

@Service
public class ServiceCenterService implements SaveService<ServiceCenterEntity> {
    private final ServiceCenterCrudRepository crudRepository;

    @Autowired
    public ServiceCenterService(ServiceCenterCrudRepository crudRepository) {
        Preconditions.checkNotNull(crudRepository);
        this.crudRepository = crudRepository;
    }

    @Override
    public void saveAllObjects(Collection<ServiceCenterEntity> objects) {
        crudRepository.saveAll(objects);
    }
}
