package ua.kostenko.carinfo.rest.controllers.rest.common;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import ua.kostenko.carinfo.common.api.records.GenericRecord;

public interface RestApi<T extends GenericRecord<I>, I, D extends ResourceSupport> {

    ResourceSupport index();

    D getById(@PathVariable(required = false) Long id);

    Resources<Param> params();

    PagedResources<D> find(PagedResourcesAssembler<T> assembler, Pageable pageable,
            @PathVariable(required = false) String indexField);

    Resource<Integer> count(@PathVariable(required = false) String indexField);

    PagedResources<D> findByParams(PagedResourcesAssembler<T> assembler, Pageable pageable, T params);

    Resource<Integer> countByParams(T params);
}
