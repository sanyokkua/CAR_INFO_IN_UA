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
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.AdministrativeObjectAssembler;
import ua.kostenko.carinfo.rest.resources.resources.AdministrativeObjectResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/administrative", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class AdminObjectRestApiController extends DefaultApiController<AdministrativeObject, String, AdministrativeObjectResource> {

    @Autowired
    protected AdminObjectRestApiController(@Nonnull @NonNull SearchService<AdministrativeObject, String> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(AdministrativeObject params) {
        return mapBuilder()
                .put(AdministrativeObject.ADMIN_OBJ_NAME, params.getAdminObjName())
                .put(AdministrativeObject.ADMIN_OBJ_TYPE, params.getAdminObjType())
                .build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(AdministrativeObject.ADMIN_OBJ_NAME, AdministrativeObject.ADMIN_OBJ_TYPE);
    }

    @Override
    protected Class<AdminObjectRestApiController> getClassInstance() {
        return AdminObjectRestApiController.class;
    }

    @Override
    protected ResourceAssembler<AdministrativeObject, AdministrativeObjectResource> getResourceAssembler() {
        return new AdministrativeObjectAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<AdministrativeObjectResource> find(PagedResourcesAssembler<AdministrativeObject> assembler, Pageable pageable, @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<AdministrativeObjectResource> findByParams(PagedResourcesAssembler<AdministrativeObject> assembler, Pageable pageable, AdministrativeObject params) {
        return getFindByParamsResult(assembler, pageable, params);
    }

}