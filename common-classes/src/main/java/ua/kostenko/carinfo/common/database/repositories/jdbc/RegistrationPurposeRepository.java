package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.mapping.RegistrationPurpose;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationPurposeRepository extends CachingJdbcRepository<RegistrationPurpose> {

    @Autowired
    public RegistrationPurposeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationPurpose> getCacheLoader() {
        return new CacheLoader<String, RegistrationPurpose>() {
            @Override
            public RegistrationPurpose load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationPurpose create(@NonNull @Nonnull RegistrationPurpose entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationPurpose update(@NonNull @Nonnull RegistrationPurpose entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationPurpose find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationPurpose> findAll() {
        return null;
    }

    @Override
    public RegistrationPurpose findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationPurpose entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationPurpose> entities) {

    }
}
