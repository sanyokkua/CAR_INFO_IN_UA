package ua.kostenko.carinfo.carinfoua.persistent;

import ua.kostenko.carinfo.carinfoua.data.InfoData;
import ua.kostenko.carinfo.carinfoua.data.InfoDataFields;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarInfoDataApiInMemory implements CarInfoDataApi {
  private static final List<InfoData> INFO_DATA = new ArrayList<>();

  @Override
  public void save(InfoData infoData) {
    INFO_DATA.add(infoData);
  }

  @Override
  public void saveAll(List<InfoData> infoDataList) {
    INFO_DATA.addAll(infoDataList);
    System.out.println("Size of DB: " + INFO_DATA.size());
  }

  @Override
  public void update(InfoData infoData) {
    INFO_DATA.stream().filter(infoData1 -> infoData.getCarNewRegistrationNumber().equals(infoData1.getCarNewRegistrationNumber())).findFirst().ifPresent(infoData1 -> {
      infoData1 = infoData;
    });
  }

  @Override
  public void updateAll(List<InfoData> infoDataList) {
    infoDataList.forEach(this::update);
  }

  @Override
  public void remove(InfoData infoData) {
    INFO_DATA.remove(infoData);
  }

  @Override
  public void removeAll(List<InfoData> infoDataList) {
    infoDataList.forEach(this::remove);
  }

  @Override
  public List<InfoData> search(InfoDataFields field, String value) {
    return INFO_DATA.stream().filter(infoData -> infoData.getFieldValue(field).equalsIgnoreCase(value) || infoData.getFieldValue(field).toUpperCase().contains(value.toUpperCase())).collect(Collectors.toList());
  }
}
