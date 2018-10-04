package ua.kostenko.carinfo.carinfoua.controllers;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import ua.kostenko.carinfo.carinfoua.utils.CSVCarInfoImportTool;
import ua.kostenko.carinfo.carinfoua.utils.CSVKOATUUImportTool;
import ua.kostenko.carinfo.carinfoua.utils.ServiceCenterDataImportTool;

@Controller
public class InitController {
  private CSVCarInfoImportTool csvCarInfoImportTool;
  private CSVKOATUUImportTool csvkoatuuImportTool;
  private ServiceCenterDataImportTool serviceCenterDataImportTool;

  @Autowired
  public InitController(CSVCarInfoImportTool csvCarInfoImportTool, CSVKOATUUImportTool csvkoatuuImportTool, ServiceCenterDataImportTool serviceCenterDataImportTool) {
    Preconditions.checkNotNull(csvCarInfoImportTool);
    Preconditions.checkNotNull(csvkoatuuImportTool);
    Preconditions.checkNotNull(serviceCenterDataImportTool);
    this.csvCarInfoImportTool = csvCarInfoImportTool;
    this.csvkoatuuImportTool = csvkoatuuImportTool;
    this.serviceCenterDataImportTool = serviceCenterDataImportTool;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initDatabase() {
    serviceCenterDataImportTool.initiServiceCenterData();
    csvkoatuuImportTool.initDB();
    csvCarInfoImportTool.initDB();
  }

}
