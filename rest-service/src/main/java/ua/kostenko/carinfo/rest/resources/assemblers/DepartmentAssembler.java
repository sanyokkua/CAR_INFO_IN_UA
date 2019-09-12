package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.Department;
import ua.kostenko.carinfo.rest.controllers.rest.DepartmentRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.DepartmentResource;

public class DepartmentAssembler extends ResourceAssemblerSupport<Department, DepartmentResource> {

    public DepartmentAssembler() {
        super(DepartmentRestApiController.class, DepartmentResource.class);
    }

    @Override
    public DepartmentResource toResource(final Department entity) {
        final DepartmentResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setDepartmentCode(entity.getDepartmentCode());
        resource.setDepartmentAddress(entity.getDepartmentAddress());
        resource.setDepartmentEmail(entity.getDepartmentEmail());
        return resource;
    }
}
