package ua.kostenko.carinfo.rest.controllers.rest.common;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;
import ua.kostenko.carinfo.common.api.records.GenericRecord;
import ua.kostenko.carinfo.rest.controllers.rest.RestRootController;
import ua.kostenko.carinfo.rest.services.common.SearchService;
import ua.kostenko.carinfo.rest.utils.MapBuilder;
import ua.kostenko.carinfo.rest.utils.Translation;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
public abstract class DefaultApiController<T extends GenericRecord<I>, I, D extends ResourceSupport> implements RestApi<T, I, D> {
    private final SearchService<T, I> searchService;
    private final Translation translation;

    protected DefaultApiController(@NonNull @Nonnull SearchService<T, I> searchService, @NonNull @Nonnull Translation translation) {
        this.searchService = searchService;
        this.translation = translation;
    }

    @GetMapping
    @Override
    public ResourceSupport index() {
        log.info("Request came to / endpoint");
        return index(getClassInstance());
    }

    @GetMapping("/{id}")
    public D getById(@PathVariable(required = false) Long id) {
        log.info("Request came to /{id} endpoint with id: {}", id);
        if (Objects.nonNull(id)) {
            T t = searchService.getById(id);
            return getResource(t);
        }
        return null;
    }

    @GetMapping("params")
    @Override
    public Resources<Param> params() {
        log.info("Request came to /params endpoint");
        return getResource(getParams(), linkTo(methodOn(getClassInstance()).params()).withSelfRel());
    }

    @GetMapping(path = {"count", "count/{indexField}"})
    @Override
    public Resource<Integer> count(@PathVariable(required = false) String indexField) {
        log.info("Request came to /count/{indexField} endpoint, indexField: {}", indexField);
        int result;
        if (StringUtils.isBlank(indexField) || "{indexField}".equalsIgnoreCase(indexField)) {
            result = searchService.countAll();
        } else {
            result = searchService.countForField(indexField);
        }
        return getResource(result, linkTo(methodOn(getClassInstance()).count(indexField)).withSelfRel());
    }

    @GetMapping("countByParams")
    @Override
    public Resource<Integer> countByParams(T params) {
        log.info("Request came to /countByParams endpoint, params: {}", params);
        int result;
        if (Objects.isNull(params)) {
            result = searchService.countAll();
        } else {
            Map<String, Object> paramToMap = convertParamToMap(params);
            result = searchService.countByParams(paramToMap);
        }
        return getResource(result, linkTo(methodOn(getClassInstance()).countByParams(params)).withSelfRel());
    }

    protected abstract Map<String, Object> convertParamToMap(T params);

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

    private D getResource(T resourceValue) {
        D resource = getResourceAssembler().toResource(resourceValue);
        resource.add(getRootLink());
        return resource;
    }

    protected abstract ResourceAssembler<T, D> getResourceAssembler();

    private Link getRootLink() {
        return linkTo(methodOn(getClassInstance()).index()).withRel("root");
    }

    protected abstract Class<? extends RestApi<T, I, D>> getClassInstance();

    private <R extends RestApi<T, I, D>> ResourceSupport index(Class<R> controllerClass) {
        ResourceSupport resourceSupport = new ResourceSupport();
        resourceSupport.add(linkTo(methodOn(controllerClass).index()).withSelfRel());
        resourceSupport.add(linkTo(methodOn(controllerClass).getById(null)).withSelfRel());
        resourceSupport.add(linkTo(methodOn(controllerClass).params()).withRel("params"));

        Link find = linkTo(methodOn(controllerClass).find(null, null, null)).withRel("find");
        resourceSupport.add(find);
        resourceSupport.add(getFindLinkWithParams(find));

        Link count = linkTo(methodOn(controllerClass).count(null)).withRel("count");
        resourceSupport.add(count);
        resourceSupport.add(getCountLinkWithParams(count));

        resourceSupport.add(getFindByParamsLink(controllerClass));
        resourceSupport.add(getCountByParamsLink(controllerClass));
        resourceSupport.add(ControllerLinkBuilder.linkTo(methodOn(RestRootController.class).index()).withRel("root"));
        return resourceSupport;
    }

    private Link getCountLinkWithParams(Link link) {
        TemplateVariables tmpVars = new TemplateVariables(new TemplateVariable("indexField", TemplateVariable.VariableType.PATH_VARIABLE, "The main field for search"));
        UriTemplate templateVariables = new UriTemplate(link.getHref(), tmpVars);
        return new Link(templateVariables, "count");
    }

