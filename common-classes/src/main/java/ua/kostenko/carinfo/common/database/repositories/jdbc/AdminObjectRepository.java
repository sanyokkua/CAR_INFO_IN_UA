package ua.kostenko.carinfo.common.database.repositories.jdbc;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.database.Constants.AdminObject;
import ua.kostenko.carinfo.common.database.raw.AdministrativeObject;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;
import ua.kostenko.carinfo.common.database.repositories.FieldSearchable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class AdminObjectRepository extends CachingJdbcRepository<AdministrativeObject> implements FieldSearchable<AdministrativeObject> {
    private static final RowMapper<AdministrativeObject> ROW_MAPPER = (resultSet, i) -> AdministrativeObject.builder()
                                                                                                            .adminObjId(resultSet.getLong(AdminObject.ID))
                                                                                                            .adminObjType(resultSet.getString(AdminObject.TYPE))
                                                                                                            .adminObjName(resultSet.getString(AdminObject.NAME))
                                                                                                            .build();
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
        String jdbcTemplateInsert = "insert into carinfo.admin_object (admin_obj_id, admin_obj_type, admin_obj_name) values (?, ?, ?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getAdminObjId(), entity.getAdminObjType(), entity.getAdminObjName());
        return getFromCache(entity.getAdminObjName());
    }

    @Nullable
    @Override
    public AdministrativeObject update(@NonNull @Nonnull AdministrativeObject entity) {
        getCache().invalidate(entity.getAdminObjName());
        String jdbcTemplateUpdate = "update carinfo.admin_object set admin_obj_type = ?, admin_obj_name = ? where admin_obj_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getAdminObjType(), entity.getAdminObjName(), entity.getAdminObjId());
        return findByField(entity.getAdminObjName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        AdministrativeObject administrativeObject = find(id);
        if (nonNull(administrativeObject)) {
            getCache().invalidate(administrativeObject.getAdminObjName());
        }
        String jdbcTemplateDelete = "delete from carinfo.admin_object where admin_obj_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public AdministrativeObject find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.admin_object where admin_obj_id = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<AdministrativeObject> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.admin_object;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(admin_obj_id) from carinfo.admin_object where admin_obj_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull AdministrativeObject entity) {
        return nonNull(getFromCache(entity.getAdminObjName()));
    }

    @Override
    public void createAll(Iterable<AdministrativeObject> entities) {
        final int batchSize = 100;
        List<List<AdministrativeObject>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<AdministrativeObject> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.admin_object (admin_obj_id, admin_obj_type, admin_obj_name) values (?, ?, ?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    AdministrativeObject object = batch.get(i);
                    ps.setLong(1, object.getAdminObjId());
                    ps.setString(2, object.getAdminObjType());
                    ps.setString(3, object.getAdminObjName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public AdministrativeObject findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.admin_object where admin_obj_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }
}
