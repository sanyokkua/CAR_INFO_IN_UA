package ua.kostenko.carinfo.consumer.consuming.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.entities.AdministrativeObjectEntity;

@Repository
public interface AdministrativeObjectsCrudRepository extends CrudRepository<AdministrativeObjectEntity, Long> {
}
