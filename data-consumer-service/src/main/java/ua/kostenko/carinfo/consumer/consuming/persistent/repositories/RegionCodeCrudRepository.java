package ua.kostenko.carinfo.consumer.consuming.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.entities.RegionCodeEntity;

@Repository
public interface RegionCodeCrudRepository extends CrudRepository<RegionCodeEntity, String> {
}
