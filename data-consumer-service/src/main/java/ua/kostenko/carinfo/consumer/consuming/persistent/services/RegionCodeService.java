package ua.kostenko.carinfo.consumer.consuming.persistent.services;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.entities.RegionCodeEntity;
import ua.kostenko.carinfo.consumer.consuming.persistent.SaveService;
import ua.kostenko.carinfo.consumer.consuming.persistent.repositories.RegionCodeCrudRepository;

import java.util.Collection;

@Service
public class RegionCodeService implements SaveService<RegionCodeEntity> {
    private final RegionCodeCrudRepository crudRepository;

    @Autowired
    public RegionCodeService(RegionCodeCrudRepository crudRepository) {
        Preconditions.checkNotNull(crudRepository);
        this.crudRepository = crudRepository;
    }

    @Override
    public void saveAllObjects(Collection<RegionCodeEntity> objects) {
        crudRepository.saveAll(objects);
    }
}
