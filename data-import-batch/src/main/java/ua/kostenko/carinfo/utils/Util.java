package ua.kostenko.carinfo.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

public final class Util {

    public static String toUpperCase(@Nullable String string) {
        return StringUtils.defaultIfBlank(string, StringUtils.EMPTY).toUpperCase();
    }
}
