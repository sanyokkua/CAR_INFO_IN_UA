package ua.kostenko.carinfo.rest.controllers.rest.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface RestApi<T> {
    Resources<Param> getAllParams();
    PagedResources<Resource<T>> getAll(PagedResourcesAssembler<T> assembler, Pageable pageable);
    PagedResources<Resource<T>> findForField(@PathVariable String field, PagedResourcesAssembler<T> assembler, Pageable pageable);
    PagedResources<Resource<T>> findByParams(@RequestParam Map<String, Object> params, PagedResourcesAssembler<T> assembler, Pageable pageable);

    Resource<Integer> countAll();
    Resource<Integer> countForField(String field);
    Resource<Integer> countByParams(Map<String, Object> params);
    ResourceSupport index();
}
