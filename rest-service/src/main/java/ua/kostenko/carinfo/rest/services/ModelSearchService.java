package ua.kostenko.carinfo.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Service
public class ModelSearchService extends CommonSearchService<Model, String> {

    @Autowired
    public ModelSearchService(final DBService<Model> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return Model.MODEL_NAME;
    }
}
