package ua.kostenko.carinfo.common.database;

public interface ExtendedSearch<T, S extends SearchBuilder<T>> {
    S findByPages();
}
