package ua.kostenko.carinfo.carinfoua.persistent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.carinfoua.data.InfoData;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarInfoCRUDRepository extends CrudRepository<InfoData, Long> {

  List<InfoData> findAllByCarNewRegistrationNumberOrCarNewRegistrationNumberLike(String number, String number2);

  Optional<InfoData> findById(Long id);

  List<InfoData> findAllByCarModelContains(String model);

  List<InfoData> findAllByCarBrandContainsOrCarBrandLike(String brand, String number2);

}
