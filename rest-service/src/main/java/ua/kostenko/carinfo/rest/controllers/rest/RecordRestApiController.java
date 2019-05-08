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
import ua.kostenko.carinfo.common.database.Constants;
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
                Constants.RegistrationRecord.REGISTRATION_NUMBER,
                Constants.AdminObject.NAME,
                Constants.RegistrationOperation.CODE,
                Constants.RegistrationColor.NAME,
                Constants.RegistrationBodyType.NAME,
                Constants.RegistrationPurpose.NAME,
                Constants.RegistrationModel.NAME,
                Constants.RegistrationBrand.NAME,
                Constants.RegistrationFuelType.NAME,
                Constants.RegistrationRecord.DEPARTMENT_CODE,
                Constants.RegistrationRecord.ENGINE_CAPACITY,
                Constants.RegistrationRecord.OWN_WEIGHT,
                Constants.RegistrationRecord.TOTAL_WEIGHT,
                Constants.RegistrationRecord.REGISTRATION_NUMBER,
                Constants.RegistrationRecord.REGISTRATION_DATE,
                Constants.RegistrationRecord.KIND,
                Constants.RegistrationRecord.MAKE_YEAR,
                Constants.RegistrationRecord.PERSON_TYPE
        );
    }

    @Override
    protected Class<? extends RestApi<Registration>> getControllerClass() {
        return RecordRestApiController.class;
    }
}
