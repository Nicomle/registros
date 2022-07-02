package app.core.auth.components;

import app.core.auth.dtos.AuthUserLoggedIn;
import app.core.auth.exceptions.AuthException;
import app.core.auth.request.AuthRequest;
import app.feature.entities.Usuario;
import app.feature.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthValidator {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public AuthUserLoggedIn validarBuscarUsuario(AuthRequest authRequest) throws AuthException {
        Optional<Usuario> userOpt = usuarioRepository.findByUserNameAndPassword(authRequest.getUserName(), authRequest.getPassword());
        if (!userOpt.isPresent()) {
            message("Usuario o contraseña incorrecta");
        }

        Usuario user = userOpt.get();
        return AuthUserLoggedIn.builder()
                .id(user.getId())
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
