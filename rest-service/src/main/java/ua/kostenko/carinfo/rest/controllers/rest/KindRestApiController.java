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
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.KindAssembler;
import ua.kostenko.carinfo.rest.resources.resources.KindResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/kinds", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class KindRestApiController extends DefaultApiController<Kind, String, KindResource> {

    @Autowired
    protected KindRestApiController(@Nonnull @NonNull SearchService<Kind, String> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(Kind params) {
        return mapBuilder().put(Kind.KIND_NAME, params.getKindName()).build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(Kind.KIND_NAME);
    }

    @Override
    protected Class<KindRestApiController> getClassInstance() {
        return KindRestApiController.class;
    }

    @Override
    protected ResourceAssembler<Kind, KindResource> getResourceAssembler() {
        return new KindAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<KindResource> find(PagedResourcesAssembler<Kind> assembler, Pageable pageable, @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<KindResource> findByParams(PagedResourcesAssembler<Kind> assembler, Pageable pageable, Kind params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
