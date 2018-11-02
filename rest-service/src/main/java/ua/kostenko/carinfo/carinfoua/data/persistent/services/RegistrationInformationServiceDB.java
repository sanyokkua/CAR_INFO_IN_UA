package ua.kostenko.carinfo.carinfoua.data.persistent.services;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;
import ua.kostenko.carinfo.carinfoua.data.persistent.repositories.RegistrationInformationCrudRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RegistrationInformationServiceDB implements RegistrationInformationService {
  private final RegistrationInformationCrudRepository registrationInformationCRUDRepository;

  @Autowired
  public RegistrationInformationServiceDB(RegistrationInformationCrudRepository registrationInformationCRUDRepository) {
    Preconditions.checkNotNull(registrationInformationCRUDRepository);
    this.registrationInformationCRUDRepository = registrationInformationCRUDRepository;
  }

  @Override
  public List<RegistrationInformationEntity> search(String value) {
    log.info("Searching RegistrationInformationEntity for field: {}, value: {}", "CarNewRegistrationNumber", value);
    List<RegistrationInformationEntity> results = registrationInformationCRUDRepository.findAllByCarNewRegistrationNumberLike(value);

    log.warn("Field {} is not supported yet, null will be returned", "CarNewRegistrationNumber");

    return results;
  }

}
