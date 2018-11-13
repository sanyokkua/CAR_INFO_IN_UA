package ua.kostenko.carinfo.rest.data.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import ua.kostenko.carinfo.common.entities.RegionCodeEntity;

public interface RegionCodeCrudRepository extends CrudRepository<RegionCodeEntity, String> {
    RegionCodeEntity findByRegion(String region);
}
