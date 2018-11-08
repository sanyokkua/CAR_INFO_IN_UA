package ua.kostenko.carinfo.consuming.persistent.services;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.data.entities.AdministrativeObjectEntity;
import ua.kostenko.carinfo.consuming.persistent.SaveService;
import ua.kostenko.carinfo.consuming.persistent.repositories.AdministrativeObjectsCrudRepository;

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
