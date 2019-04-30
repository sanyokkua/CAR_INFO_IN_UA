package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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
    public RegistrationBrandRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Brand> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public Brand create(@NonNull @Nonnull Brand entity) {
        String jdbcTemplateInsert = "insert into carinfo.brand (brand_name) values (:name);";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getBrandName()).build();
        return create(jdbcTemplateInsert, Constants.RegistrationBrand.ID, parameterSource);
    }

    @Nullable
    @Override
    public Brand update(@NonNull @Nonnull Brand entity) {
        String jdbcTemplateUpdate = "update carinfo.brand set brand_name = :name where brand_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder()
                .addParam("name", entity.getBrandName())
                .addParam("id", entity.getBrandId())
                .build();
        jdbcTemplate.update(jdbcTemplateUpdate, parameterSource);
        ParamsHolder holder = getParamsHolderBuilder().param(Brand.BRAND_NAME, entity.getBrandName()).build();
        return findOne(holder);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.brand where brand_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return delete(jdbcTemplateDelete, params);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(brand_id) from carinfo.brand where brand_id = :id;";
        SqlParameterSource params = getSqlParamBuilder().addParam("id", id).build();
        return exist(jdbcTemplateSelectCount, params);
    }

    @Cacheable(cacheNames = "brandCheck", unless = "#result == false ", key = "#entity.hashCode()")
    @Override
    public boolean exist(@NonNull @Nonnull Brand entity) {
        String jdbcTemplateSelectCount = "select count(brand_id) from carinfo.brand where brand_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", entity.getBrandName()).build();
        return exist(jdbcTemplateSelectCount, parameterSource);
    }

    @Nullable
    @Override
    public Brand findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.brand where brand_id = :id;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("id", id).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Cacheable(cacheNames = "brand", unless = "#result == null", key = "#searchParams.hashCode()")
    @Nullable
    @Override
    public Brand findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Brand.BRAND_NAME);
        String jdbcTemplateSelect = "select * from carinfo.brand where brand_name = :name;";
        SqlParameterSource parameterSource = getSqlParamBuilder().addParam("name", param).build();
        return findOne(jdbcTemplateSelect, parameterSource);
    }

    @Override
    public List<Brand> find() {
        String jdbcTemplateSelect = "select * from carinfo.brand;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<Brand> find(@NonNull @Nonnull ParamsHolder searchParams) {
        String select = "select * ";
        String from = "from carinfo.brand b ";
        String name = searchParams.getString(Brand.BRAND_NAME);
        return findPage(searchParams, select, from, buildWhere().addFieldParam("b.brand_name", "name", name));

    }
}
