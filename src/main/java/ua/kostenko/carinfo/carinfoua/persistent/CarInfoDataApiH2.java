package ua.kostenko.carinfo.carinfoua.persistent;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.carinfoua.data.InfoData;
import ua.kostenko.carinfo.carinfoua.data.InfoDataFields;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CarInfoDataApiH2 implements CarInfoDataApi {
  private CarInfoCRUDRepository carInfoCRUDRepository;

  @Autowired
  public CarInfoDataApiH2(CarInfoCRUDRepository carInfoCRUDRepository) {
    Preconditions.checkNotNull(carInfoCRUDRepository);
    this.carInfoCRUDRepository = carInfoCRUDRepository;
  }

  @Override
  public void save(InfoData infoData) {
    carInfoCRUDRepository.save(infoData);
  }

  @Override
  public void saveAll(List<InfoData> infoDataList) {
    carInfoCRUDRepository.saveAll(infoDataList);
  }

  @Override
  public void update(InfoData infoData) {
    carInfoCRUDRepository.save(infoData);
  }

  @Override
  public void updateAll(List<InfoData> infoDataList) {
    carInfoCRUDRepository.saveAll(infoDataList);
  }

  @Override
  public void remove(InfoData infoData) {
    carInfoCRUDRepository.delete(infoData);
  }

  @Override
  public void removeAll(List<InfoData> infoDataList) {
    carInfoCRUDRepository.deleteAll(infoDataList);
  }

  @Override
  public List<InfoData> search(InfoDataFields field, String value) {
    switch (field) {
      case CAR_NEW_REGISTRATION_NUMBER:
        return carInfoCRUDRepository.findAllByCarNewRegistrationNumberOrCarNewRegistrationNumberLike(value, value);
      case CAR_MODEL:
        return carInfoCRUDRepository.findAllByCarModelContains(value);
      case CAR_BRAND:
        return carInfoCRUDRepository.findAllByCarBrandContainsOrCarBrandLike(value, value);
      default:
        return null;
    }
  }
}
