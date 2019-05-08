package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class RestRootController {

    @GetMapping
    public ResourceSupport index() {
        log.info("Request came to /api endpoint");
        ResourceSupport resourceSupport = new ResourceSupport();
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).index()).withRel("administrative"));
        resourceSupport.add(linkTo(methodOn(BodyTypeRestApiController.class).index()).withRel("bodies"));
        resourceSupport.add(linkTo(methodOn(BrandRestApiController.class).index()).withRel("brands"));
        resourceSupport.add(linkTo(methodOn(ColorRestApiController.class).index()).withRel("colors"));
        resourceSupport.add(linkTo(methodOn(DepartmentRestApiController.class).index()).withRel("departments"));
        resourceSupport.add(linkTo(methodOn(FuelTypeRestApiController.class).index()).withRel("fuels"));
        resourceSupport.add(linkTo(methodOn(KindRestApiController.class).index()).withRel("kinds"));
        resourceSupport.add(linkTo(methodOn(ModelRestApiController.class).index()).withRel("models"));
        resourceSupport.add(linkTo(methodOn(OperationRestApiController.class).index()).withRel("operations"));
        resourceSupport.add(linkTo(methodOn(PurposeRestApiController.class).index()).withRel("purposes"));
        resourceSupport.add(linkTo(methodOn(RecordRestApiController.class).index()).withRel("registrations"));
        resourceSupport.add(linkTo(methodOn(VehicleRestApiController.class).index()).withRel("vehicles"));
        resourceSupport.add(linkTo(methodOn(RestRootController.class).index()).withSelfRel());
        return resourceSupport;
    }
}
