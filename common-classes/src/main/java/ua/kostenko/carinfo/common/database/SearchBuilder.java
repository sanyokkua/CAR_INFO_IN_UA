package ua.kostenko.carinfo.common.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBuilder<T> {
    SearchBuilder<T> begin(Pageable pageable);
    Page<T> invoke();
}
