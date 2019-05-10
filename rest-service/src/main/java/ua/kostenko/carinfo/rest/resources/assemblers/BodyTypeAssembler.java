package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.rest.controllers.rest.BodyTypeRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.BodyTypeResource;

public class BodyTypeAssembler extends ResourceAssemblerSupport<BodyType, BodyTypeResource> {

    public BodyTypeAssembler() {
        super(BodyTypeRestApiController.class, BodyTypeResource.class);
    }

    @Override
    public BodyTypeResource toResource(BodyType entity) {
        BodyTypeResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setBodyTypeName(entity.getBodyTypeName());
        return resource;
    }
}
