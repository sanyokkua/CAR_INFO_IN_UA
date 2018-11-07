package ua.kostenko.carinfo.consuming.data.persistent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.consuming.data.persistent.entities.RegistrationInformationEntity;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RegistrationInformationCrudRepository extends JpaRepository<RegistrationInformationEntity, Long> {
  RegistrationInformationEntity findFirstByDataSetYear(String year);
}
