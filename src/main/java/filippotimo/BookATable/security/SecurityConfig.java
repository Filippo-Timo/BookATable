package filippotimo.BookATable.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JWTCheckerFilter jwtCheckerFilter) {

        // TODO: 1) RIMUOVO IL LOGIN CHE C'E' DI DEFAULT

        httpSecurity.formLogin(formLogin -> formLogin.disable());

        // TODO: 2) RIMUOVO IL CSRF

        httpSecurity.csrf(csrf -> csrf.disable());

        // TODO: 3) ABILITO IL CORS
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // TODO: 4) CAMBIO IL MECCANISMO A SESSIONI

        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // TODO: 5) DISABILITO LA PROTEZIONE SU TUTTI GLI ENDPOINT

        httpSecurity.authorizeHttpRequests(req -> req
                .requestMatchers(
                        "/api/auth/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/v3/api-docs.yaml"
                ).permitAll()
                .anyRequest().authenticated()  // ← autenticazione richiesta ovunque
        );
        // TODO: 6) AGGIUNGO IL FILTRO JWT
        httpSecurity.addFilterBefore(jwtCheckerFilter,
                UsernamePasswordAuthenticationFilter.class);

        // TODO: 7) INFINE CREO L'OGGETTO CHE SERVE A SPRING SECURITY PER APPLICARE QUESTE IMPOSTAZIONI

        return httpSecurity.build();

    }


    // ******************************** Bcrypt ********************************
//  Tramite questo @Bean configuro BCrypt per la protezione delle password

    @Bean
    public PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(13);
    }


    // ******************************** Cors ********************************

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
