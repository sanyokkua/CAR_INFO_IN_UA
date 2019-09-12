package ua.kostenko.carinfo.rest.controllers.rest;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.NonNull;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.DepartmentAssembler;
import ua.kostenko.carinfo.rest.resources.resources.DepartmentResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

@RestController
@RequestMapping(value = "/api/departments", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class DepartmentRestApiController extends DefaultApiController<Department, Long, DepartmentResource> {

    @Autowired
    protected DepartmentRestApiController(@Nonnull @NonNull SearchService<Department, Long> searchService,
            @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(Department params) {
        return mapBuilder()
                .put(Department.DEPARTMENT_CODE, params.getDepartmentCode())
                .put(Department.DEPARTMENT_ADDRESS, params.getDepartmentAddress())
                .put(Department.DEPARTMENT_EMAIL, params.getDepartmentEmail())
                .build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(Department.DEPARTMENT_CODE, Department.DEPARTMENT_ADDRESS, Department.DEPARTMENT_EMAIL);
    }

    @Override
    protected Class<DepartmentRestApiController> getClassInstance() {
        return DepartmentRestApiController.class;
    }

    @Override
    protected ResourceAssembler<Department, DepartmentResource> getResourceAssembler() {
        return new DepartmentAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<DepartmentResource> find(PagedResourcesAssembler<Department> assembler, Pageable pageable,
            @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<DepartmentResource> findByParams(PagedResourcesAssembler<Department> assembler,
            Pageable pageable, Department params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
