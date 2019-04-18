package ua.kostenko.carinfo.common.database.repositories;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.kostenko.carinfo.common.api.ParamsHolderBuilder;

import javax.annotation.Nonnull;

abstract class CommonDBRepository<T> implements DBRepository<T> {
    final JdbcTemplate jdbcTemplate;

    @Autowired
    public CommonDBRepository(@NonNull @Nonnull JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    protected ParamsHolderBuilder getBuilder() {
        return new ParamsHolderBuilder();
    }
}
