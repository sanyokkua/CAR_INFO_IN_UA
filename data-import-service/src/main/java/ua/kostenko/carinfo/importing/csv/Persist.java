package ua.kostenko.carinfo.importing.csv;

public interface Persist<T> {
    void persist(T obj);
}
