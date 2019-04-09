package ua.kostenko.carinfo.common.database.repositories;

import ua.kostenko.carinfo.common.database.PageableSearch;

import javax.transaction.Transactional;

@Transactional
public interface PageableRepository<T> extends CrudRepository<T>, PageableSearch<T> {
}
