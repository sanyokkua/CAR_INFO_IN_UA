package ua.kostenko.carinfo.importing.data.persistent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.importing.data.persistent.entities.RegistrationInformationEntity;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface RegistrationInformationCrudRepository extends JpaRepository<RegistrationInformationEntity, Long> {
  RegistrationInformationEntity findFirstByDataSetYear(String year);
}
