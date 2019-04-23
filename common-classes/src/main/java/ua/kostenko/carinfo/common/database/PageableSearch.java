package ua.kostenko.carinfo.common.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ua.kostenko.carinfo.common.api.ParamsHolder;

import javax.annotation.Nonnull;
import java.util.*;

public interface PageableSearch<T> {

    Page<T> find(@Nonnull @NonNull final ParamsHolder searchParams);

    default Page<T> getPage(JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper, Pageable pageable, String select, String from, String where, int total) {
        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<T> result = jdbcTemplate.query(querySql, rowMapper);
        return new PageImpl<>(result, pageable, total);
    }

    default WhereBuilder buildWhere() {
        return new WhereBuilder();
    }

    class WhereBuilder {
        private final Map<String, Object> fields;//TODO: change to <String, String>
        private final MapSqlParameterSource sqlParameters;

        @Data
        @AllArgsConstructor
        public class BuildResult {
            private String whereSql;
            private SqlParameterSource sqlParameters;
        }

        private WhereBuilder() {
            fields = new HashMap<>();
            sqlParameters = new MapSqlParameterSource();
        }

        public WhereBuilder addEq(@NonNull @Nonnull String fieldOne, @NonNull @Nonnull String fieldTwo) {
            if (StringUtils.isBlank(fieldOne) || StringUtils.isBlank(fieldTwo)) {
                return this;
            }
            fields.put(fieldOne, fieldTwo);
            return this;
        }

        public WhereBuilder addParam(@NonNull @Nonnull String fieldName, @NonNull @Nonnull String paramName, @Nonnull Object value) {
            if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(paramName) || Objects.isNull(value)) {
                return this;
            }
            fields.put(fieldName, String.format(":%s", paramName));
            sqlParameters.addValue(paramName, value);
            return this;
        }

        public BuildResult buildWhere() {
            StringJoiner stringJoiner = new StringJoiner(" and ", " where ", " ");
            stringJoiner.setEmptyValue("");
            fields.entrySet().stream()
                  .filter(stringObjectEntry -> Objects.nonNull(stringObjectEntry.getValue()))
                  .map(entry -> entry.getKey() + " = " + entry.getValue())
                  .forEach(stringJoiner::add);
            String sql = stringJoiner.toString();
            return new BuildResult(sql, this.sqlParameters);
        }

        @Deprecated
        public WhereBuilder add(@NonNull @Nonnull String key, Object value, boolean isValue) {
            if (isValue && Objects.nonNull(value)) {
                fields.put(key, String.format("'%s'", value.toString()));
            } else {
                fields.put(key, value);
            }
            return this;
        }

        @Deprecated
        public String build() {
            StringJoiner stringJoiner = new StringJoiner(" and ", " where ", " ");
            stringJoiner.setEmptyValue("");
            fields.entrySet().stream()
                  .filter(stringObjectEntry -> Objects.nonNull(stringObjectEntry.getValue()))
                  .map(entry -> entry.getKey() + " = " + entry.getValue())
                  .forEach(stringJoiner::add);
            return stringJoiner.toString();
        }
    }

}
