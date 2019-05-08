package ua.kostenko.carinfo.rest.controllers.rest.common;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kostenko.carinfo.rest.controllers.rest.RestRootController;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
public abstract class RestApiController<T> implements RestApi<T> {
    private final SearchService<T> searchService;
    private final Translation translation;

    protected RestApiController(@Nonnull @NonNull SearchService<T> searchService, @Nonnull @NonNull Translation translation) {
        this.searchService = searchService;
        this.translation = translation;
    }

    @GetMapping("params")
    public Resources<Param> getAllParams() {
        log.info("Request came to /params endpoint");
        return getResource(getParams(), linkTo(methodOn(getControllerClass()).getAllParams()).withSelfRel());
    }

    @GetMapping("countAll")
    @Override
    public Resource<Integer> countAll() {
        log.info("Request came to /countAll endpoint");
        int all = searchService.countAll();
        return getResource(all, linkTo(methodOn(getControllerClass()).countAll()).withSelfRel());
    }

    @GetMapping("countForField/{field}")
    @Override
    public Resource<Integer> countForField(@PathVariable String field) {
        log.info("Request came to /countForField endpoint, field: {}", field);
        int all = searchService.countForField(field);
        return getResource(all, linkTo(methodOn(getControllerClass()).countForField(field)).withSelfRel());
    }

    @GetMapping("countByParams")
    @Override
    public Resource<Integer> countByParams(@RequestParam Map<String, Object> params) {
        log.info("Request came to /countByParams endpoint, params: {}", params);
        int all = searchService.countByParams(params);
        return getResource(all, linkTo(methodOn(getControllerClass()).countByParams(params)).withSelfRel());
    }

    @GetMapping
    public ResourceSupport index() {
        log.info("Request came to / endpoint");
        return index(getControllerClass());
    }

    private static <V, T extends RestApi<V>> ResourceSupport index(Class<T> controllerClass) {
        ResourceSupport resourceSupport = new ResourceSupport();
        resourceSupport.add(linkTo(methodOn(controllerClass).getAll(null, null)).withRel("getAll"));
        resourceSupport.add(linkTo(methodOn(controllerClass).findForField(null, null, null)).withRel("findForField"));
        resourceSupport.add(linkTo(methodOn(controllerClass).findByParams(null, null, null)).withRel("findByParams"));
        resourceSupport.add(linkTo(methodOn(controllerClass).countAll()).withRel("countAll"));
        resourceSupport.add(linkTo(methodOn(controllerClass).countForField(null)).withRel("countForField"));
        resourceSupport.add(linkTo(methodOn(controllerClass).countByParams(null)).withRel("countByParams"));
        resourceSupport.add(linkTo(methodOn(controllerClass).getAllParams()).withRel("params"));
        resourceSupport.add(linkTo(methodOn(controllerClass).index()).withSelfRel());
        resourceSupport.add(ControllerLinkBuilder.linkTo(methodOn(RestRootController.class).index()).withRel("root"));
        return resourceSupport;
    }

    private Resource<Integer> getResource(Integer resourceValue, Link... links) {
        Resource<Integer> resource = new Resource<>(resourceValue, links);
        resource.add(getRootLink());
        return resource;
    }

    private Resources<Param> getResource(List<Param> resourceValue, Link... links) {
        Resources<Param> resource = new Resources<>(resourceValue, links);
        resource.add(getRootLink());
        return resource;
    }

    protected abstract List<Param> getParams();

    protected abstract Class<? extends RestApi<T>> getControllerClass();

    private Link getRootLink() {
        return linkTo(methodOn(getControllerClass()).index()).withRel("root");
    }

    protected PagedResources<Resource<T>> getAllPages(PagedResourcesAssembler<T> assembler, Pageable pageable) {
        log.info("Request came to /getAllPages endpoint, pageable: {}", pageable);
        Page<T> all = searchService.getAll(pageable);
        return getPageableResource(assembler, all);
    }

    private PagedResources<Resource<T>> getPageableResource(@NonNull @Nonnull PagedResourcesAssembler<T> assembler, @NonNull @Nonnull Page<T> page) {
        PagedResources<Resource<T>> resources = assembler.toResource(page);
        resources.add(getRootLink());
        return resources;
    }

    protected PagedResources<Resource<T>> findForFieldPages(@PathVariable String field, PagedResourcesAssembler<T> assembler, Pageable pageable) {
        log.info("Request came to /findForFieldPages endpoint, field: {}, pageable: {}", field, pageable);
        Page<T> forField = searchService.findForField(field, pageable);
        return getPageableResource(assembler, forField);
    }

    protected PagedResources<Resource<T>> findByParamsPages(@RequestParam Map<String, Object> params, PagedResourcesAssembler<T> assembler, Pageable pageable) {
        log.info("Request came to /findByParamsPages endpoint, params: {}, pageable: {}", params, pageable);
        Page<T> byParams = searchService.findByParams(params, pageable);
        return getPageableResource(assembler, byParams);
    }

    protected List<Param> getParams(@NonNull @Nonnull String... params) {
        if (params.length > 0) {
            return Stream.of(params)
                         .map(key -> new Param(key, translation.getTranslation(key)))
                         .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
