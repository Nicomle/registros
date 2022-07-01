package app.feature.services;

import app.core.auth.entities.AuthRoles;
import app.core.auth.enums.Roles;
import app.core.auth.repositories.AuthRolesRepository;
import app.core.auth.services.AuthService;
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

    @Autowired
    private AuthService authService;

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
            usuario.setStatus(true);
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
            usuarioBase.get().setRoles(null);
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

    public ResponseEntity<GlobalResponse> cambiarStatus(Long id, HttpServletRequest request) {
        try {
            Optional<Usuario> usuarioBase = usuarioRepository.findById(id);
            if (!usuarioBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            usuarioBase.get().setStatus(!usuarioBase.get().isStatus());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    usuarioRepository.save(usuarioBase.get()), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar editar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> agregarRol(Long idUsuario, Long idRol, HttpServletRequest request) {
        try {
            Optional<AuthRoles> authBase = authService.obtenerRol(idRol);
            if (!authBase.isPresent()) {
                ErrorDetails errorDetails = new ErrorDetails("Error al intentar agregar un nuevo rol", "El rol con el ID: " + idRol + " no existe");
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            Optional<Usuario> usuarioBase = usuarioRepository.findById(idUsuario);
            if (!usuarioBase.isPresent()) {
                ErrorDetails errorDetails = new ErrorDetails("Error al intentar agregar un nuevo rol", "El usuario con el ID: " + idUsuario + " no existe");
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            Set<AuthRoles> roles = usuarioBase.get().getRoles();
            for (AuthRoles rol : roles) {
                if (rol.getId() == idRol) {
                    ErrorDetails errorDetails = new ErrorDetails("Error al intentar agregar un nuevo rol", "El usuario ya posee el rol con el ID: " + idRol);
                    return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                            null, errorDetails), HttpStatus.BAD_REQUEST);
                }
            }
            roles.add(authBase.get());
            usuarioBase.get().setRoles(roles);
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    usuarioRepository.save(usuarioBase.get()), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar editar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> eliminarRol(Long idUsuario, Long idRol, HttpServletRequest request) {
        try {
            Optional<AuthRoles> authBase = authService.obtenerRol(idRol);
            if (!authBase.isPresent()) {
                ErrorDetails errorDetails = new ErrorDetails("Error al intentar agregar un nuevo rol", "El rol con el ID: " + idRol + " no existe");
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            Optional<Usuario> usuarioBase = usuarioRepository.findById(idUsuario);
            if (!usuarioBase.isPresent()) {
                ErrorDetails errorDetails = new ErrorDetails("Error al intentar agregar un nuevo rol al usuario", "El usuario con el ID: " + idUsuario + " no existe");
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            Set<AuthRoles> roles = usuarioBase.get().getRoles();
            if (roles.size() == 1) {
                ErrorDetails errorDetails = new ErrorDetails("Error al intentar eliminar un rol", "El usuario necesita poseer minimo un rol");
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            boolean exit = false;
            for (AuthRoles rol : roles) {
                if (rol.getId() == idRol) {
                    exit = true;
                    break;
                }
            }
            if (!exit) {
                ErrorDetails errorDetails = new ErrorDetails("Error al intentar eliminar un rol", "El usuario no posee el rol con el ID: " + idRol);
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            roles.remove(authBase.get());
            usuarioBase.get().setRoles(roles);
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
