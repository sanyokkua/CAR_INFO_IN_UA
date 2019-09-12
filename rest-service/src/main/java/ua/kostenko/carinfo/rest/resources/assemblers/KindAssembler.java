package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.Kind;
import ua.kostenko.carinfo.rest.controllers.rest.KindRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.KindResource;

public class KindAssembler extends ResourceAssemblerSupport<Kind, KindResource> {

    public KindAssembler() {
        super(KindRestApiController.class, KindResource.class);
    }

    @Override
    public KindResource toResource(final Kind entity) {
        final KindResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setKindName(entity.getKindName());
        return resource;
    }
}
