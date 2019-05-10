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
import ua.kostenko.carinfo.common.api.records.Color;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.ColorAssembler;
import ua.kostenko.carinfo.rest.resources.resources.ColorResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/colors", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ColorRestApiController extends DefaultApiController<Color, String, ColorResource> {

    @Autowired
    protected ColorRestApiController(@Nonnull @NonNull SearchService<Color, String> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(Color params) {
        return mapBuilder().put(Color.COLOR_NAME, params.getColorName()).build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(Color.COLOR_NAME);
    }

    @Override
    protected Class<ColorRestApiController> getClassInstance() {
        return ColorRestApiController.class;
    }

    @Override
    protected ResourceAssembler<Color, ColorResource> getResourceAssembler() {
        return new ColorAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<ColorResource> find(PagedResourcesAssembler<Color> assembler, Pageable pageable, @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<ColorResource> findByParams(PagedResourcesAssembler<Color> assembler, Pageable pageable, Color params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
