package brittaju.domain.service;

import brittaju.domain.model.User;
import brittaju.web.model.SignUpRequest;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UUID registration(SignUpRequest request);

    Optional<User> findByLogin(String login);

    Optional<User> findById(UUID id);

}
