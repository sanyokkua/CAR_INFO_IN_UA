package ua.kostenko.carinfo.rest.utils;

import java.util.Map;
import javax.annotation.Nonnull;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Translation {

    private Map<String, String> translations;

    public String getTranslation(@NonNull @Nonnull String key) {
        return translations.getOrDefault(key, key);
    }
}
