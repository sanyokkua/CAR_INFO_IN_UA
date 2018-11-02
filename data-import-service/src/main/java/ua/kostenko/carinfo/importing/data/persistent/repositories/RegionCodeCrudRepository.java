package ua.kostenko.carinfo.importing.data.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.kostenko.carinfo.importing.data.persistent.entities.RegionCodeEntity;

public interface RegionCodeCrudRepository extends CrudRepository<RegionCodeEntity, String> {
}
