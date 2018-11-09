package ua.kostenko.carinfo.consumer.consuming.persistent.services;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.consumer.consuming.persistent.SaveService;
import ua.kostenko.carinfo.consumer.consuming.persistent.repositories.AdministrativeObjectsCrudRepository;

import java.util.Collection;

@Service
public class AdministrativeObjectsService implements SaveService<AdministrativeObjectEntity> {
    private final AdministrativeObjectsCrudRepository crudRepository;

    @Autowired
    public AdministrativeObjectsService(AdministrativeObjectsCrudRepository crudRepository) {
        Preconditions.checkNotNull(crudRepository);
        this.crudRepository = crudRepository;
    }

    @Override
    public void saveAllObjects(Collection<AdministrativeObjectEntity> objects) {
        crudRepository.saveAll(objects);
    }
}
