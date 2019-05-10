package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.Vehicle;
import ua.kostenko.carinfo.rest.controllers.rest.VehicleRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.VehicleResource;

public class VehicleAssembler extends ResourceAssemblerSupport<Vehicle, VehicleResource> {

    public VehicleAssembler() {
        super(VehicleRestApiController.class, VehicleResource.class);
    }

    @Override
    public VehicleResource toResource(Vehicle entity) {
        VehicleResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setBrandName(entity.getBrandName());
        resource.setModelName(entity.getModelName());
        return resource;
    }
}