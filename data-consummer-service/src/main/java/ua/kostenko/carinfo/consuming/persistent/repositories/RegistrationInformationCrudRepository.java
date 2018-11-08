package ua.kostenko.carinfo.consuming.persistent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.data.entities.RegistrationInformationEntity;

@Repository
public interface RegistrationInformationCrudRepository extends JpaRepository<RegistrationInformationEntity, Long> {
    RegistrationInformationEntity findFirstByDataSetYear(String year);
}
