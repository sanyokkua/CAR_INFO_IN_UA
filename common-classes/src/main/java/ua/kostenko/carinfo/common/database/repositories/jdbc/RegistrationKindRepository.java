package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.mapping.RegistrationKind;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationKindRepository extends CachingJdbcRepository<RegistrationKind> {

    @Autowired
    public RegistrationKindRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationKind> getCacheLoader() {
        return new CacheLoader<String, RegistrationKind>() {
            @Override
            public RegistrationKind load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationKind create(@NonNull @Nonnull RegistrationKind entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationKind update(@NonNull @Nonnull RegistrationKind entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationKind find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationKind> findAll() {
        return null;
    }

    @Override
    public RegistrationKind findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationKind entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationKind> entities) {

    }
}
