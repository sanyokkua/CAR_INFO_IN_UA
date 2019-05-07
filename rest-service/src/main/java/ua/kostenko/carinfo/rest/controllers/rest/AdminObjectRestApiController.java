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
import org.springframework.web.bind.annotation.*;
import ua.kostenko.carinfo.common.api.records.AdministrativeObject;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.rest.services.SearchService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/administrative", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class AdminObjectRestApiController extends RestApiController<AdministrativeObject> {

    @Autowired
    protected AdminObjectRestApiController(@Nonnull @NonNull SearchService queryService) {
        super(queryService);
    }

    @GetMapping("params")
    @Override
    public Resources<String> getAllParams() {
        List<String> params = Stream.of(Constants.AdminObject.ID, Constants.AdminObject.TYPE, Constants.AdminObject.NAME).collect(Collectors.toList());
        return new Resources<>(params, linkTo(methodOn(AdminObjectRestApiController.class).getAllParams()).withSelfRel());
    }

    @GetMapping("getAll")
    @Override
    public Resources<Resource<AdministrativeObject>> getAll(PagedResourcesAssembler<AdministrativeObject> assembler, Pageable pageable) {
        Page<AdministrativeObject> allAdminObjects = searchService.getAll(pageable);
        return getFromPageable(assembler, allAdminObjects);
    }

    @GetMapping("findForField/{field}")
    @Override
    public Resources<Resource<AdministrativeObject>> findForField(@Nonnull @NonNull @PathVariable String field, PagedResourcesAssembler<AdministrativeObject> assembler, Pageable pageable) {
        Page<AdministrativeObject> forField = searchService.findForField(field, pageable);
        return getFromPageable(assembler, forField);
    }

    @GetMapping("findByParams")
    @Override
    public Resources<Resource<AdministrativeObject>> findByParams(@Nonnull @NonNull @RequestParam Map<String, Object> params, PagedResourcesAssembler<AdministrativeObject> assembler, Pageable pageable) {
        Page<AdministrativeObject> byParams = searchService.findByParams(params, pageable);
        return getFromPageable(assembler, byParams);
    }

    @GetMapping("countAll")
    @Override
    public Resource<Integer> countAll() {
        int all = searchService.countAll();
        return new Resource<>(all, linkTo(methodOn(AdminObjectRestApiController.class).countAll()).withSelfRel());
    }

    @GetMapping("countForField/{field}")
    @Override
    public Resource<Integer> countForField(@Nonnull @NonNull @PathVariable String field) {
        int all = searchService.countForField(field);
        return new Resource<>(all, linkTo(methodOn(AdminObjectRestApiController.class).countForField(field)).withSelfRel());
    }

    @GetMapping("countByParams")
    @Override
    public Resource<Integer> countByParams(@Nonnull @NonNull @RequestParam Map<String, Object> params) {
        int all = searchService.countByParams(params);
        return new Resource<>(all, linkTo(methodOn(AdminObjectRestApiController.class).countByParams(params)).withSelfRel());
    }

    @SuppressWarnings("ConstantConditions")
    @GetMapping("*")
    public ResourceSupport index() {
        ResourceSupport resourceSupport = new ResourceSupport();
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).getAll(null, null)).withRel("getAll"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).findForField(null, null, null)).withRel("findForField"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).findByParams(null, null, null)).withRel("findByParams"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).countAll()).withRel("countAll"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).countForField(null)).withRel("countForField"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).countByParams(null)).withRel("countByParams"));
        resourceSupport.add(linkTo(methodOn(AdminObjectRestApiController.class).getAllParams()).withRel("params"));
        return resourceSupport;
    }

}