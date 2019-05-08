package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import ua.kostenko.carinfo.common.api.records.FuelType;
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
@RequestMapping(value = "/api/fuels", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class FuelTypeRestApiController extends RestApiController<FuelType> {

    protected FuelTypeRestApiController(@Nonnull @NonNull SearchService<FuelType> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @GetMapping("getAll")
    @Override
    public PagedResources<Resource<FuelType>> getAll(PagedResourcesAssembler<FuelType> assembler, Pageable pageable) {
        return getAllPages(assembler, pageable);
    }

    @GetMapping("findForField/{field}")
    @Override
    public PagedResources<Resource<FuelType>> findForField(@PathVariable String field, PagedResourcesAssembler<FuelType> assembler, Pageable pageable) {
        return findForFieldPages(field, assembler, pageable);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<Resource<FuelType>> findByParams(@RequestParam Map<String, Object> params, PagedResourcesAssembler<FuelType> assembler, Pageable pageable) {
        return findByParamsPages(params, assembler, pageable);
    }

    @Override
    protected List<Param> getParams() {
        return getParams(Constants.RegistrationFuelType.NAME);
    }

    @Override
    protected Class<? extends RestApi<FuelType>> getControllerClass() {
        return FuelTypeRestApiController.class;
    }
}
