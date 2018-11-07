package ua.kostenko.carinfo.consuming.data.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.kostenko.carinfo.consuming.data.persistent.entities.RegionCodeEntity;

public interface RegionCodeCrudRepository extends CrudRepository<RegionCodeEntity, String> {
}
