package ua.kostenko.carinfo.common.database.repositories.jdbc.crud;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.ParamsHolder;
import ua.kostenko.carinfo.common.database.Constants;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;
import ua.kostenko.carinfo.common.database.repositories.FieldSearchable;
import ua.kostenko.carinfo.common.database.repositories.PageableRepository;
import ua.kostenko.carinfo.common.records.Brand;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
@Slf4j
public class RegistrationBrandRepository extends CachingJdbcRepository<Brand> implements PageableRepository<Brand>, FieldSearchable<Brand> {
    private static final RowMapper<Brand> ROW_MAPPER = (resultSet, i) -> Brand.builder()
                                                                              .brandId(resultSet.getLong(Constants.RegistrationBrand.ID))
                                                                              .brandName(resultSet.getString(Constants.RegistrationBrand.NAME))
                                                                              .build();

    @Autowired
    public RegistrationBrandRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    CacheLoader<String, Brand> getCacheLoader() {
        return new CacheLoader<String, Brand>() {
            @Override
            public Brand load(@NonNull @Nonnull String key) {
                return findByField(key);
            }
        };
    }

    @Override
    public Brand findByField(@NonNull @Nonnull String fieldValue) {
        if (StringUtils.isBlank(fieldValue)) {
            return null;
        }
        String jdbcTemplateSelect = "select * from carinfo.brand where brand_name = ?;";
        return CrudRepository.getNullableResultIfException(() -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, fieldValue));
    }

    @Nullable
    @Override
    public Brand create(@NonNull @Nonnull Brand entity) {
        String jdbcTemplateInsert = "insert into carinfo.brand (brand_name) values (?);";
        jdbcTemplate.update(jdbcTemplateInsert, entity.getBrandName());
        return getFromCache(entity.getBrandName());
    }

    @Nullable
    @Override
    public Brand update(@NonNull @Nonnull Brand entity) {
        getCache().invalidate(entity.getBrandName());
        String jdbcTemplateUpdate = "update carinfo.brand set brand_name = ? where brand_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getBrandName(), entity.getBrandId());
        return findByField(entity.getBrandName());
    }

    @Override
    public boolean delete(@NonNull @Nonnull Long id) {
        Brand brand = find(id);
        if (nonNull(brand)) {
            getCache().invalidate(brand.getBrandName());
        }
        String jdbcTemplateDelete = "delete from carinfo.brand where brand_id = ?;";
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    @Nullable
    @Override
    public Brand find(@NonNull @Nonnull Long id) {
        String jdbcTemplateSelect = "select * from carinfo.brand where brand_id = ?;";
        return CrudRepository.getNullableResultIfException(
                () -> jdbcTemplate.queryForObject(jdbcTemplateSelect, ROW_MAPPER, id));
    }

    @Override
    public List<Brand> findAll() {
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
    public boolean isExists(@NonNull @Nonnull Brand entity) {
        return nonNull(getFromCache(entity.getBrandName()));
    }

    @Override
    public void createAll(Iterable<Brand> entities) {
        final int batchSize = 100;
        List<List<Brand>> batchLists = Lists.partition(Lists.newArrayList(entities), batchSize);
        for (List<Brand> batch : batchLists) {
            String jdbcTemplateInsertAll = "insert into carinfo.brand (brand_name) values (?);";
            jdbcTemplate.batchUpdate(jdbcTemplateInsertAll, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@Nonnull PreparedStatement ps, int i)
                        throws SQLException {
                    Brand object = batch.get(i);
                    ps.setString(1, object.getBrandName());
                }

                @Override
                public int getBatchSize() {
                    return batch.size();
                }
            });
        }
    }

    @Override
    public Page<Brand> find(@Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.brand b ";
        String name = searchParams.getString(Constants.RegistrationBrand.NAME);

        String where = buildWhere().add("b.brand_name", name).build();

        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, (rs, rowNum) -> rs.getInt(1));

        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Brand> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
