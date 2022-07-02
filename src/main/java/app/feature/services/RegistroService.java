package app.feature.services;

import app.feature.entities.Proyecto;
import app.feature.entities.Registro;
import app.feature.entities.Usuario;
import app.feature.repositories.RegistroRepository;
import app.core.exceptions.ErrorDetails;
import app.core.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class RegistroService {

    @Autowired
    RegistroRepository registroRepository;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    ProyectoService proyectoService;

    public ResponseEntity<GlobalResponse> obtenerRegistro(Long id, HttpServletRequest request) {
        try {
            Optional<Registro> registroBase = registroRepository.findById(id);
            if (!registroBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    registroBase.get(), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar obtener registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> obtenerListaRegistros(HttpServletRequest request) {
        try {
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    registroRepository.findAll(), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar obtener lista de registros en la base de datos", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> obtenerListaRegistrosUsuario(String userName, HttpServletRequest request) {
        try {
            Usuario usuarioBase = usuarioService.obtenerUsuarioUserName(userName);
            if (usuarioBase == null) {
                ErrorDetails errorDetails = new ErrorDetails("Error al intentar obtener lista de registros en la base de datos", "El usuario con el nombre usuario: " + userName + " no existe");
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            List<Registro> listRegistros = registroRepository.obtenerRegistrosUsuario(usuarioBase.getId());
            if (listRegistros.size() == 0) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    listRegistros, null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar obtener lista de registros en la base de datos", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> guardarRegistro(Registro registro, HttpServletRequest request) {
        if (registro.getUser().getId() == null || registro.getUser().getId() <= 0) {
            ErrorDetails errorDetails = new ErrorDetails("El ID del usuario ingresado es invalida.", "ID Empresa: " + registro.getUser().getId());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        if (registro.getProject().getId() == null || registro.getProject().getId() <= 0) {
            ErrorDetails errorDetails = new ErrorDetails("El ID del proyecto ingresado es invalida.", "ID Empresa: " + registro.getUser().getId());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        try {
            Usuario usuario = usuarioService.obtenerUsuarioId(registro.getUser().getId());
            if (usuario == null) {
                ErrorDetails errorDetails = new ErrorDetails("El ID del usuario ingresado no existe.", "ID Empresa: " + registro.getUser().getId());
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            Proyecto proyecto = proyectoService.obtenerProyectoId(registro.getProject().getId());
            if (proyecto == null) {
                ErrorDetails errorDetails = new ErrorDetails("El ID del proyecto ingresado no existe.", "ID Empresa: " + registro.getUser().getId());
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            registro.setUser(usuario);
            registro.setProject(proyecto);
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    registroRepository.save(registro), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar guardar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> eliminarRegistro(Long id, HttpServletRequest request) {
        try {
            Optional<Registro> registroBase = registroRepository.findById(id);
            if (!registroBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            Registro registroCopia = registroBase.get();
            registroBase.get().setProject(null);
            registroBase.get().setUser(null);
            registroRepository.delete(registroBase.get());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    registroCopia, null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar eliminar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<GlobalResponse> editarRegistro(Long id, Registro registro, HttpServletRequest request) {
        if (registro.getUser().getId() == null || registro.getUser().getId() <= 0) {
            ErrorDetails errorDetails = new ErrorDetails("El ID del usuario ingresado es invalida.", "ID Empresa: " + registro.getUser().getId());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        if (registro.getProject().getId() == null || registro.getProject().getId() <= 0) {
            ErrorDetails errorDetails = new ErrorDetails("El ID del proyecto ingresado es invalida.", "ID Empresa: " + registro.getUser().getId());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<Registro> registroBase = registroRepository.findById(id);
            if (!registroBase.isPresent()) {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
            Usuario usuario = usuarioService.obtenerUsuarioId(registro.getUser().getId());
            if (usuario == null) {
                ErrorDetails errorDetails = new ErrorDetails("El ID del usuario ingresado no existe.", "ID Empresa: " + registro.getUser().getId());
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            Proyecto proyecto = proyectoService.obtenerProyectoId(registro.getProject().getId());
            if (proyecto == null) {
                ErrorDetails errorDetails = new ErrorDetails("El ID del proyecto ingresado no existe.", "ID Empresa: " + registro.getUser().getId());
                return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                        null, errorDetails), HttpStatus.BAD_REQUEST);
            }
            registroBase.get().setDate(registro.getDate());
            registroBase.get().setUser(usuario);
            registroBase.get().setProject(proyecto);
            registroBase.get().setHours(registro.getHours());
            registroBase.get().setDescription(registro.getDescription());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.OK, request.getRequestURI(),
                    registroRepository.save(registroBase.get()), null), HttpStatus.OK);
        } catch (Exception e) {
            ErrorDetails errorDetails = new ErrorDetails("Error al intentar editar registro en la base de datos.", e.getMessage());
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.INTERNAL_SERVER_ERROR, request.getRequestURI(),
                    null, errorDetails), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
