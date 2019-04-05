package ua.kostenko.carinfo.common.database;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ua.kostenko.carinfo.common.ParamsHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public interface PageableSearch<T> {
    int DEFAULT_PAGE_SIZE = 10;

    Page<T> find(@Nonnull @NonNull ParamsHolder searchParams);

    default int getPage(@Nullable Integer uiPageNumber) {
        int page = Objects.nonNull(uiPageNumber) && uiPageNumber >= 0 ? uiPageNumber : 0;
        return page > 0 ? page - 1 : page;
    }

    default int getAmount(@Nullable Integer recordsPerPage) {
        return Objects.nonNull(recordsPerPage) && recordsPerPage > 0 ? recordsPerPage : DEFAULT_PAGE_SIZE;
    }

    default Page<T> getPage(JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper, Pageable pageable, String select, String from, String where, int total) {
        String querySql = select + from + where + " limit " + pageable.getPageSize() + " offset " + pageable.getOffset();
        List<T> result = jdbcTemplate.query(querySql, rowMapper);
        return new PageImpl<>(result, pageable, total);
    }
}
