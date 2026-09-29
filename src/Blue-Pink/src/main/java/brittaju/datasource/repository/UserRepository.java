package brittaju.datasource.repository;

import brittaju.datasource.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, UUID> {

     UserEntity findByLogin(String login);

}