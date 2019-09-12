package ua.kostenko.carinfo.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Service
public class FuelTypeSearchService extends CommonSearchService<FuelType, String> {

    @Autowired
    public FuelTypeSearchService(final DBService<FuelType> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return FuelType.FUEL_NAME;
    }
}
