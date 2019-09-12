package ua.kostenko.carinfo.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Vehicle;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Service
public class VehicleSearchService extends CommonSearchService<Vehicle, String> {

    @Autowired
    public VehicleSearchService(final DBService<Vehicle> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return Vehicle.BRAND_NAME;
    }
}
