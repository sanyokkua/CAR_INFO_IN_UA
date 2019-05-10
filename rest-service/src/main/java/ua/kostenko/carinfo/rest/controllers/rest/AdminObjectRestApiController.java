package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.controllers.rest.common.RestApiController;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/administrative", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class AdminObjectRestApiController extends RestApiController<AdministrativeObject> {

    @Autowired
    protected AdminObjectRestApiController(@Nonnull @NonNull SearchService<AdministrativeObject> queryService, @NonNull @Nonnull Translation translation) {
        super(queryService, translation);
    }

    @GetMapping("getAll")
    @Override
    public PagedResources<Resource<AdministrativeObject>> getAll(PagedResourcesAssembler<AdministrativeObject> assembler, Pageable pageable) {
        return getAllPages(assembler, pageable);
    }

    @GetMapping("findForField/{field}")
    @Override
    public PagedResources<Resource<AdministrativeObject>> findForField(@PathVariable String field, PagedResourcesAssembler<AdministrativeObject> assembler, Pageable pageable) {
        return findForFieldPages(field, assembler, pageable);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<Resource<AdministrativeObject>> findByParams(PagedResourcesAssembler<AdministrativeObject> assembler, Pageable pageable, @RequestParam Map<String, Object> params) {
        return findByParamsPages(params, assembler, pageable);
    }

    @Override
    protected List<Param> getParams() {
        return getParams(AdministrativeObject.ADMIN_OBJ_NAME, AdministrativeObject.ADMIN_OBJ_TYPE);
    }

    @Override
    protected Class<AdminObjectRestApiController> getControllerClass() {
        return AdminObjectRestApiController.class;
    }

}