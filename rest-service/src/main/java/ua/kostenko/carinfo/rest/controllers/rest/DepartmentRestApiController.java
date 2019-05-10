package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.controllers.rest.common.RestApi;
import ua.kostenko.carinfo.rest.controllers.rest.common.RestApiController;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/departments", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class DepartmentRestApiController extends RestApiController<Department> {

    protected DepartmentRestApiController(@Nonnull @NonNull SearchService<Department> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @GetMapping("getAll")
    @Override
    public PagedResources<Resource<Department>> getAll(PagedResourcesAssembler<Department> assembler, Pageable pageable) {
        return getAllPages(assembler, pageable);
    }

    @GetMapping("findForField/{field}")
    @Override
    public PagedResources<Resource<Department>> findForField(@PathVariable String field, PagedResourcesAssembler<Department> assembler, Pageable pageable) {
        return findForFieldPages(field, assembler, pageable);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<Resource<Department>> findByParams(PagedResourcesAssembler<Department> assembler, Pageable pageable, @RequestParam Map<String, Object> params) {
        return findByParamsPages(params, assembler, pageable);
    }

    @Override
    protected List<Param> getParams() {
        return getParams(Department.DEPARTMENT_CODE, Department.DEPARTMENT_ADDRESS, Department.DEPARTMENT_EMAIL);
    }

    @Override
    protected Class<? extends RestApi<Department>> getControllerClass() {
        return DepartmentRestApiController.class;
    }
}
