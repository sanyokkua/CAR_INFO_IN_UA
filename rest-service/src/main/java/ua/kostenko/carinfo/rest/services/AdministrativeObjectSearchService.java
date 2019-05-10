package ua.kostenko.carinfo.rest.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Slf4j
@Service
public class AdministrativeObjectSearchService extends CommonSearchService<AdministrativeObject, String> {

    @Autowired
    public AdministrativeObjectSearchService(DBService<AdministrativeObject> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return AdministrativeObject.ADMIN_OBJ_NAME;
    }
}
