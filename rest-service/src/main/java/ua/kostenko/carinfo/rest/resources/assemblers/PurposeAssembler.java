package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.Purpose;
import ua.kostenko.carinfo.rest.controllers.rest.PurposeRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.PurposeResource;

public class PurposeAssembler extends ResourceAssemblerSupport<Purpose, PurposeResource> {

    public PurposeAssembler() {
        super(PurposeRestApiController.class, PurposeResource.class);
    }

    @Override
    public PurposeResource toResource(final Purpose entity) {
        final PurposeResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setPurposeName(entity.getPurposeName());
        return null;
    }
}
