package brittaju.security;

import brittaju.domain.model.Role;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProvider provider;

    public JwtAuthentication createAuthentication(Claims claims) {
        UUID userId = provider.getUserId(claims);
        List<Role> roles = provider.getRoles(claims).stream()
                .map(Role::valueOf)
                .toList();
        return new JwtAuthentication(
                userId,
                roles,
                true
        );
    }
}
