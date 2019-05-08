package ua.kostenko.carinfo.rest.utils;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.Map;

@Data
@Builder
public class Translation {
    private Map<String, String> translations;

    public String getTranslation(@NonNull @Nonnull String key) {
        return translations.getOrDefault(key, key);
    }
}
