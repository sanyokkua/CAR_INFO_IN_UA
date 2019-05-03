package ua.kostenko.carinfo.rest.controllers.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.rest.services.QueryService;

import javax.annotation.Nonnull;

@Slf4j
@RestController
@RequestMapping("/api")
public abstract class RestApiController<T> {
    private final QueryService queryService;

    protected RestApiController(@Nonnull @NonNull QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public abstract Resources<T> getAll();

    @GetMapping
    public abstract Resources<T> findWithField(@NonNull @Nonnull String field);

    @GetMapping
    public abstract Resources<T> findByParams(@NonNull @Nonnull ParamsHolderBuilder params);

    @GetMapping
    public abstract Resource<Integer> countAll();

    @GetMapping
    public abstract Resource<Integer> countForField(@NonNull @Nonnull String field);

    @GetMapping
    public abstract Resource<Integer> countByParams(@NonNull @Nonnull ParamsHolderBuilder params);
}
