package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.mapping.RegistrationColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationColorRepository extends CachingJdbcRepository<RegistrationColor> {

    @Autowired
    public RegistrationColorRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationColor> getCacheLoader() {
        return new CacheLoader<String, RegistrationColor>() {
            @Override
            public RegistrationColor load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationColor create(@NonNull @Nonnull RegistrationColor entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationColor update(@NonNull @Nonnull RegistrationColor entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationColor find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationColor> findAll() {
        return null;
    }

    @Override
    public RegistrationColor findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationColor entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationColor> entities) {

    }
}
