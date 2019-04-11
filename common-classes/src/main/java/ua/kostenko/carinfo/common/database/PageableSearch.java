package ua.kostenko.carinfo.common.database;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
        private final Map<String, Object> values;

        private WhereBuilder() {
            values = new HashMap<>();
        }

        public WhereBuilder add(@NonNull @Nonnull String key, Object value) {
            values.put(key, value);
            return this;
        }

        public String build() {
            StringJoiner stringJoiner = new StringJoiner(" and ", " where ", " ");
            stringJoiner.setEmptyValue("");
            values.entrySet().stream()
                  .filter(stringObjectEntry -> Objects.nonNull(stringObjectEntry.getValue()))
                  .map(entry -> entry.getKey() + " = " + entry.getValue())
                  .forEach(stringJoiner::add);
            return stringJoiner.toString();
        }
    }

}
