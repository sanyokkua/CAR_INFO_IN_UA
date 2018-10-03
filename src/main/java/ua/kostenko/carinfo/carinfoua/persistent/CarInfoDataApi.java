package ua.kostenko.carinfo.carinfoua.persistent;

import ua.kostenko.carinfo.carinfoua.data.InfoData;
import ua.kostenko.carinfo.carinfoua.data.InfoDataFields;

import java.util.List;

public interface CarInfoDataApi {
  void save(InfoData infoData);

  void saveAll(List<InfoData> infoDataList);

  void update(InfoData infoData);

  void updateAll(List<InfoData> infoDataList);

  void remove(InfoData infoData);

  void removeAll(List<InfoData> infoDataList);

  List<InfoData> search(InfoDataFields field, String value);

  List<InfoData> searchRaw(String field, String value);

  boolean checkYearInDb(String dateLabel);
}
