package ua.kostenko.carinfo.consumer.consuming.persistent;

import java.util.Collection;

public interface SaveService<T> {
    void saveAllObjects(Collection<T> objects);
}
