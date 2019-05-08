package ua.kostenko.carinfo.rest.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Slf4j
@Service
public class DepartmentSearchService extends CommonSearchService<Department> {

    @Autowired
    public DepartmentSearchService(DBService<Department> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return Constants.RegistrationDepartment.CODE;
    }
}
