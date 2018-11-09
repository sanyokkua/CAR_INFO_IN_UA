package ua.kostenko.carinfo.consumer.consuming.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.entities.ServiceCenterEntity;

@Repository
public interface ServiceCenterCrudRepository extends CrudRepository<ServiceCenterEntity, Long> {
}
