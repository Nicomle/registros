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
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthRolesRepository authRolesRepository;

    public ResponseEntity<GlobalResponse> obtenerUsuario(String dni, HttpServletRequest request) {
        try {
            Optional<Usuario> usuarioBase = usuarioRepository.findByDni(Long.parseLong(dni));
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
            AuthRoles auth = new AuthRoles();
            auth.setRol(Roles.USER.toString());
            auth.setUsuario(usuario);
            authRolesRepository.save(auth);
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    usuarioRepository.save(usuario), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar guardar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> eliminarUsuario(String dni, HttpServletRequest request) {
        try {
            Optional<Usuario> usuarioBase = usuarioRepository.findByDni(Long.parseLong(dni));
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

    public ResponseEntity<GlobalResponse> editarUsuario(String dni, Usuario usuario, HttpServletRequest request) {
        try {
            Optional<Usuario> usuarioBase = usuarioRepository.findByDni(Long.parseLong(dni));
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

    public Usuario obtenerUsuarioId(Long id) throws Exception {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return usuario.get();
        } else {
            return null;
        }
    }
}