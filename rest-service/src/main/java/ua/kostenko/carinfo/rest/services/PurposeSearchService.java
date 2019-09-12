package ua.kostenko.carinfo.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Service
public class PurposeSearchService extends CommonSearchService<Purpose, String> {

    @Autowired
    public PurposeSearchService(final DBService<Purpose> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return Purpose.PURPOSE_NAME;
    }
}
