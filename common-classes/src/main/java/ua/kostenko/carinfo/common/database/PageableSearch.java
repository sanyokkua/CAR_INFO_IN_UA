package ua.kostenko.carinfo.common.database;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import ua.kostenko.carinfo.common.api.ParamsHolder;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public interface PageableSearch<T> {

    Page<T> find(@Nonnull @NonNull final ParamsHolder searchParams);

    default WhereBuilder buildWhere() {
        return new WhereBuilder();
    }

    class WhereBuilder {
        private final Map<String, String> fields;
        private final MapSqlParameterSource sqlParameters;

        private WhereBuilder() {
            fields = new HashMap<>();
            sqlParameters = new MapSqlParameterSource();
        }

        public WhereBuilder addFieldParam(@NonNull @Nonnull String fieldName, @NonNull @Nonnull String paramName, Object value) {
            if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(paramName) || Objects.isNull(value)) {
                return this;
            }
            addEqualFields(fieldName, String.format(":%s", paramName));
            sqlParameters.addValue(paramName, value);
            return this;
        }

        public WhereBuilder addEqualFields(@NonNull @Nonnull String mainField, String equalField) {
            if (StringUtils.isBlank(mainField) || StringUtils.isBlank(equalField)) {
                return this;
            }
            fields.put(mainField, equalField);
            return this;
        }

        public WhereBuilder addOnlyParam(@NonNull @Nonnull String paramName, Object value) {
            if (StringUtils.isBlank(paramName) || Objects.isNull(value)) {
                return this;
            }
            sqlParameters.addValue(paramName, value);
            return this;
        }

        public BuildResult build() {
            StringJoiner stringJoiner = new StringJoiner(" and ", " where ", " ");
            stringJoiner.setEmptyValue("");
            fields.entrySet().stream()
                  .filter(stringObjectEntry -> Objects.nonNull(stringObjectEntry.getValue()))
                  .map(entry -> entry.getKey() + " = " + entry.getValue())
                  .forEach(stringJoiner::add);
            String sql = stringJoiner.toString();
            return new BuildResult(sql, this.sqlParameters);
        }

        @Getter(AccessLevel.PUBLIC)
        @Setter(AccessLevel.PRIVATE)
        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        public class BuildResult {
            private String whereSql;
            private SqlParameterSource sqlParameters;
        }
    }

}
