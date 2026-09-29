package brittaju.domain.service;

import brittaju.domain.model.User;
import brittaju.security.JwtAuthentication;
import brittaju.security.JwtProvider;
import brittaju.web.model.JwtRequest;
import brittaju.web.model.JwtResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtResponse login(JwtRequest request) {
        User user = userService.findByLogin(request.login())
                .orElseThrow(() -> new BadCredentialsException("Invalid login or password"));
        if (!passwordEncoder.matches(request.password(), user.passwordHash())) {
            throw new BadCredentialsException("Invalid login or password");
        }
        return new JwtResponse(
                jwtProvider.generateAccessToken(user),
                jwtProvider.generateRefreshToken(user)
        );
    }

    public JwtResponse refreshAccessToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        var claims = jwtProvider.getClaims(refreshToken);
        User user = userService.findById(jwtProvider.getUserId(claims))
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        return new JwtResponse(jwtProvider.generateAccessToken(user), refreshToken);
    }

    public JwtResponse refreshRefreshToken(String refreshToken) {
        if (!jwtProvider.validateRefreshToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        var claims = jwtProvider.getClaims(refreshToken);
        User user = userService.findById(jwtProvider.getUserId(claims))
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        return new JwtResponse(
                jwtProvider.generateAccessToken(user),
                jwtProvider.generateRefreshToken(user)
        );
    }

    public JwtAuthentication getAuthentication() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
