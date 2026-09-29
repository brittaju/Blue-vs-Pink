package brittaju.domain.model;

import java.util.List;
import java.util.UUID;

public record User(UUID id, String login, String passwordHash, List<Role> roles) {

    public User(UUID id, String login, String passwordHash) {
        this(id, login, passwordHash, List.of(Role.USER));
    }

}
