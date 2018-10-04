package ua.kostenko.carinfo.carinfoua.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.carinfoua.data.ServiceCenterData;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ServiceCenterCrudRepository extends CrudRepository<ServiceCenterData, Integer> {

}
