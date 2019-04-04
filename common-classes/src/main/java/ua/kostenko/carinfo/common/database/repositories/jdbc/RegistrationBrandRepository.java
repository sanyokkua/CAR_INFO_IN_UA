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
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.raw.RegistrationBrand;
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
public class RegistrationBrandRepository extends CachingJdbcRepository<RegistrationBrand> implements FieldSearchable<RegistrationBrand> {
    private static final RowMapper<RegistrationBrand> ROW_MAPPER = (resultSet, i) -> RegistrationBrand.builder()
                                                                                                      .brandId(resultSet.getLong(Constants.RegistrationBrand.ID))
                                                                                                      .brandName(resultSet.getString(Constants.RegistrationBrand.NAME))
                                                                                                      .build();
    @Autowired
    public RegistrationBrandRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, RegistrationBrand> getCacheLoader() {
        return new CacheLoader<String, RegistrationBrand>() {
            @Override
            public RegistrationBrand load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Override
    public RegistrationBrand findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.brand where brand_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public RegistrationBrand create(@NonNull @Nonnull RegistrationBrand entity) {
        String jdbcTemplateInsert = "insert into carinfo.brand (brand_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getBrandName());
        return getFromCache(entity.getBrandName());
    }

    @Nullable
    @Override
    public RegistrationBrand update(@NonNull @Nonnull RegistrationBrand entity) {
        getCache().invalidate(entity.getBrandName());
        String jdbcTemplateUpdate = "update carinfo.brand set brand_name = ? where brand_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getBrandName(), entity.getBrandId());
        return findByField(entity.getBrandName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        RegistrationBrand brand = find(id);
        if (nonNull(brand)) {
            getCache().invalidate(brand.getBrandName());
        }
        String jdbcTemplateDelete = "delete from carinfo.brand where brand_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public RegistrationBrand find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.brand where brand_id = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<RegistrationBrand> findAll() {
        String jdbcTemplateSelect = "select * from carinfo.brand;";
        return jdbcTemplate.query(jdbcTemplateSelect, ROW_MAPPER);
    }

    @Override
    public boolean isExistsId(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelectCount = "select count(brand_id) from carinfo.brand where brand_id = ?;";
        long numberOfRows = jdbcTemplate.queryForObject(jdbcTemplateSelectCount, (rs, rowNum) -> rs.getLong(1), id);
        return numberOfRows > 0;
    }

    @Override
    public boolean isExists(@NonNull @Nonnull RegistrationBrand entity) {
        return nonNull(getFromCache(entity.getBrandName()));
    }

    @Override
    public void createAll(Iterable<RegistrationBrand> entities) {
        final int batchSize = 100;
        List<List<RegistrationBrand>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<RegistrationBrand> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.brand (brand_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    RegistrationBrand object = batch.get(i);
                    ps.setString(1, object.getBrandName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }
}
