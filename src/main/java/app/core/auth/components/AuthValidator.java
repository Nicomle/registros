package app.core.auth.components;

import app.core.auth.dtos.AuthUserLoggedIn;
import app.core.auth.exceptions.AuthException;
import app.core.auth.request.AuthRequest;
import app.feature.entities.Usuario;
import app.feature.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AuthValidator {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final String CLIENT_CREDENTIALS = "client_credentials";

    public AuthUserLoggedIn validate(AuthRequest authRequest, String grantType) throws AuthException {

        if (grantType.isEmpty() || !grantType.equals(CLIENT_CREDENTIALS)) {
            message("Invalid grant_type");
        }

        if (Objects.isNull(authRequest) || authRequest.getUserName().equals("") || authRequest.getPassword().equals("")) {
            message("Invalid user or password");
        }

        Optional<Usuario> userOpt = usuarioRepository.findByUserNameAndPassword(authRequest.getUserName(), authRequest.getPassword());
        if (!userOpt.isPresent()) {
            message("Invalid user or password");
        }

        Usuario user = userOpt.get();
        return AuthUserLoggedIn.builder()
                .userName(user.getUserName())
                .name(user.getName())
                .surName(user.getSurname())
                .dni(user.getDni())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }

    public void message(String message) throws AuthException {
        throw new AuthException(message);
    }
}
