package com.rupesh.DevSpace.Security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@Slf4j
public class WebConfigSecurity {

    private final JwtAuthFilter authFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public WebConfigSecurity(JwtAuthFilter authFilter, OAuth2SuccessHandler oAuth2SuccessHandler, HandlerExceptionResolver handlerExceptionResolver) {
        this.authFilter = authFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable())
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/admin/**" , "/Admin/**").hasRole("ADMIN")
                        .requestMatchers("/wishlist/**").hasAnyRole("ADMIN", "USER")

                        .anyRequest().authenticated())

                .addFilterBefore(
                        authFilter,
                        UsernamePasswordAuthenticationFilter.class)

                .oauth2Login(oAuth2-> oAuth2.failureHandler(
                                (request, response, exception) -> {
                                    handlerExceptionResolver.resolveException(request , response , null , exception);
                                })
                        .successHandler(oAuth2SuccessHandler));
        return httpSecurity.build();
    }

}
