package ua.kostenko.carinfo.importing.controllers;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import ua.kostenko.carinfo.importing.utils.Initializer;
import ua.kostenko.carinfo.importing.utils.ServiceCenterDataImportTool;
import ua.kostenko.carinfo.importing.utils.csv.tools.CsvAdministrativeObjectsImportTool;
import ua.kostenko.carinfo.importing.utils.csv.tools.CsvRegionCodeImportTool;
import ua.kostenko.carinfo.importing.utils.csv.tools.CsvRegistrationInformationImportTool;

import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class InitController {
    private final List<Initializer> needToBeInitialized;

    @Autowired
    public InitController(CsvRegistrationInformationImportTool csvRegistrationInformationImportTool, CsvAdministrativeObjectsImportTool csvAdministrativeObjectsImportTool,
                          ServiceCenterDataImportTool serviceCenterDataImportTool, CsvRegionCodeImportTool csvRegionCodeImportTool) {
        Preconditions.checkNotNull(csvRegistrationInformationImportTool);
        Preconditions.checkNotNull(csvAdministrativeObjectsImportTool);
        Preconditions.checkNotNull(serviceCenterDataImportTool);
        Preconditions.checkNotNull(csvRegionCodeImportTool);
        needToBeInitialized = Arrays.asList(csvRegionCodeImportTool, serviceCenterDataImportTool, csvAdministrativeObjectsImportTool, csvRegistrationInformationImportTool);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initDatabase() {
        needToBeInitialized.forEach(Initializer::init);
    }

}
