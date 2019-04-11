package ua.kostenko.carinfo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.function.Supplier;

@Slf4j
public class Utils {

    public static <T> T getResultOrWrapExceptionToNull(Supplier<T> supplier) {
        T t = null;
        try {
            t = supplier.get();
        } catch (EmptyResultDataAccessException ex) {
            //TODO: ignored, returning null if any exception occurred
            log.warn("Exception occurred due extracting value.", ex);
        }
        return t;
    }
}
