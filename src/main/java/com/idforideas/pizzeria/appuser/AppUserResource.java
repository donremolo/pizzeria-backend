package com.idforideas.pizzeria.appuser;

import static java.util.Map.of;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static com.idforideas.pizzeria.security.CustomEnvironmentVariables.SECRET;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idforideas.pizzeria.utils.Response;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * @author Nick Galan
 * @version 1.0
 * @since 2/28/2022
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AppUserResource {
    private final AppUserService userService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<Response> saveUser(@RequestBody @Valid AppUser user) {
        return ResponseEntity.status(CREATED).body(
            Response.builder()
                .timeStamp(now())
                .data(of("user", userService.create(user)))
                .message("User created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .build()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<Response> getUsers() {
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(now())
                .data(of("users", userService.getUsers()))
                .message("Users retrieved")
                .status(OK)
                .statusCode(OK.value())
                .build()
        );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@RequestBody @Valid AppUser newUser, @PathVariable("id") Long id) {
            return userService.get(id).map(user -> {
                user.setFullName(newUser.getFullName());
                user.setEmail(newUser.getEmail());
                user.setPassword(newUser.getPassword());
                user.setRole(newUser.getRole());
                return ResponseEntity.ok(
                    Response.builder()
                        .timeStamp(now())
                        .data(of("user", userService.update(user)))
                        .message("User updated")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
                );
            }).orElseGet(() -> {
                return ResponseEntity.status(CREATED).body(
                    Response.builder()
                        .timeStamp(now())
                        .data(of("user", userService.create(newUser)))
                        .message("User created")
                        .status(CREATED)
                        .statusCode(CREATED.value())
                        .build()
                );
            });
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken  = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(System.getenv(SECRET).getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String email = decodedJWT.getSubject();
                AppUser user = userService.get(email).orElseThrow();
                String accessToken = JWT.create()
                    .withSubject(user.getEmail())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
               
            } catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message",  e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
