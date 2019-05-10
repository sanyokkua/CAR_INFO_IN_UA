package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import ua.kostenko.carinfo.common.api.records.Color;
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
@RequestMapping(value = "/api/colors", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ColorRestApiController extends RestApiController<Color> {

    protected ColorRestApiController(@Nonnull @NonNull SearchService<Color> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @GetMapping("getAll")
    @Override
    public PagedResources<Resource<Color>> getAll(PagedResourcesAssembler<Color> assembler, Pageable pageable) {
        return getAllPages(assembler, pageable);
    }

    @GetMapping("findForField/{field}")
    @Override
    public PagedResources<Resource<Color>> findForField(@PathVariable String field, PagedResourcesAssembler<Color> assembler, Pageable pageable) {
        return findForFieldPages(field, assembler, pageable);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<Resource<Color>> findByParams(PagedResourcesAssembler<Color> assembler, Pageable pageable, @RequestParam Map<String, Object> params) {
        return findByParamsPages(params, assembler, pageable);
    }

    @Override
    protected List<Param> getParams() {
        return getParams(Color.COLOR_NAME);
    }

    @Override
    protected Class<? extends RestApi<Color>> getControllerClass() {
        return ColorRestApiController.class;
    }
}
