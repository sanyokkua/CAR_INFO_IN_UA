package ua.kostenko.carinfo.consuming.persistent;

import java.util.Collection;

public interface SaveService<T> {
    void saveAllObjects(Collection<T> objects);
}
