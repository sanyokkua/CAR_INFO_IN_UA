package ua.kostenko.carinfo.importing.importing;

import lombok.NonNull;

import javax.annotation.Nonnull;

public interface Persist<T> {
    void persist(@NonNull @Nonnull T record);
}
