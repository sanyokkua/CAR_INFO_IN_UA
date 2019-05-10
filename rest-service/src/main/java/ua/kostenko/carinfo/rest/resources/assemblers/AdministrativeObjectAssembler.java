package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.rest.controllers.rest.AdminObjectRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.AdministrativeObjectResource;

public class AdministrativeObjectAssembler extends ResourceAssemblerSupport<AdministrativeObject, AdministrativeObjectResource> {

    public AdministrativeObjectAssembler() {
        super(AdminObjectRestApiController.class, AdministrativeObjectResource.class);
    }

    @Override
    public AdministrativeObjectResource toResource(AdministrativeObject entity) {
        AdministrativeObjectResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setAdminObjType(entity.getAdminObjType());
        resource.setAdminObjName(entity.getAdminObjName());
        return resource;
    }
}
