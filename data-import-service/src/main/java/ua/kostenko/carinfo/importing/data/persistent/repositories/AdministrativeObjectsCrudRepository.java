package ua.kostenko.carinfo.importing.data.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.AdministrativeObjectEntity;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AdministrativeObjectsCrudRepository extends CrudRepository<AdministrativeObjectEntity, Long> {
}
