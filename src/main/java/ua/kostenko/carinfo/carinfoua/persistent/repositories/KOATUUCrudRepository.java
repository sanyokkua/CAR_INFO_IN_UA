package ua.kostenko.carinfo.carinfoua.persistent.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ua.kostenko.carinfo.carinfoua.data.KOATUU;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface KOATUUCrudRepository extends CrudRepository<KOATUU, Integer> {

  List<KOATUU> findAllByTypeName(String typeName);

  List<KOATUU> findAllByNameLike(String name);
}
