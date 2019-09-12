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
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.rest.controllers.rest.common.DefaultApiController;
import ua.kostenko.carinfo.rest.controllers.rest.common.Param;
import ua.kostenko.carinfo.rest.resources.assemblers.OperationAssembler;
import ua.kostenko.carinfo.rest.resources.resources.OperationResource;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

@RestController
@RequestMapping(value = "/api/operations", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class OperationRestApiController extends DefaultApiController<Operation, Long, OperationResource> {

    @Autowired
    protected OperationRestApiController(@Nonnull @NonNull SearchService<Operation, Long> searchService,
            @Nonnull @NonNull Translation translation) {
        super(searchService, translation);
    }

    @Override
    protected Map<String, Object> convertParamToMap(Operation params) {
        return mapBuilder()
                .put(Operation.OPERATION_CODE, params.getOperationCode())
                .put(Operation.OPERATION_CODE, params.getOperationCode())
                .build();
    }

    @Override
    protected List<Param> getParams() {
        return buildParamsList(Operation.OPERATION_CODE, Operation.OPERATION_NAME);
    }

    @Override
    protected Class<OperationRestApiController> getClassInstance() {
        return OperationRestApiController.class;
    }

    @Override
    protected ResourceAssembler<Operation, OperationResource> getResourceAssembler() {
        return new OperationAssembler();
    }

    @GetMapping(path = {"find", "find/{indexField}"})
    @Override
    public PagedResources<OperationResource> find(PagedResourcesAssembler<Operation> assembler, Pageable pageable,
            @PathVariable(required = false) String indexField) {
        return getFindResult(assembler, pageable, indexField);
    }

    @GetMapping("findByParams")
    @Override
    public PagedResources<OperationResource> findByParams(PagedResourcesAssembler<Operation> assembler,
            Pageable pageable, Operation params) {
        return getFindByParamsResult(assembler, pageable, params);
    }
}
