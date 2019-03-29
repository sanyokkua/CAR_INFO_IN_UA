package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationBrandRepository extends CachingJdbcRepository<RegistrationBrandRepository> {

    @Autowired
    public RegistrationBrandRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationBrandRepository> getCacheLoader() {
        return new CacheLoader<String, RegistrationBrandRepository>() {
            @Override
            public RegistrationBrandRepository load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationBrandRepository create(@NonNull @Nonnull RegistrationBrandRepository entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationBrandRepository update(@NonNull @Nonnull RegistrationBrandRepository entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationBrandRepository find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationBrandRepository> findAll() {
        return null;
    }

    @Override
    public RegistrationBrandRepository findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationBrandRepository entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationBrandRepository> entities) {

    }
}
