package com.oskin.ad_board.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oskin.ad_board.exception.CustomExceptionResponse;
import com.oskin.ad_board.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final List<String> permitAllPaths = Arrays.asList(
            "/auth",
            "/reg"
    );

    @Autowired
    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();
        if (method.equals("GET") && (path.startsWith("/ad") || path.startsWith("/profile") || path.startsWith("/review"))) {
            return true;
        }
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/webjars")) {
            return true;
        }
        return permitAllPaths.stream().anyMatch(path::equals);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String id;
        String jwt;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                id = jwtUtils.getId(jwt);
            } catch (MalformedJwtException | SignatureException e) {
                response.setStatus(401);
                response.setContentType("application/json");
                CustomExceptionResponse exception = new CustomExceptionResponse(401, "invalid token");
                response.getWriter().write(mapper.writeValueAsString(exception));
                return;
            } catch (ExpiredJwtException e) {
                response.setStatus(401);
                response.setContentType("application/json");
                CustomExceptionResponse exception = new CustomExceptionResponse(401, "the jwt token's lifetime has expired");
                response.getWriter().write(mapper.writeValueAsString(exception));
                return;
            }
        } else {
            response.setStatus(401);
            response.setContentType("application/json");
            CustomExceptionResponse exception = new CustomExceptionResponse(401, "user must log in or register");
            response.getWriter().write(mapper.writeValueAsString(exception));
            return;
        }
        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    id,
                    null,
                    jwtUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).toList()
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }
}
