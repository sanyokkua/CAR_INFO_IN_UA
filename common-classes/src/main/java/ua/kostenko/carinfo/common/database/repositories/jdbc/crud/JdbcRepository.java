package ua.kostenko.carinfo.common.database.repositories.jdbc.crud;

import lombok.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.kostenko.carinfo.common.database.repositories.CrudRepository;

import javax.annotation.Nonnull;

abstract class JdbcRepository<T> implements CrudRepository<T> {
    final JdbcTemplate jdbcTemplate;

    protected JdbcRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
