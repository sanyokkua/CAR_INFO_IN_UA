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
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.BodyTypeAssembler;
import ua.kostenko.carinfo.rest.resources.resources.BodyTypeResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/bodies", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class BodyTypeRestApiController extends DefaultApiController<BodyType, String, BodyTypeResource> {

    @Autowired
    protected BodyTypeRestApiController(@Nonnull @NonNull SearchService<BodyType, String> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(BodyType params) {
        return mapBuilder().put(BodyType.BODY_TYPE_NAME, params.getBodyTypeName()).build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(BodyType.BODY_TYPE_NAME);
    }

    @Override
    protected Class<BodyTypeRestApiController> getClassInstance() {
        return BodyTypeRestApiController.class;
    }

    @Override
    protected ResourceAssembler<BodyType, BodyTypeResource> getResourceAssembler() {
        return new BodyTypeAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<BodyTypeResource> find(PagedResourcesAssembler<BodyType> assembler, Pageable pageable, @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<BodyTypeResource> findByParams(PagedResourcesAssembler<BodyType> assembler, Pageable pageable, BodyType params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
