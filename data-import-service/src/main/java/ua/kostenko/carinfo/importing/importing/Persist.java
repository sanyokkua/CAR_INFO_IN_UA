package ua.kostenko.carinfo.importing.importing;

import javax.annotation.Nonnull;
import lombok.NonNull;

public interface Persist<T> {

    void persist(@NonNull @Nonnull T record);
}
