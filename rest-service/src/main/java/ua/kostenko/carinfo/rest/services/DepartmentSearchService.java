package ua.kostenko.carinfo.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.common.api.services.DBService;
import ua.kostenko.carinfo.rest.services.common.CommonSearchService;

@Service
public class DepartmentSearchService extends CommonSearchService<Department, Long> {

    @Autowired
    public DepartmentSearchService(final DBService<Department> service) {
        super(service);
    }

    @Override
    public String getFindForFieldParam() {
        return Department.DEPARTMENT_CODE;
    }
}
