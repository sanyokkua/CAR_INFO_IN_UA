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
public class RegistrationBodyTypeRepository extends CachingJdbcRepository<RegistrationBodyTypeRepository> {

    @Autowired
    public RegistrationBodyTypeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationBodyTypeRepository> getCacheLoader() {
        return new CacheLoader<String, RegistrationBodyTypeRepository>() {
            @Override
            public RegistrationBodyTypeRepository load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationBodyTypeRepository create(@NonNull @Nonnull RegistrationBodyTypeRepository entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationBodyTypeRepository update(@NonNull @Nonnull RegistrationBodyTypeRepository entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationBodyTypeRepository find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationBodyTypeRepository> findAll() {
        return null;
    }

    @Override
    public RegistrationBodyTypeRepository findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationBodyTypeRepository entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationBodyTypeRepository> entities) {

    }
}
