package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ua.kostenko.carinfo.common.Utils;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;

import javax.annotation.Nonnull;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
abstract class CommonDBRepository<T> implements DBRepository<T> {
    static final RowMapper<Integer> FIND_TOTAL_MAPPER = (rs, rowNum) -> rs.getInt(1);
    private static final RowMapper<Long> EXISTENCE_COUNT_MAPPER = (rs, rowNum) -> rs.getLong(1);
    final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommonDBRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected ParamsHolderBuilder getParamsHolderBuilder() {
        return new ParamsHolderBuilder();
    }

    boolean exist(@NonNull @Nonnull String sql, Object... args) {
        long numberOfRows = jdbcTemplate.query(sql, args, EXISTENCE_COUNT_MAPPER).stream().findFirst().orElse(0L);
        return numberOfRows > 0;
    }

    T findOne(@NonNull @Nonnull String sql, Object... args) {
        return Utils.getResultOrWrapExceptionToNull(() -> {
            List<T> list = jdbcTemplate.query(sql, args, getRowMapper());
            return list.stream().findFirst().orElse(null);
        });
    }

    abstract RowMapper<T> getRowMapper();

    T create(@NonNull @Nonnull String sql, String idFieldName, Object... args) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int numberOfPlaceholders = StringUtils.countMatches(sql, "?");
        if (numberOfPlaceholders != args.length) {
            throw new IllegalArgumentException("Number of placeholders in sql query is not equal to number of arguments. Number of placeholders = "
                                                       + numberOfPlaceholders + " and number of arguments = " + args.length);
        }
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < args.length; i++) {
                Object argument = args[i];
                if (argument instanceof Long) {
                    long value = (long) argument;
                    statement.setLong(i + 1, value);
                } else if (argument instanceof Integer) {
                    int value = (int) argument;
                    statement.setInt(i + 1, value);
                } else if (argument instanceof String) {
                    String value = (String) argument;
                    statement.setString(i + 1, value);
                } else if (argument instanceof Date) {
                    Date value = (Date) argument;
                    statement.setDate(i + 1, value);
                } else if (Objects.isNull(argument)) {
                    statement.setNull(i + 1, Types.NULL);
                } else {
                    log.error("Param with type {} is not supported", argument.getClass().getSimpleName());
                    throw new IllegalArgumentException("Param with type " + argument.getClass().getSimpleName() + " is not supported");
                }
            }
            return statement;
        }, keyHolder);
        Map<String, Object> keys = keyHolder.getKeys();
        if (Objects.nonNull(keys)) {
            Object id = keys.get(idFieldName);
            return findOne((long) id);
        } else {
            throw new NullPointerException("KeyHolder returned null instead of map with ids");
        }
    }

    boolean delete(@NonNull @Nonnull String jdbcTemplateDelete, long id) {
        int updated = jdbcTemplate.update(jdbcTemplateDelete, id);
        return updated > 0;
    }

    List<T> find(@NonNull @Nonnull String jdbcTemplateSelect) {
        return jdbcTemplate.query(jdbcTemplateSelect, getRowMapper());
    }
}
