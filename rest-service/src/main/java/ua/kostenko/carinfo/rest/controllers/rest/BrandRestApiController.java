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
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.BrandAssembler;
import ua.kostenko.carinfo.rest.resources.resources.BrandResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

@RestController
@RequestMapping(value = "/api/brands", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class BrandRestApiController extends DefaultApiController<Brand, String, BrandResource> {

    @Autowired
    protected BrandRestApiController(@Nonnull @NonNull SearchService<Brand, String> searchService,
            @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(Brand params) {
        return mapBuilder().put(Brand.BRAND_NAME, params.getBrandName()).build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(Brand.BRAND_NAME);
    }

    @Override
    protected Class<BrandRestApiController> getClassInstance() {
        return BrandRestApiController.class;
    }

    @Override
    protected ResourceAssembler<Brand, BrandResource> getResourceAssembler() {
        return new BrandAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<BrandResource> find(PagedResourcesAssembler<Brand> assembler, Pageable pageable,
            @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<BrandResource> findByParams(PagedResourcesAssembler<Brand> assembler, Pageable pageable,
            Brand params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
