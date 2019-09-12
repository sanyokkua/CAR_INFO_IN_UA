package ua.kostenko.carinfo.common.database.repositories;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

class SqlParameterMap {

    private final MapSqlParameterSource params;

    private SqlParameterMap() {
        params = new MapSqlParameterSource();
    }

    static SqlParameterMap getBuilder() {
        return new SqlParameterMap();
    }

    SqlParameterMap addParam(String name, Object value) {
        this.params.addValue(name, value);
        return this;
    }

    SqlParameterSource build() {
        return this.params;
    }
}
