package filippotimo.BookATable.security;

import org.springframework.beans.factory.annotation.Autowired;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JWTCheckerFilter jwtCheckerFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

        // TODO: 1) RIMUOVO IL LOGIN CHE C'E' DI DEFAULT

        httpSecurity.formLogin(formLogin -> formLogin.disable());

        // TODO: 2) RIMUOVO IL CSRF

        httpSecurity.csrf(csrf -> csrf.disable());

        // TODO: 3) CAMBIO IL MECCANISMO A SESSIONI

        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // TODO: 4) DISABILITO LA PROTEZIONE SU TUTTI GLI ENDPOINT

        httpSecurity.authorizeHttpRequests(req -> req
                        .requestMatchers(
                                "/**", // /api/auth
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()
                // .anyRequest().authenticated()  // ← autenticazione richiesta ovunque
        );
        // TODO: 5) AGGIUNGO IL FILTRO JWT
        httpSecurity.addFilterBefore(jwtCheckerFilter,
                UsernamePasswordAuthenticationFilter.class);

        // TODO: 6) INFINE CREO L'OGGETTO CHE SERVE A SPRING SECURITY PER APPLICARE QUESTE IMPOSTAZIONI

        return httpSecurity.build();

    }

    // ******************************** Bcrypt ********************************
//  Tramite questo @Bean configuro BCrypt per la protezione delle password

    @Bean
    public PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(13);
    }


}
