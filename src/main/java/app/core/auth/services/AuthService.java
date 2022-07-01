package app.core.auth.services;

import app.core.auth.entities.AuthRoles;
import app.core.auth.repositories.AuthRolesRepository;
import app.core.auth.response.AuthResponse;
import app.core.auth.dtos.AuthUserLoggedIn;
import app.core.response.GlobalResponse;
import app.core.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthJwt authJwt;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    AuthRolesRepository authRolesRepository;

    @Value("${configs.auth.token.expiration:3600}")
    private int EXPIRATION_TIME;

    public AuthResponse login(AuthUserLoggedIn user) {

        AuthResponse response = AuthResponse.builder()
                .userName(user.getUserName())
                .tokenType("Bearer")
                .accessToken(authJwt.generateToken(user))
                .issuedAt(String.valueOf(dateUtil.getDateMillis()))
                .expiresIn(EXPIRATION_TIME)
                .roles(user.getRoles())
                .build();
        return response;
    }

    public Optional<AuthRoles> obtenerRol(Long id) {
        return authRolesRepository.findById(id);
    }

    public ResponseEntity<GlobalResponse> obtenerListaRoles(HttpServletRequest request) {
        return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                authRolesRepository.findAll(), null), HttpStatus.OK);
    }
}
