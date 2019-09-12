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
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.PurposeAssembler;
import ua.kostenko.carinfo.rest.resources.resources.PurposeResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

@RestController
@RequestMapping(value = "/api/purposes", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class PurposeRestApiController extends DefaultApiController<Purpose, String, PurposeResource> {

    @Autowired
    protected PurposeRestApiController(@Nonnull @NonNull SearchService<Purpose, String> searchService,
            @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(Purpose params) {
        return mapBuilder().put(Purpose.PURPOSE_NAME, params.getPurposeName()).build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(Purpose.PURPOSE_NAME);
    }

    @Override
    protected Class<PurposeRestApiController> getClassInstance() {
        return PurposeRestApiController.class;
    }

    @Override
    protected ResourceAssembler<Purpose, PurposeResource> getResourceAssembler() {
        return new PurposeAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<PurposeResource> find(PagedResourcesAssembler<Purpose> assembler, Pageable pageable,
            @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<PurposeResource> findByParams(PagedResourcesAssembler<Purpose> assembler, Pageable pageable,
            Purpose params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
