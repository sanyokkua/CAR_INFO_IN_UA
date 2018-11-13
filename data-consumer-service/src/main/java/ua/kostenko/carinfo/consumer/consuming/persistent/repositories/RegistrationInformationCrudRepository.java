package ua.kostenko.carinfo.consumer.consuming.persistent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.entities.RegistrationInformationEntity;

@Repository
public interface RegistrationInformationCrudRepository extends JpaRepository<RegistrationInformationEntity, Long> {
}
