package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
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
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.RegistrationAssembler;
import ua.kostenko.carinfo.rest.resources.resources.RegistrationResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/registrations", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class RecordRestApiController extends DefaultApiController<Registration, String, RegistrationResource> {

    @Autowired
    protected RecordRestApiController(@Nonnull @NonNull SearchService<Registration, String> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(Registration params) {
        return mapBuilder()
                .put(Registration.REGISTRATION_NUMBER, params.getRegistrationNumber())
                .put(Registration.ADMIN_OBJ_NAME, params.getAdminObjName())
                .put(Registration.ADMIN_OBJ_TYPE, params.getAdminObjType())
                .put(Registration.OPERATION_CODE, params.getOperationCode())
                .put(Registration.OPERATION_NAME, params.getOperationName())
                .put(Registration.DEPARTMENT_CODE, params.getDepartmentCode())
                .put(Registration.DEPARTMENT_ADDRESS, params.getDepartmentAddress())
                .put(Registration.DEPARTMENT_EMAIL, params.getDepartmentEmail())
                .put(Registration.KIND, params.getKindName())
                .put(Registration.COLOR, params.getColorName())
                .put(Registration.BODY_TYPE, params.getBodyTypeName())
                .put(Registration.PURPOSE, params.getPurposeName())
                .put(Registration.BRAND, params.getBrandName())
                .put(Registration.MODEL, params.getModelName())
                .put(Registration.FUEL_TYPE, params.getFuelTypeName())
                .put(Registration.ENGINE_CAPACITY, params.getEngineCapacity())
                .put(Registration.MAKE_YEAR, params.getMakeYear())
                .put(Registration.OWN_WEIGHT, params.getOwnWeight())
                .put(Registration.TOTAL_WEIGHT, params.getTotalWeight())
                .put(Registration.PERSON_TYPE, params.getPersonType())
                .put(Registration.REGISTRATION_DATE, params.getRegistrationDate())
                .build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(
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
    protected Class<RecordRestApiController> getClassInstance() {
        return RecordRestApiController.class;
    }

    @Override
    protected ResourceAssembler<Registration, RegistrationResource> getResourceAssembler() {
        return new RegistrationAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<RegistrationResource> find(PagedResourcesAssembler<Registration> assembler, Pageable pageable, @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<RegistrationResource> findByParams(PagedResourcesAssembler<Registration> assembler, Pageable pageable, Registration params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
