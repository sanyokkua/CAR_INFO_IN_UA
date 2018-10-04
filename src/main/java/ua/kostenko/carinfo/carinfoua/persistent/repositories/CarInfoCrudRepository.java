package ua.kostenko.carinfo.carinfoua.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.carinfoua.data.InfoData;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CarInfoCrudRepository extends CrudRepository<InfoData, Long> {

  InfoData findFirstByDataSetYear(String year);

  List<InfoData> findAllByCarNewRegistrationNumberLike(String number);

  List<InfoData> findAllByCarModelLike(String model);

  List<InfoData> findAllByCarBrandLike(String brand);

  void deleteAllByDataSetYear(String dataSetYear);

}
