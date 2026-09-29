package brittaju.web.controller;

import brittaju.domain.service.AuthService;
import brittaju.domain.service.UserService;
import brittaju.security.JwtAuthentication;
import brittaju.web.model.JwtRequest;
import brittaju.web.model.JwtResponse;
import brittaju.web.model.RefreshJwtRequest;
import brittaju.web.model.SignUpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, UUID>> register(@RequestBody SignUpRequest request) {
        UUID id = userService.registration(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("userId", id));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/token")
    public ResponseEntity<JwtResponse> refreshAccess(@RequestBody RefreshJwtRequest request) {
        return ResponseEntity.ok(authService.refreshAccessToken(request.refreshToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshRefresh(@RequestBody RefreshJwtRequest request) {
        return ResponseEntity.ok(authService.refreshRefreshToken(request.refreshToken()));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me() {
        JwtAuthentication auth = authService.getAuthentication();
        return ResponseEntity.ok(Map.of(
                "userId", auth.getPrincipal().toString(),
                "roles", auth.getAuthorities().stream().map(Object::toString).toList()
        ));
    }

}
