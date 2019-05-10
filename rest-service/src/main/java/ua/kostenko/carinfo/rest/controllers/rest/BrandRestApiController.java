package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.web.bind.annotation.*;
import ua.kostenko.carinfo.common.api.records.Brand;
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
@RequestMapping(value = "/api/brands", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class BrandRestApiController extends RestApiController<Brand> {

    protected BrandRestApiController(@Nonnull @NonNull SearchService<Brand> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @GetMapping("getAll")
    @Override
    public PagedResources<Resource<Brand>> getAll(PagedResourcesAssembler<Brand> assembler, Pageable pageable) {
        return getAllPages(assembler, pageable);
    }

    @GetMapping("findForField/{field}")
    @Override
    public PagedResources<Resource<Brand>> findForField(@PathVariable String field, PagedResourcesAssembler<Brand> assembler, Pageable pageable) {
        return findForFieldPages(field, assembler, pageable);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<Resource<Brand>> findByParams(PagedResourcesAssembler<Brand> assembler, Pageable pageable, @RequestParam Map<String, Object> params) {
        return findByParamsPages(params, assembler, pageable);
    }

    @Override
    protected List<Param> getParams() {
        return getParams(Brand.BRAND_NAME);
    }

    @Override
    protected Class<? extends RestApi<Brand>> getControllerClass() {
        return BrandRestApiController.class;
    }
}
