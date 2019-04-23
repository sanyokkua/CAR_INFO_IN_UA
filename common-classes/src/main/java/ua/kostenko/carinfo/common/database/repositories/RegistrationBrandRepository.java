package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.records.Brand;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class RegistrationBrandRepository extends CommonDBRepository<Brand> {
    private static final RowMapper<Brand> ROW_MAPPER = (resultSet, i) -> Brand.builder()
                                                                              .brandId(resultSet.getLong(Constants.RegistrationBrand.ID))
                                                                              .brandName(resultSet.getString(Constants.RegistrationBrand.NAME))
                                                                              .build();

    @Autowired
    public RegistrationBrandRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Brand> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public Brand create(@NonNull @Nonnull Brand entity) {
        String jdbcTemplateInsert = "insert into carinfo.brand (brand_name) values (?);";
        return create(jdbcTemplateInsert, Constants.RegistrationBrand.ID, entity.getBrandName());
    }

    @Nullable
    @Override
    public Brand update(@NonNull @Nonnull Brand entity) {
        String jdbcTemplateUpdate = "update carinfo.brand set brand_name = ? where brand_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getBrandName(), entity.getBrandId());
        ParamsHolder holder = getParamsHolderBuilder().param(Brand.BRAND_NAME, entity.getBrandName()).build();
        return findOne(holder);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.brand where brand_id = ?;";
        return delete(jdbcTemplateDelete, id);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(brand_id) from carinfo.brand where brand_id = ?;";
        return exist(jdbcTemplateSelectCount, id);
    }

    @Override
    public boolean exist(@NonNull @Nonnull Brand entity) {
        String jdbcTemplateSelectCount = "select count(brand_id) from carinfo.brand where brand_name = ?;";
        return exist(jdbcTemplateSelectCount, entity.getBrandName());
    }

    @Cacheable(cacheNames = "brand", unless = "#result != null")
    @Nullable
    @Override
    public Brand findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.brand where brand_id = ?;";
        return findOne(jdbcTemplateSelect, id);
    }

    @Nullable
    @Override
    public Brand findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Brand.BRAND_NAME);
        String jdbcTemplateSelect = "select * from carinfo.brand where brand_name = ?;";
        return findOne(jdbcTemplateSelect, param);
    }

    @Override
    public List<Brand> find() {
        String jdbcTemplateSelect = "select * from carinfo.brand;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<Brand> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.brand b ";
        String name = searchParams.getString(Brand.BRAND_NAME);

        String where = buildWhere().add("b.brand_name", name, true).build();

        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, FIND_TOTAL_MAPPER);

        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Brand> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
