package ua.kostenko.carinfo.rest.data.persistent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.entities.RegistrationInformationEntity;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface RegistrationInformationCrudRepository extends JpaRepository<RegistrationInformationEntity, Long> {
    List<RegistrationInformationEntity> findAllByCarNewRegistrationNumberLike(String number);
}
