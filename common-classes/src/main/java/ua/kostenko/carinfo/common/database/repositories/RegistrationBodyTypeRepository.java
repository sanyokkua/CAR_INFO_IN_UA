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
import ua.kostenko.carinfo.common.api.records.BodyType;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class RegistrationBodyTypeRepository extends CommonDBRepository<BodyType> {
    private static final RowMapper<BodyType> ROW_MAPPER = (resultSet, i) -> BodyType.builder()
                                                                                    .bodyTypeId(resultSet.getLong(Constants.RegistrationBodyType.ID))
                                                                                    .bodyTypeName(resultSet.getString(Constants.RegistrationBodyType.NAME))
                                                                                    .build();

    @Autowired
    public RegistrationBodyTypeRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<BodyType> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public BodyType create(@NonNull @Nonnull BodyType entity) {
        String jdbcTemplateInsert = "insert into carinfo.body_type (body_type_name) values (?);";
        return create(jdbcTemplateInsert, Constants.RegistrationBodyType.ID, entity.getBodyTypeName());
    }

    @Nullable
    @Override
    public BodyType update(@NonNull @Nonnull BodyType entity) {
        String jdbcTemplateUpdate = "update carinfo.body_type set body_type_name = ? where body_type_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getBodyTypeName(), entity.getBodyTypeId());
        ParamsHolder holder = getParamsHolderBuilder().param(BodyType.BODY_TYPE_NAME, entity.getBodyTypeName()).build();
        return findOne(holder);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.body_type where body_type_id = ?;";
        return delete(jdbcTemplateDelete, id);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(body_type_id) from carinfo.body_type where body_type_id = ?;";
        return exist(jdbcTemplateSelectCount, id);
    }

    @Override
    public boolean exist(@NonNull @Nonnull BodyType entity) {
        String jdbcTemplateSelectCount = "select count(body_type_id) from carinfo.body_type where body_type_name = ?;";
        return exist(jdbcTemplateSelectCount, entity.getBodyTypeName());
    }

    @Nullable
    @Override
    public BodyType findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_id = ?;";
        return findOne(jdbcTemplateSelect, id);
    }

    @Cacheable(cacheNames = "bodyType", unless = "#result != null")
    @Nullable
    @Override
    public BodyType findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(BodyType.BODY_TYPE_NAME);
        String jdbcTemplateSelect = "select * from carinfo.body_type where body_type_name = ?;";
        return findOne(jdbcTemplateSelect, param);
    }

    @Override
    public List<BodyType> find() {
        String jdbcTemplateSelect = "select * from carinfo.body_type;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<BodyType> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * ";
        String from = "from carinfo.body_type bt ";
        String name = searchParams.getString(BodyType.BODY_TYPE_NAME);

        String where = buildWhere().add("bt.body_type_name", name, true).build();

        String countQuery = "select count(1) as row_count " + from + where;
        int total = jdbcTemplate.queryForObject(countQuery, FIND_TOTAL_MAPPER);

        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<BodyType> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
