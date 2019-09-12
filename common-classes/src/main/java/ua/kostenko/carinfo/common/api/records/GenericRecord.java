package ua.kostenko.carinfo.common.api.records;

public interface GenericRecord<T> {

    Long getId();

    T getIndexField();
}
