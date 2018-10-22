package ua.kostenko.carinfo.carinfoua.data.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegionCodeEntity;

public interface RegionCodeCrudRepository extends CrudRepository<RegionCodeEntity, String> {
}
