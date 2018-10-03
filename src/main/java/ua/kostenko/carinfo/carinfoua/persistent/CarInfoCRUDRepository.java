package ua.kostenko.carinfo.carinfoua.persistent;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.carinfoua.data.InfoData;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

@Repository
public interface CarInfoCRUDRepository extends CrudRepository<InfoData, Long> {

  List<InfoData> findAllByCarNewRegistrationNumberOrCarNewRegistrationNumberLike(String number, String number2);

  Optional<InfoData> findById(Long id);

  List<InfoData> findAllByCarModelContainsOrCarModelLike(String model, String model2);

  List<InfoData> findAllByCarBrandContainsOrCarBrandLike(String brand, String number2);

  InfoData findFirstByDataSetYear(String year);

  @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + Integer.MIN_VALUE))
  @Query("select t from InfoData t")
  Stream<InfoData> streamAll();
}
