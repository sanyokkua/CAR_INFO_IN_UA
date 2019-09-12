package ua.kostenko.carinfo.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Service
public class KindSearchService extends CommonSearchService<Kind, String> {

    @Autowired
    public KindSearchService(final DBService<Kind> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return Kind.KIND_NAME;
    }
}
