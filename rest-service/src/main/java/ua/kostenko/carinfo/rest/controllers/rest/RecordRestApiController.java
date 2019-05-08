package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import ua.kostenko.carinfo.common.api.records.Registration;
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
@RequestMapping(value = "/api/registrations", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class RecordRestApiController extends RestApiController<Registration> {

    protected RecordRestApiController(@Nonnull @NonNull SearchService<Registration> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @GetMapping("getAll")
    @Override
    public PagedResources<Resource<Registration>> getAll(PagedResourcesAssembler<Registration> assembler, Pageable pageable) {
        return getAllPages(assembler, pageable);
    }

    @GetMapping("findForField/{field}")
    @Override
    public PagedResources<Resource<Registration>> findForField(@PathVariable String field, PagedResourcesAssembler<Registration> assembler, Pageable pageable) {
        return findForFieldPages(field, assembler, pageable);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<Resource<Registration>> findByParams(@RequestParam Map<String, Object> params, PagedResourcesAssembler<Registration> assembler, Pageable pageable) {
        return findByParamsPages(params, assembler, pageable);
    }

    @Override
    protected List<Param> getParams() {
        return getParams(
                Registration.REGISTRATION_NUMBER,
                Registration.ADMIN_OBJ_NAME,
                Registration.ADMIN_OBJ_TYPE,
                Registration.OPERATION_CODE,
                Registration.OPERATION_NAME,
                Registration.DEPARTMENT_CODE,
                Registration.DEPARTMENT_ADDRESS,
                Registration.DEPARTMENT_EMAIL,
                Registration.KIND,
                Registration.COLOR,
                Registration.BODY_TYPE,
                Registration.PURPOSE,
                Registration.BRAND,
                Registration.MODEL,
                Registration.FUEL_TYPE,
                Registration.ENGINE_CAPACITY,
                Registration.MAKE_YEAR,
                Registration.OWN_WEIGHT,
                Registration.TOTAL_WEIGHT,
                Registration.PERSON_TYPE,
                Registration.REGISTRATION_DATE
        );
    }

    @Override
    protected Class<? extends RestApi<Registration>> getControllerClass() {
        return RecordRestApiController.class;
    }
}
