package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.rest.controllers.rest.ModelRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.ModelResource;

public class ModelAssembler extends ResourceAssemblerSupport<Model, ModelResource> {

    public ModelAssembler() {
        super(ModelRestApiController.class, ModelResource.class);
    }

    @Override
    public ModelResource toResource(Model entity) {
        ModelResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setModelName(entity.getModelName());
        return resource;
    }
}
