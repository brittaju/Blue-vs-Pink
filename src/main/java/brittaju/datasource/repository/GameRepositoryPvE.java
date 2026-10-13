package brittaju.datasource.repository;

import brittaju.datasource.model.GameEntityPvE;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepositoryPvE extends CrudRepository<GameEntityPvE, UUID> {}
