package ua.kostenko.carinfo.rest.resources.assemblers;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import ua.kostenko.carinfo.common.api.records.Color;
import ua.kostenko.carinfo.rest.controllers.rest.ColorRestApiController;
import ua.kostenko.carinfo.rest.resources.resources.ColorResource;

public class ColorAssembler extends ResourceAssemblerSupport<Color, ColorResource> {

    public ColorAssembler() {
        super(ColorRestApiController.class, ColorResource.class);
    }

    @Override
    public ColorResource toResource(Color entity) {
        ColorResource resource = super.createResourceWithId(entity.getId(), entity);
        resource.setColorName(entity.getColorName());
        return resource;
    }
}
