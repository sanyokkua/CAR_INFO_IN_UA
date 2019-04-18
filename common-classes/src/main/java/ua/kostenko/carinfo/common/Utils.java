package ua.kostenko.carinfo.common;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class Utils {

    public static <T> T getResultOrWrapExceptionToNull(Supplier<T> supplier) {
        T t = null;
        try {
            t = supplier.get();
        } catch (Exception ex) {
            log.warn("Exception occurred due extracting value.", ex);
        }
        return t;
    }

    public static <T> Optional<T> processIgnoreException(Supplier<Optional<T>> supplier) {
        Optional<T> t = Optional.empty();
        try {
            t = supplier.get();
        } catch (Exception ex) {
            log.warn("Exception occurred due extracting value.", ex);
        }
        return t;
    }
}
