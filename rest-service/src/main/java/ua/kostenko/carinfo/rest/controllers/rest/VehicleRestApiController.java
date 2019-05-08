package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import ua.kostenko.carinfo.common.api.records.Vehicle;
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
@RequestMapping(value = "/api/vehicles", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class VehicleRestApiController extends RestApiController<Vehicle> {

    protected VehicleRestApiController(@Nonnull @NonNull SearchService<Vehicle> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @GetMapping("getAll")
    @Override
    public PagedResources<Resource<Vehicle>> getAll(PagedResourcesAssembler<Vehicle> assembler, Pageable pageable) {
        return getAllPages(assembler, pageable);
    }

    @GetMapping("findForField/{field}")
    @Override
    public PagedResources<Resource<Vehicle>> findForField(@PathVariable String field, PagedResourcesAssembler<Vehicle> assembler, Pageable pageable) {
        return findForFieldPages(field, assembler, pageable);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<Resource<Vehicle>> findByParams(@RequestParam Map<String, Object> params, PagedResourcesAssembler<Vehicle> assembler, Pageable pageable) {
        return findByParamsPages(params, assembler, pageable);
    }

    @Override
    protected List<Param> getParams() {
        return getParams(Constants.RegistrationModel.NAME, Constants.RegistrationBrand.NAME);
    }

    @Override
    protected Class<? extends RestApi<Vehicle>> getControllerClass() {
        return VehicleRestApiController.class;
    }
}
