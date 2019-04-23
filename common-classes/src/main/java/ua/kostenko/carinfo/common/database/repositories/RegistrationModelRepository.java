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
import ua.kostenko.carinfo.common.api.records.Model;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Repository
@Slf4j
class RegistrationModelRepository extends CommonDBRepository<Model> {
    private static final RowMapper<Model> ROW_MAPPER = (resultSet, i) -> Model.builder()
                                                                              .modelId(resultSet.getLong(Constants.RegistrationModel.ID))
                                                                              .modelName(resultSet.getString(Constants.RegistrationModel.NAME))
                                                                              .build();

    @Autowired
    public RegistrationModelRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    RowMapper<Model> getRowMapper() {
        return ROW_MAPPER;
    }

    @Nullable
    @Override
    public Model create(@NonNull @Nonnull Model entity) {
        String jdbcTemplateInsert = "insert into carinfo.model (model_name) values (?);";
        return create(jdbcTemplateInsert, Constants.RegistrationModel.ID, entity.getModelName());
    }

    @Nullable
    @Override
    public Model update(@NonNull @Nonnull Model entity) {
        String jdbcTemplateUpdate = "update carinfo.model set model_name = ? where model_id = ?;";
        jdbcTemplate.update(jdbcTemplateUpdate, entity.getModelName(), entity.getModelId());
        ParamsHolder searchParams = getParamsHolderBuilder().param(Model.MODEL_NAME, entity.getModelName()).build();
        return findOne(searchParams);
    }

    @Override
    public boolean delete(long id) {
        String jdbcTemplateDelete = "delete from carinfo.model where model_id = ?;";
        return delete(jdbcTemplateDelete, id);
    }

    @Override
    public boolean existId(long id) {
        String jdbcTemplateSelectCount = "select count(model_id) from carinfo.model where model_id = ?;";
        return exist(jdbcTemplateSelectCount, id);
    }

    @Override
    public boolean exist(@NonNull @Nonnull Model entity) {
        String jdbcTemplateSelectCount = "select count(model_id) from carinfo.model where model_name = ?;";
        return exist(jdbcTemplateSelectCount, entity.getModelName());
    }

    @Nullable
    @Override
    public Model findOne(long id) {
        String jdbcTemplateSelect = "select * from carinfo.model where model_id = ?;";
        return findOne(jdbcTemplateSelect, id);
    }

    @Cacheable(cacheNames = "model", unless = "#result != null")
    @Nullable
    @Override
    public Model findOne(@NonNull @Nonnull ParamsHolder searchParams) {
        String param = searchParams.getString(Model.MODEL_NAME);
        String jdbcTemplateSelect = "select * from carinfo.model where model_name = ?;";
        return findOne(jdbcTemplateSelect, param);
    }

    @Override
    public List<Model> find() {
        String jdbcTemplateSelect = "select * from carinfo.model;";
        return find(jdbcTemplateSelect);
    }

    @Override
    public Page<Model> find(@NonNull @Nonnull ParamsHolder searchParams) {
        Pageable pageable = searchParams.getPage();
        String select = "select * from carinfo.model m ";
        String name = searchParams.getString(Model.MODEL_NAME);

        String where = buildWhere().add("m.model_name", name, true).build();

        String countQuery = "select count(1) as row_count " + "from carinfo.model m " + where;
        int total = jdbcTemplate.queryForObject(countQuery, FIND_TOTAL_MAPPER);

        String querySql = select + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<Model> result = jdbcTemplate.query(querySql, ROW_MAPPER);
        return new PageImpl<>(result, pageable, total);
    }
}
