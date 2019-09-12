package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.Operation;
import ua.kostenko.carinfo.rest.controllers.rest.OperationRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.OperationResource;

public class OperationAssembler extends ResourceAssemblerSupport<Operation, OperationResource> {

    public OperationAssembler() {
        super(OperationRestApiController.class, OperationResource.class);
    }

    @Override
    public OperationResource toResource(final Operation entity) {
        final OperationResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setOperationCode(entity.getOperationCode());
        resource.setOperationName(entity.getOperationName());
        return resource;
    }
}
