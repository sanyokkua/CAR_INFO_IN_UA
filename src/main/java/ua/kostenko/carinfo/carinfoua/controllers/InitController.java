package ua.kostenko.carinfo.carinfoua.controllers;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import ua.kostenko.carinfo.carinfoua.utils.CsvAdministrativeObjectsImportTool;
import ua.kostenko.carinfo.carinfoua.utils.CsvRegistrationInformationImportTool;
import ua.kostenko.carinfo.carinfoua.utils.ServiceCenterDataImportTool;

@Controller
@Slf4j
public class InitController {
    private final CsvRegistrationInformationImportTool csvRegistrationInformationImportTool;
    private final CsvAdministrativeObjectsImportTool csvAdministrativeObjectsImportTool;
    private final ServiceCenterDataImportTool serviceCenterDataImportTool;

    @Autowired
    public InitController(CsvRegistrationInformationImportTool csvRegistrationInformationImportTool, CsvAdministrativeObjectsImportTool csvAdministrativeObjectsImportTool,
                          ServiceCenterDataImportTool serviceCenterDataImportTool) {
        Preconditions.checkNotNull(csvRegistrationInformationImportTool);
        Preconditions.checkNotNull(csvAdministrativeObjectsImportTool);
        Preconditions.checkNotNull(serviceCenterDataImportTool);
        this.csvRegistrationInformationImportTool = csvRegistrationInformationImportTool;
        this.csvAdministrativeObjectsImportTool = csvAdministrativeObjectsImportTool;
        this.serviceCenterDataImportTool = serviceCenterDataImportTool;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initDatabase() {
        serviceCenterDataImportTool.initDB();
        csvAdministrativeObjectsImportTool.initDB();
        csvRegistrationInformationImportTool.initDB();
    }

}
