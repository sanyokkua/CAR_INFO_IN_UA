package ua.kostenko.carinfo.rest.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.api.services.DBService;

@Slf4j
@Service
public class AdministrativeObjectSearchService extends CommonSearchService<AdministrativeObject> {

    @Autowired
    public AdministrativeObjectSearchService(DBService<AdministrativeObject> service) {
        super(service);
    }

    @Override
    String getFindForFieldParam() {
        return AdministrativeObject.ADMIN_OBJ_NAME;
    }
}
