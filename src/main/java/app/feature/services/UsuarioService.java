package app.feature.services;

import app.core.auth.entities.AuthRoles;
import app.core.auth.enums.Roles;
import app.core.auth.repositories.AuthRolesRepository;
import app.feature.entities.Usuario;
import app.feature.repositories.UsuarioRepository;
import app.core.exceptions.ErrorDetails;
import app.core.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthRolesRepository authRolesRepository;

    public ResponseEntity<GlobalResponse> obtenerUsuario(String userName, HttpServletRequest request) {
        try {
            Optional<Usuario> usuarioBase = usuarioRepository.findByUserName(userName);
            if (!usuarioBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    usuarioBase.get(), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar obtener registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> obtenerListaUsuarios(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    usuarioRepository.findAll(), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar obtener lista de registros en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> guardarUsuario(Usuario usuario, HttpServletRequest request) {
        try {
            Optional<Usuario> usuarioBase = usuarioRepository.findByUserName(usuario.getUserName());
            if (usuarioBase.isPresent()) {
                ErrorDetails errorDetails = new ErrorDetails("Error al intentar guardar registro en la base de datos.", "El usuario " + usuario.getUserName() + " ya existe.");
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        usuario, errorDetails), HttpStatus.BAD_REQUEST);
            }
            Optional<AuthRoles> auth = authRolesRepository.findByRol(Roles.ROLE_USER);
            if (!auth.isPresent()) {
                ErrorDetails errorDetails = new ErrorDetails("Error al intentar guardar registro en la base de datos.", "El ROL que se quiere acceder no existe.");
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                        null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            Set<AuthRoles> roles = new HashSet<>();
            roles.add(auth.get());
            usuario.setRoles(roles);
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    usuarioRepository.save(usuario), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar guardar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> eliminarUsuario(String userName, HttpServletRequest request) {
        try {
            Optional<Usuario> usuarioBase = usuarioRepository.findByUserName(userName);
            if (!usuarioBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            usuarioRepository.delete(usuarioBase.get());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    usuarioBase, null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar eliminar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> editarUsuario(String userName, Usuario usuario, HttpServletRequest request) {
        try {
            Optional<Usuario> usuarioBase = usuarioRepository.findByUserName(userName);
            if (!usuarioBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            usuarioBase.get().setName(usuario.getName());
            usuarioBase.get().setSurname(usuario.getSurname());
            usuarioBase.get().setDni(usuario.getDni());
            usuarioBase.get().setEmail(usuario.getEmail());
            usuarioBase.get().setPassword(usuario.getPassword());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    usuarioRepository.save(usuarioBase.get()), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar editar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Usuario obtenerUsuarioId(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            return null;
        }
    }
}
