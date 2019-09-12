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
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.FuelTypeAssembler;
import ua.kostenko.carinfo.rest.resources.resources.FuelTypeResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

@RestController
@RequestMapping(value = "/api/fuels", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class FuelTypeRestApiController extends DefaultApiController<FuelType, String, FuelTypeResource> {

    @Autowired
    protected FuelTypeRestApiController(@Nonnull @NonNull SearchService<FuelType, String> searchService,
            @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(FuelType params) {
        return mapBuilder().put(FuelType.FUEL_NAME, params.getFuelTypeName()).build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(FuelType.FUEL_NAME);
    }

    @Override
    protected Class<FuelTypeRestApiController> getClassInstance() {
        return FuelTypeRestApiController.class;
    }

    @Override
    protected ResourceAssembler<FuelType, FuelTypeResource> getResourceAssembler() {
        return new FuelTypeAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<FuelTypeResource> find(PagedResourcesAssembler<FuelType> assembler, Pageable pageable,
            @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<FuelTypeResource> findByParams(PagedResourcesAssembler<FuelType> assembler, Pageable pageable,
            FuelType params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
