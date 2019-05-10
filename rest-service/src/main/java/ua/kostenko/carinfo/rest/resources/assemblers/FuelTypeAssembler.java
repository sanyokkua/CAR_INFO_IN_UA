package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.FuelType;
import ua.kostenko.carinfo.rest.controllers.rest.FuelTypeRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.FuelTypeResource;

public class FuelTypeAssembler extends ResourceAssemblerSupport<FuelType, FuelTypeResource> {

    public FuelTypeAssembler() {
        super(FuelTypeRestApiController.class, FuelTypeResource.class);
    }

    @Override
    public FuelTypeResource toResource(FuelType entity) {
        FuelTypeResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setFuelTypeName(entity.getFuelTypeName());
        return resource;
    }
}
