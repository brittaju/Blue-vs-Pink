package brittaju.datasource.mapper;

import brittaju.datasource.model.UserEntity;
import brittaju.domain.model.Role;
import brittaju.domain.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class UserEntityMapper {

    public User toDomain(UserEntity user) {
        return new User(
                user.getId(),
                user.getLogin(),
                user.getPasswordHash(),
                user.getRoles()
        );
    }

    public UserEntity fromDomain(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.passwordHash(),
                user.roles() == null
                        ? List.of(Role.USER)
                        : user.roles()
        );
    }
}