package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.rest.services.QueryService;

import javax.annotation.Nonnull;

@Slf4j
@RequestMapping("/api")
public abstract class RestApiController<T> {
    protected final QueryService queryService;

    protected RestApiController(@Nonnull @NonNull QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public abstract Resources<Resource<T>> getAll(PagedResourcesAssembler<T> assembler, Pageable pageable);

    @GetMapping
    public abstract Resources<Resource<T>> findForField(@NonNull @Nonnull @PathVariable String field);

    @GetMapping
    public abstract Resources<Resource<T>> findByParams(@NonNull @Nonnull ParamsHolderBuilder params);

    @GetMapping
    public abstract Resource<Integer> countAll();

    @GetMapping
    public abstract Resource<Integer> countForField(@NonNull @Nonnull @PathVariable String field);

    @GetMapping
    public abstract Resource<Integer> countByParams(@NonNull @Nonnull ParamsHolderBuilder params);

    public Resources<Resource<T>> getFromPageable(PagedResourcesAssembler<T> assembler, Page<T> page) {
        return assembler.toResource(page);
    }
}
