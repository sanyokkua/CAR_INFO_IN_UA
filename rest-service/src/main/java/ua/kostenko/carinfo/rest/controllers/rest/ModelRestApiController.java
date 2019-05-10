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
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.ModelAssembler;
import ua.kostenko.carinfo.rest.resources.resources.ModelResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/models", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class ModelRestApiController extends DefaultApiController<Model, String, ModelResource> {

    @Autowired
    protected ModelRestApiController(@Nonnull @NonNull SearchService<Model, String> searchService, @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(Model params) {
        return mapBuilder().put(Model.MODEL_NAME, params.getModelName()).build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(Model.MODEL_NAME);
    }

    @Override
    protected Class<ModelRestApiController> getClassInstance() {
        return ModelRestApiController.class;
    }

    @Override
    protected ResourceAssembler<Model, ModelResource> getResourceAssembler() {
        return new ModelAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<ModelResource> find(PagedResourcesAssembler<Model> assembler, Pageable pageable, @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<ModelResource> findByParams(PagedResourcesAssembler<Model> assembler, Pageable pageable, Model params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
