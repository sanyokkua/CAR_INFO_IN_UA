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
import org.springframework.web.bind.annotation.RequestParam;
import ua.kostenko.carinfo.rest.services.SearchService;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api")
public abstract class RestApiController<T> {
    final SearchService<T> searchService;

    protected RestApiController(@Nonnull @NonNull SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public abstract Resources<String> getAllParams();

    @GetMapping
    public abstract Resources<Resource<T>> getAll(PagedResourcesAssembler<T> assembler, Pageable pageable);

    @GetMapping
    public abstract Resources<Resource<T>> findForField(@NonNull @Nonnull @PathVariable String field, PagedResourcesAssembler<T> assembler, Pageable pageable);

    @GetMapping
    public abstract Resources<Resource<T>> findByParams(@NonNull @Nonnull @RequestParam Map<String, Object> params, PagedResourcesAssembler<T> assembler, Pageable pageable);

    @GetMapping
    public abstract Resource<Integer> countAll();

    @GetMapping
    public abstract Resource<Integer> countForField(@NonNull @Nonnull @PathVariable String field);

    @GetMapping
    public abstract Resource<Integer> countByParams(@NonNull @Nonnull @RequestParam Map<String, Object> params);

    public Resources<Resource<T>> getFromPageable(PagedResourcesAssembler<T> assembler, Page<T> page) {
        return assembler.toResource(page);
    }
}
