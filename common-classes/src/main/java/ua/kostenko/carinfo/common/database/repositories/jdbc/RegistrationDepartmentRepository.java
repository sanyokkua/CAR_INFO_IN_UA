package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.mapping.RegistrationDepartment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class RegistrationDepartmentRepository extends CachingJdbcRepository<RegistrationDepartment> {

    @Autowired
    public RegistrationDepartmentRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationDepartment> getCacheLoader() {
        return new CacheLoader<String, RegistrationDepartment>() {
            @Override
            public RegistrationDepartment load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public RegistrationDepartment create(@NonNull @Nonnull RegistrationDepartment entity) {
        return null;
    }

    @Nullable
    @Override
    public RegistrationDepartment update(@NonNull @Nonnull RegistrationDepartment entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public RegistrationDepartment find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<RegistrationDepartment> findAll() {
        return null;
    }

    @Override
    public RegistrationDepartment findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationDepartment entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<RegistrationDepartment> entities) {

    }
}
