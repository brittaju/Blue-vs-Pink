package brittaju.domain.service;

import brittaju.datasource.mapper.UserEntityMapper;
import brittaju.datasource.repository.UserRepository;
import brittaju.domain.model.User;
import brittaju.web.model.SignUpRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UUID registration(SignUpRequest request) {
        if (userRepository.findByLogin(request.login()) != null) {
            throw new IllegalArgumentException("Invalid login");
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = new User(UUID.randomUUID(), request.login(), encodedPassword);
        userRepository.save(UserEntityMapper.fromDomain(user));
        log.info("Новый пользователь успешно зарегистрирован");
        return user.id();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.ofNullable(userRepository.findByLogin(login))
                .map(UserEntityMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id).map(UserEntityMapper::toDomain);
    }

}