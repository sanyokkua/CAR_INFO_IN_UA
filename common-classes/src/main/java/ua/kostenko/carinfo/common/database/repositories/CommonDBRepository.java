package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ua.kostenko.carinfo.common.api.ParamsHolder;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;
import ua.kostenko.carinfo.common.api.records.GenericRecord;
import ua.kostenko.carinfo.common.database.Constants;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
abstract class CommonDBRepository<T extends GenericRecord<R>, R> implements DBRepository<T, R> {
    static final String ID_PARAM = "id";
    static final String NAME_PARAM = "name";
    private static final RowMapper<Integer> FIND_TOTAL_MAPPER = (rs, rowNum) -> rs.getInt(1);
    private static final RowMapper<Long> EXISTENCE_COUNT_MAPPER = (rs, rowNum) -> rs.getLong(1);
    private static final String LIMIT_PARAM = "lim";
    private static final String OFFSET_PARAM = "off";
    final NamedParameterJdbcTemplate jdbcTemplate;

    CommonDBRepository(@NonNull @Nonnull NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    T create(@NonNull @Nonnull String sql, String idFieldName, SqlParameterSource sqlParams) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, sqlParams, keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();
        if (Objects.nonNull(keys)) {
            Object id = keys.get(idFieldName);
            return findOne((long) id);
        } else {
            throw new NullPointerException("KeyHolder returned null instead of map with ids");
        }
    }

    boolean exist(@NonNull @Nonnull String sql, SqlParameterSource sqlParams) {
        long numberOfRows = jdbcTemplate.query(sql, sqlParams, EXISTENCE_COUNT_MAPPER).stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    boolean delete(@NonNull @Nonnull String jdbcTemplateDelete, SqlParameterSource sqlParams) {
        int updated = jdbcTemplate.update(jdbcTemplateDelete, sqlParams);
        return updated > 0;
    }

    List<T> find(@NonNull @Nonnull String jdbcTemplateSelect) {
        return jdbcTemplate.query(jdbcTemplateSelect, getRowMapper());
    }

    abstract RowMapper<T> getRowMapper();

    T findOne(@NonNull @Nonnull String sql, SqlParameterSource sqlParams) {
        try {
            List<T> list = jdbcTemplate.query(sql, sqlParams, getRowMapper());
            return list.stream().findFirst().orElse(null);
        } catch (Exception ex) {
            log.warn("Exception occurred due extracting value.", ex);
        }
        return null;

    }

    Page<T> findPage(@NonNull @Nonnull ParamsHolder paramsHolder, @NonNull @Nonnull String selectSql, @NonNull @Nonnull String fromSql, @NonNull @Nonnull WhereBuilder whereBuilder) {
        if (StringUtils.isBlank(selectSql) || StringUtils.isBlank(fromSql)) {
            throw new IllegalArgumentException("Select or From values can't be blank");
        }
        Pageable page = paramsHolder.getPage();
        int limit = page.getPageSize();
        long offset = page.getOffset();
        whereBuilder.addOnlyParam(LIMIT_PARAM, limit).addOnlyParam(OFFSET_PARAM, offset);

        WhereBuilder.BuildResult buildResult = whereBuilder.build();
        String whereSql = buildResult.getWhereSql();
        SqlParameterSource sqlParameters = buildResult.getSqlParameters();

        String countQuery = String.format("select count(1) as row_count %s %s", fromSql, whereSql);
        int total = jdbcTemplate.query(countQuery, sqlParameters, FIND_TOTAL_MAPPER).stream().findFirst().orElse(0);

        String querySql = String.format("%s %s %s limit :lim offset :off", selectSql, fromSql, whereSql);
        List<T> result = jdbcTemplate.query(querySql, sqlParameters, getRowMapper());
        return new PageImpl<>(result, page, total);
    }

    ParamsHolderBuilder getParamsHolderBuilder() {
        return new ParamsHolderBuilder();
    }

    SqlParameterMap getSqlParamBuilder() {
        return SqlParameterMap.getBuilder();
    }

    @Override
    public int countAll() {
        String countQuery = String.format("select count(1) as row_count from %s", formatTableNameWithSchema(getTableName()));
        return jdbcTemplate.query(countQuery, FIND_TOTAL_MAPPER).stream().findFirst().orElse(0);
    }

    @Override
    public int countAll(@Nonnull @NonNull ParamsHolder searchParams) {
        WhereBuilder.BuildResult result = getWhereFromParams(searchParams);
        String countQuery = String.format("select count(1) as row_count from %s %s", formatTableNameWithSchema(getTableName()), result.getWhereSql());
        return jdbcTemplate.query(countQuery, result.getSqlParameters(), FIND_TOTAL_MAPPER).stream().findFirst().orElse(0);
    }

    abstract WhereBuilder.BuildResult getWhereFromParams(ParamsHolder params);

    private String formatTableNameWithSchema(@NonNull @Nonnull String tableName) {
        return String.format("%s.%s", Constants.SCHEMA, tableName);
    }

    abstract String getTableName();
}
