package ua.kostenko.carinfo.carinfoua.persistent.services;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.carinfoua.data.InfoData;
import ua.kostenko.carinfo.carinfoua.data.KOATUU;
import ua.kostenko.carinfo.carinfoua.persistent.repositories.CarInfoCrudRepository;
import ua.kostenko.carinfo.carinfoua.persistent.repositories.KOATUUCrudRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CarInfoDataServiceDB implements CarInfoDataService {
  private final static Logger LOGGER = LoggerFactory.getLogger(CarInfoDataServiceDB.class);
  private CarInfoCrudRepository carInfoCRUDRepository;
  private KOATUUCrudRepository koatuuCrudRepository;

  @Autowired
  public CarInfoDataServiceDB(CarInfoCrudRepository carInfoCRUDRepository, KOATUUCrudRepository koatuuCrudRepository) {
    Preconditions.checkNotNull(carInfoCRUDRepository);
    Preconditions.checkNotNull(koatuuCrudRepository);
    this.carInfoCRUDRepository = carInfoCRUDRepository;
    this.koatuuCrudRepository = koatuuCrudRepository;
  }

  @Override
  public void save(InfoData infoData) {
    LOGGER.info("Saving InfoData Object: {}", infoData);
    carInfoCRUDRepository.save(infoData);
  }

  @Override
  public void saveAll(List<InfoData> infoDataList) {
    LOGGER.info("Saving list of InfoData Objects, size: {}", infoDataList.size());
    carInfoCRUDRepository.saveAll(infoDataList);
  }

  @Override
  public void remove(InfoData infoData) {
    LOGGER.info("Removing InfoData Object {}", infoData);
    carInfoCRUDRepository.delete(infoData);
  }

  @Override
  public void removeAll(List<InfoData> infoDataList) {
    LOGGER.info("Removing list of InfoData Objects, size: {}", infoDataList.size());
    carInfoCRUDRepository.deleteAll(infoDataList);
  }

  @Override
  public void removeAllByDateForDataSet(String date) {
    LOGGER.info("Removing all data for date: {}", date);
    carInfoCRUDRepository.deleteAllByDataSetYear(date);
  }

  @Override
  public List<InfoData> search(InfoData.InfoDataFields field, String value) {
    LOGGER.info("Searching InfoData for field: {}, value: {}", field.name(), value);
    List<InfoData> results = Collections.EMPTY_LIST;
    switch (field) {
      case CAR_NEW_REGISTRATION_NUMBER:
        results = carInfoCRUDRepository.findAllByCarNewRegistrationNumberLike(value);
        break;
      case CAR_MODEL:
        results = carInfoCRUDRepository.findAllByCarModelLike(value);
        break;
      case CAR_BRAND:
        results = carInfoCRUDRepository.findAllByCarBrandLike(value);
        break;
      default:
        LOGGER.warn("Field {} is not supported yet, null will be returned", field.name());
        break;
    }
    results.stream().forEach(infoData -> {
      Optional<KOATUU> koatuuOptional = koatuuCrudRepository.findById(Integer.valueOf(infoData.getReg_addr_koatuu()));
      koatuuOptional.ifPresent(koatuu -> infoData.setReg_addr_koatuu(koatuu.getName()));
    });
    return results;
  }

  @Override
  public boolean checkYearInDb(String date) {
    LOGGER.info("Checking existing records for date: {}", date);
    Preconditions.checkNotNull(date);
    InfoData firstByDataSetYear = carInfoCRUDRepository.findFirstByDataSetYear(date);
    return firstByDataSetYear != null && date.equalsIgnoreCase(firstByDataSetYear.getDataSetYear());
  }
}
