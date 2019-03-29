package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.Constants.AdminObject;
import ua.kostenko.carinfo.common.database.mapping.AdministrativeObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
public class AdminObjectRepository extends CachingJdbcRepository<AdministrativeObject> {

    @Autowired
    public AdminObjectRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public CacheLoader<String, AdministrativeObject> getCacheLoader() {
        return new CacheLoader<String, AdministrativeObject>() {
            @Override
            public AdministrativeObject load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Nullable
    @Override
    public AdministrativeObject create(@NonNull @Nonnull AdministrativeObject entity) {
        String templateSql = "insert into %s.%s (%s, %s, %s) values (?,?,?)";
        String sql = String.format(templateSql, Constants.SCHEMA, AdminObject.TABLE, AdminObject.ID, AdminObject.TYPE, AdminObject.NAME);
        jdbcTemplate.update(sql, entity.getAdminObjId(), entity.getAdminObjType().toUpperCase(), entity.getAdminObjName().toUpperCase());
        return findByField(entity.getAdminObjName().toUpperCase());
    }

    @Nullable
    @Override
    public AdministrativeObject update(@NonNull @Nonnull AdministrativeObject entity) {
        return null;
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        return false;
    }

    @Nullable
    @Override
    public AdministrativeObject find(@NonNull @Nonnull Long id) {
        return null;
    }

    @Override
    public List<AdministrativeObject> findAll() {
        return null;
    }

    @Override
    public AdministrativeObject findByField(String fieldValue) {
        return null;
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        return false;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull AdministrativeObject entity) {
        return false;
    }

    @Override
    public void createAll(Iterable<AdministrativeObject> entities) {

    }
}
