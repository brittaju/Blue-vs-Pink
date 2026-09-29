package brittaju.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class AuthFilter extends GenericFilterBean {

    private final JwtProvider jwtProvider;
    private final JwtUtil jwtUtil;

    public AuthFilter(JwtProvider jwtProvider, JwtUtil jwtUtil) {
        this.jwtProvider = jwtProvider;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI();

        boolean isPublicPath =
                path.equals("/") ||
                        path.startsWith("/index.html") ||
                        path.startsWith("/static") ||
                        path.startsWith("/css") ||
                        path.startsWith("/js") ||
                        path.equals("/image.jpg") ||
                        path.equals("/favicon.ico") ||
                        (path.startsWith("/auth/") && !path.equals("/auth/me"));

        if (isPublicPath) {
            chain.doFilter(req, res);
            return;
        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Unauthorized\"}");
            return;
        }

        String token = header.substring("Bearer ".length());
        if (!jwtProvider.validateAccessToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Invalid token\"}");
            return;
        }

        Claims claims = jwtProvider.getClaims(token);
        JwtAuthentication authentication = jwtUtil.createAuthentication(claims);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(req, res);
        SecurityContextHolder.clearContext();
    }

}

