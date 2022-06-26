package app.core.auth.services;

import app.core.auth.response.AuthResponse;
import app.core.auth.dtos.AuthUserLoggedIn;
import app.core.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthJwt authJwt;

    @Autowired
    private DateUtil dateUtil;

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
}