    private Link getFindLinkWithParams(Link link) {
        TemplateVariables tmpVars = new TemplateVariables(new TemplateVariable("indexField", TemplateVariable.VariableType.PATH_VARIABLE, "The main field for search"));
        UriTemplate templateVariables = new UriTemplate(link.getHref(), tmpVars);
        return new Link(templateVariables, "find");
    }

    private <R extends RestApi<T, I, D>> Link getFindByParamsLink(Class<R> controllerClass) {
        Link link = linkTo(methodOn(controllerClass).findByParams(null, null, null)).withRel("findByParams");
        TemplateVariables tmpVars = getRequestTemplateVariables();
        UriTemplate templateVariables = new UriTemplate(link.getHref(), tmpVars);
        return new Link(templateVariables, "findByParams");
    }

    private <R extends RestApi<T, I, D>> Link getCountByParamsLink(Class<R> controllerClass) {
        Link link = linkTo(methodOn(controllerClass).countByParams(null)).withRel("countByParams");
        TemplateVariables tmpVars = getRequestTemplateVariables();
        UriTemplate templateVariables = new UriTemplate(link.getHref(), tmpVars);
        return new Link(templateVariables, "countByParams");
    }

    protected PagedResources<D> getFindResult(PagedResourcesAssembler<T> assembler, Pageable pageable, @PathVariable(required = false) String indexField) {
        log.info("Request came to /find/{indexField} endpoint, indexField: {}", indexField);
        Page<T> findResult;
        if (StringUtils.isBlank(indexField) || "{indexField}".equalsIgnoreCase(indexField)) {
            findResult = searchService.getAll(pageable);
        } else {
            findResult = searchService.findForField(indexField, pageable);
        }
        return getPageableResourceFind(assembler, findResult, indexField);
    }

    private PagedResources<D> getPageableResourceFind(@NonNull @Nonnull PagedResourcesAssembler<T> assembler, @NonNull @Nonnull Page<T> page, String indexField) {
        Link link = linkTo(methodOn(getClassInstance()).find(assembler, page.getPageable(), indexField)).withSelfRel();
        PagedResources<D> resources = assembler.toResource(page, getResourceAssembler(), link);
        resources.add(getRootLink());
        return resources;
    }

    protected PagedResources<D> getFindByParamsResult(PagedResourcesAssembler<T> assembler, Pageable pageable, T params) {
        log.info("Request came to /findByParams endpoint, params: {}", params);

        Page<T> findResult;
        if (Objects.isNull(params)) {
            findResult = searchService.getAll(pageable);
        } else {
            Map<String, Object> paramToMap = convertParamToMap(params);
            findResult = searchService.findByParams(paramToMap, pageable);
        }
        return getPageableResource(assembler, findResult, params);
    }

    private PagedResources<D> getPageableResource(@NonNull @Nonnull PagedResourcesAssembler<T> assembler, @NonNull @Nonnull Page<T> page, T params) {
        Link selfLink = linkTo(methodOn(getClassInstance()).findByParams(assembler, page.getPageable(), params)).withSelfRel();
        TemplateVariables tmpVars = getRequestTemplateVariables();
        UriTemplate templateVariables = new UriTemplate(selfLink.getHref(), tmpVars);
        Map<String, Object> arguments = convertParamToMap(params);
        arguments.put("page", page.getPageable().getPageNumber());
        arguments.put("size", page.getPageable().getPageSize());
        Link baseLinkWithParams = new Link(templateVariables, Link.REL_SELF).expand(arguments);
        PagedResources<D> resources = assembler.toResource(page, getResourceAssembler(), baseLinkWithParams);
        resources.add(getRootLink());
        return resources;
    }

    private TemplateVariables getRequestTemplateVariables() {
        List<TemplateVariable> variables = getParams().stream()
                                                      .map(param -> new TemplateVariable(param.getKey(), TemplateVariable.VariableType.REQUEST_PARAM, param.getValue()))
                                                      .collect(Collectors.toList());
        variables.add(new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM, "Number of current page"));
        variables.add(new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM, "Number of records"));
        return new TemplateVariables(variables);
    }

    protected List<Param> buildParamsList(@NonNull @Nonnull String... params) {
        if (params.length > 0) {
            return Stream.of(params)
                         .map(key -> new Param(key, translation.getTranslation(key)))
                         .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    protected MapBuilder<String, Object> mapBuilder() {
        return MapBuilder.getBuilder();
    }

    @ExceptionHandler(Exception.class)
    public final Resource<Errors> handleAllExceptions(Exception exception, WebRequest request) {
        Errors errors = new Errors(new Date(), exception.getMessage(), request.getDescription(false), exception.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new Resource<>(errors, linkTo(methodOn(getClassInstance()).index()).withRel("root"));
    }

}
