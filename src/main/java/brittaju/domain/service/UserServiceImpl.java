package brittaju.domain.service;

import brittaju.datasource.mapper.UserEntityMapper;
import brittaju.datasource.repository.UserRepository;
import brittaju.domain.model.User;
import brittaju.web.model.SignUpRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityMapper mapper;

    @Override
    @Transactional
    public UUID registration(SignUpRequest request) {
        if (userRepository.findByLogin(request.login()) != null) {
            throw new IllegalArgumentException("Invalid login");
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = new User(
                UUID.randomUUID(),
                request.login(),
                encodedPassword
        );
        userRepository.save(mapper.fromDomain(user));
        log.info("Новый пользователь успешно зарегистрирован");
        return user.id();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(userRepository.findByLogin(login))
                .map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository
                .findById(id)
                .map(mapper::toDomain);
    }
}