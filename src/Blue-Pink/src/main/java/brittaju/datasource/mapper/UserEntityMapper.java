package brittaju.datasource.mapper;

import brittaju.datasource.model.UserEntity;
import brittaju.domain.model.Role;
import brittaju.domain.model.User;

import java.util.List;

public final class UserEntityMapper {

    private UserEntityMapper() {}

    public static User toDomain(UserEntity user) {
        return new User(user.getId(), user.getLogin(), user.getPasswordHash(), user.getRoles());
    }

    public static UserEntity fromDomain(User user) {
        UserEntity entity = new UserEntity(user.id(), user.login(), user.passwordHash());
        entity.setRoles(user.roles() == null ? List.of(Role.USER) : user.roles());
        return entity;
    }
}