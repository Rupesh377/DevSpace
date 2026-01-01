package com.rupesh.DevSpace.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final CustomUserDetailService userDetailService;

    public JwtAuthFilter(AuthUtil authUtil, CustomUserDetailService userDetailService) {
        this.authUtil = authUtil;
        this.userDetailService = userDetailService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader=request.getHeader("Authorization");

        if(authHeader== null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request , response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String email = authUtil.extractEmail(jwt);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailService.loadUserByUsername(email);

            if (authUtil.isTokenValid(jwt, userDetails.getUsername()))
            {
                var authorities = userDetails.getAuthorities().stream()
                        .map(auth -> {
                            if (auth.getAuthority().startsWith("ROLE_")) {
                                return auth;
                            }
                            return new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                    "ROLE_" + auth.getAuthority());}).toList();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
