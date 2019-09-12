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
import ua.kostenko.carinfo.common.api.records.Vehicle;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.VehicleAssembler;
import ua.kostenko.carinfo.rest.resources.resources.VehicleResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

@RestController
@RequestMapping(value = "/api/vehicles", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class VehicleRestApiController extends DefaultApiController<Vehicle, String, VehicleResource> {

    @Autowired
    protected VehicleRestApiController(@Nonnull @NonNull SearchService<Vehicle, String> searchService,
            @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(Vehicle params) {
        return mapBuilder()
                .put(Vehicle.BRAND_NAME, params.getBrandName())
                .put(Vehicle.MODEL_NAME, params.getModelName())
                .build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(Vehicle.BRAND_NAME, Vehicle.MODEL_NAME);
    }

    @Override
    protected Class<VehicleRestApiController> getClassInstance() {
        return VehicleRestApiController.class;
    }

    @Override
    protected ResourceAssembler<Vehicle, VehicleResource> getResourceAssembler() {
        return new VehicleAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<VehicleResource> find(PagedResourcesAssembler<Vehicle> assembler, Pageable pageable,
            @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<VehicleResource> findByParams(PagedResourcesAssembler<Vehicle> assembler, Pageable pageable,
            Vehicle params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
