package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.Registration;
import ua.kostenko.carinfo.rest.controllers.rest.RecordRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.RegistrationResource;

public class RegistrationAssembler extends ResourceAssemblerSupport<Registration, RegistrationResource> {

    public RegistrationAssembler() {
        super(RecordRestApiController.class, RegistrationResource.class);
    }

    @Override
    public RegistrationResource toResource(final Registration entity) {
        final RegistrationResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setAdminObjName(entity.getAdminObjName());
        resource.setAdminObjType(entity.getAdminObjType());
        resource.setOperationCode(entity.getOperationCode());
        resource.setOperationName(entity.getOperationName());
        resource.setDepartmentCode(entity.getDepartmentCode());
        resource.setDepartmentAddress(entity.getDepartmentAddress());
        resource.setDepartmentEmail(entity.getDepartmentEmail());
        resource.setKindName(entity.getKindName());
        resource.setColorName(entity.getColorName());
        resource.setBodyTypeName(entity.getBodyTypeName());
        resource.setPurposeName(entity.getPurposeName());
        resource.setBrandName(entity.getBrandName());
        resource.setModelName(entity.getModelName());
        resource.setFuelTypeName(entity.getFuelTypeName());
        resource.setEngineCapacity(entity.getEngineCapacity());
        resource.setMakeYear(entity.getMakeYear());
        resource.setOwnWeight(entity.getOwnWeight());
        resource.setTotalWeight(entity.getTotalWeight());
        resource.setPersonType(entity.getPersonType());
        resource.setRegistrationNumber(entity.getRegistrationNumber());
        resource.setRegistrationDate(entity.getRegistrationDate());
        return resource;
    }
}
