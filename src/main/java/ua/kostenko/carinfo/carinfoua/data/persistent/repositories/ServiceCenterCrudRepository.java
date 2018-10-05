package ua.kostenko.carinfo.carinfoua.data.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.ServiceCenterEntity;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ServiceCenterCrudRepository extends CrudRepository<ServiceCenterEntity, Long> {}
