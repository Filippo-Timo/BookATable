package filippotimo.BookATable.security;

import filippotimo.BookATable.entities.GenericUser;
import filippotimo.BookATable.exceptions.UnauthorizedException;
import filippotimo.BookATable.services.GenericUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class JWTCheckerFilter extends OncePerRequestFilter {

    private final JWTTools jwtTools;
    private final GenericUserService userService;

    @Autowired
    public JWTCheckerFilter(JWTTools jwtTools, GenericUserService userService) {
        this.jwtTools = jwtTools;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1) VERIFICO L'AUTHORIZATION HEADER
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Token mancante o in formato errato!");

        // 2) ESTRAGGO IL TOKEN
        String accessToken = authHeader.replace("Bearer ", "");

        // 3) VERIFICO IL TOKEN
        jwtTools.verifyToken(accessToken);

        // 4) ESTRAGGO L'ID DAL TOKEN
        UUID userId = jwtTools.extractIdFromToken(accessToken);

        // 5) RECUPERO L'UTENTE DAL DB
        GenericUser user = userService.findById(userId);

        // 6) ASSOCIO L'UTENTE AL SECURITY CONTEXT
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 7) PASSO AL PROSSIMO FILTRO
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/api/auth/**", request.getServletPath());
    }

}
