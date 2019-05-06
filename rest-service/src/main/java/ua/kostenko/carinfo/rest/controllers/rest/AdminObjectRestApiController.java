package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.rest.services.QueryService;

import javax.annotation.Nonnull;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/administrative", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class AdminObjectRestApiController extends RestApiController<AdministrativeObject> {

    @Autowired
    protected AdminObjectRestApiController(@Nonnull @NonNull QueryService queryService) {
        super(queryService);
    }

    @SuppressWarnings("ConstantConditions")
    @GetMapping("*")
    public ResourceSupport index() {
        ResourceSupport resourceSupport = new ResourceSupport();
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).getAll(null, null)).withRel("getAll"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).findForField(null)).withRel("findForField"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).findByParams(null)).withRel("findByParams"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).countAll()).withRel("countAll"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).countForField(null)).withRel("countForField"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).countByParams(null)).withRel("countByParams"));
        return resourceSupport;
    }

    @GetMapping("getAll")
    @Override
    public Resources<Resource<AdministrativeObject>> getAll(PagedResourcesAssembler<AdministrativeObject> assembler, Pageable pageable) {
        Page<AdministrativeObject> allAdminObjects = queryService.getAllAdminObjects(pageable.getPageNumber());
        return getFromPageable(assembler, allAdminObjects);
    }

    @GetMapping("find/{field}")
    @Override
    public Resources<Resource<AdministrativeObject>> findForField(@Nonnull @NonNull @PathVariable String field) {
        return null;
    }

    @GetMapping("findBy")
    @Override
    public Resources<Resource<AdministrativeObject>> findByParams(@Nonnull @NonNull ParamsHolderBuilder params) {
        return null;
    }

    @GetMapping("count")
    @Override
    public Resource<Integer> countAll() {
        return null;
    }

    @GetMapping("count/{field}")
    @Override
    public Resource<Integer> countForField(@Nonnull @NonNull @PathVariable String field) {
        return null;
    }

    @GetMapping("countBy")
    @Override
    public Resource<Integer> countByParams(@Nonnull @NonNull ParamsHolderBuilder params) {
        return null;
    }
}