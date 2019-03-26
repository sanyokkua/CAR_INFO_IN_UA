package ua.kostenko.carinfo.importing.importing;

import lombok.NonNull;

public interface Persist<T> {
    void persist(@NonNull T record);
}
