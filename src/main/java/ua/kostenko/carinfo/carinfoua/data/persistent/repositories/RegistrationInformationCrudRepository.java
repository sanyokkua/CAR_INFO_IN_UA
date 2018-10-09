package ua.kostenko.carinfo.carinfoua.data.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.carinfoua.data.persistent.entities.RegistrationInformationEntity;

import java.util.List;

@Repository
public interface RegistrationInformationCrudRepository extends CrudRepository<RegistrationInformationEntity, Long> {

    RegistrationInformationEntity findFirstByDataSetYear(String year);

    List<RegistrationInformationEntity> findAllByCarNewRegistrationNumberLike(String number);

    List<RegistrationInformationEntity> findAllByCarModelLike(String model);

    List<RegistrationInformationEntity> findAllByCarBrandLike(String brand);

    void deleteAllByDataSetYear(String dataSetYear);

}
