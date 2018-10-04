package ua.kostenko.carinfo.carinfoua.persistent.services;

import ua.kostenko.carinfo.carinfoua.data.InfoData;

import java.util.List;

public interface CarInfoDataService {
  void save(InfoData infoData);

  void saveAll(List<InfoData> infoDataList);

  void remove(InfoData infoData);

  void removeAll(List<InfoData> infoDataList);

  void removeAllByDateForDataSet(String date);

  List<InfoData> search(InfoData.InfoDataFields field, String value);

  boolean checkYearInDb(String dateLabel);
}
