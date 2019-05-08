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
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.controllers.rest.common.RestApiController;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/bodies", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class BodyTypeRestApiController extends RestApiController<BodyType> {

    @Autowired
    protected BodyTypeRestApiController(@Nonnull @NonNull SearchService<BodyType> searchService, @NonNull @Nonnull Translation translation) {
        super(searchService, translation);
    }

    @GetMapping("getAll")
    @Override
    public PagedResources<Resource<BodyType>> getAll(PagedResourcesAssembler<BodyType> assembler, Pageable pageable) {
        return getAllPages(assembler, pageable);
    }

    @GetMapping("findForField/{field}")
    @Override
    public PagedResources<Resource<BodyType>> findForField(@PathVariable String field, PagedResourcesAssembler<BodyType> assembler, Pageable pageable) {
        return findForFieldPages(field, assembler, pageable);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<Resource<BodyType>> findByParams(@RequestParam Map<String, Object> params, PagedResourcesAssembler<BodyType> assembler, Pageable pageable) {
        return findByParamsPages(params, assembler, pageable);
    }

    @Override
    protected List<Param> getParams() {
        return getParams(Constants.RegistrationBodyType.ID, Constants.RegistrationBodyType.NAME);
    }

    @Override
    protected Class<BodyTypeRestApiController> getControllerClass() {
        return BodyTypeRestApiController.class;
    }
}
