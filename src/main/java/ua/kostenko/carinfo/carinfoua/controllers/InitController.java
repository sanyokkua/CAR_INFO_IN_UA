package ua.kostenko.carinfo.carinfoua.controllers;

import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import ua.kostenko.carinfo.carinfoua.utils.CSVImportTool;

@Controller
public class InitController {
  private CSVImportTool csvImportTool;

  @Autowired
  public InitController(CSVImportTool csvImportTool) {
    Preconditions.checkNotNull(csvImportTool);
    this.csvImportTool = csvImportTool;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initDatabase() {
    csvImportTool.initDB();
  }

}
