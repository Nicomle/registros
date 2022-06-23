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

    @Value("${USERNAME_KEY}")
    private String USERNAME_KEY;

    @Value("${PASSWORD_KEY}")
    private String PASSWORD_KEY;

    private static final String CLIENT_CREDENTIALS = "client_credentials";

    public boolean validate(MultiValueMap<String, String> params) {
        return Objects.nonNull(params.getFirst(USERNAME_KEY)) && Objects.nonNull(params.getFirst(PASSWORD_KEY));
    }

    public AuthUserLoggedIn validate(MultiValueMap<String, String> formParams, String grantType) throws AuthException {

        if (grantType.isEmpty() || !grantType.equals(CLIENT_CREDENTIALS)) {
            message("Invalid grant_type");
        }

        if (Objects.isNull(formParams) || formParams.getFirst("client_id").equals("") || formParams.getFirst("client_secret").equals("")) {
            message("Invalid client_id or client_secret");
        }

        return AuthUserLoggedIn.builder()
                .name("Gerard")
                .surName("Palet")
                .dni(30000000L)
                .email("paletgerardo@gmail.com")
                .build();
    }

    public AuthUserLoggedIn validate(AuthRequest authRequest, String grantType) throws AuthException {

        if (grantType.isEmpty() || !grantType.equals(CLIENT_CREDENTIALS)) {
            message("Invalid grant_type");
        }

        if (Objects.isNull(authRequest) || authRequest.getUser().equals("") || authRequest.getPassword().equals("")) {
            message("Invalid user or password");
        }

        Optional<Usuario> userOpt = usuarioRepository.findByEmailAndPassword(authRequest.getUser(), authRequest.getPassword());
        if (!userOpt.isPresent()) {
            message("Invalid user or password");
        }

        Usuario user = userOpt.get();
        List<String> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roles.add(role.getRol().toString()));
        return AuthUserLoggedIn.builder()
                .name(user.getName())
                .surName(user.getSurname())
                .dni(user.getDni())
                .email(user.getEmail())
                .roles(roles)
                .build();
    }

    public void message(String message) throws AuthException {
        throw new AuthException(message);
    }
}
