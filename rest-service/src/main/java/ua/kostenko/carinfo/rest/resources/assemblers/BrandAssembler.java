package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.rest.controllers.rest.BrandRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.BrandResource;

public class BrandAssembler extends ResourceAssemblerSupport<Brand, BrandResource> {

    public BrandAssembler() {
        super(BrandRestApiController.class, BrandResource.class);
    }

    @Override
    public BrandResource toResource(final Brand entity) {
        final BrandResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setBrandName(entity.getBrandName());
        return resource;
    }
}
