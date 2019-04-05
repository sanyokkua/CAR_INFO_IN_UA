package ua.kostenko.carinfo.common.database.repositories;

import ua.kostenko.carinfo.common.database.PageableSearch;

public interface PageableRepository<T> extends CrudRepository<T>, PageableSearch<T> {
}
