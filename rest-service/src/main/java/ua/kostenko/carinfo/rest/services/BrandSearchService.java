package ua.kostenko.carinfo.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Service
public class BrandSearchService extends CommonSearchService<Brand, String> {

    @Autowired
    public BrandSearchService(final DBService<Brand> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return Brand.BRAND_NAME;
    }
}
