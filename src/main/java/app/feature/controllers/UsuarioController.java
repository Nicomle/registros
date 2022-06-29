package app.feature.controllers;

import app.feature.entities.Usuario;
import app.feature.services.UsuarioService;
import app.core.exceptions.ErrorDetails;
import app.core.response.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/obtener")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> obtenerUsuario(@RequestParam String userName, HttpServletRequest request) {
        if (userName == null || userName.equals("")) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "El nombre usuario no puede ser vacio ni nulo.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return usuarioService.obtenerUsuario(userName, request);
    }

    @GetMapping("/obtener/lista")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> obtenerListaUsuarios(HttpServletRequest request) {
        return usuarioService.obtenerListaUsuarios(request);
    }

    @PostMapping("/guardar")
    public ResponseEntity<GlobalResponse> guardarUsuario(@Valid @RequestBody Usuario usuario, BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", String.join(". ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return usuarioService.guardarUsuario(usuario, request);
    }

    @DeleteMapping("/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> eliminarUsuario(@RequestParam String userName, HttpServletRequest request) {
        if (userName == null || userName.equals("")) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "El nombre usuario no puede ser nulo ni vacio.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return usuarioService.eliminarUsuario(userName, request);
    }

    @PutMapping("/editar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> editarUsuario(@RequestParam String userName, @Valid @RequestBody Usuario usuario, BindingResult bindingResult, HttpServletRequest request) {
        if (userName == null || userName.equals("")) {
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", "El nombre usuario no puede ser nulo ni vacio.");
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            bindingResult.getAllErrors().forEach(error -> {
                errors.add(error.getDefaultMessage());
            });
            ErrorDetails errorDetails = new ErrorDetails("Error en el ingreso de datos.", String.join(". ", errors));
            return new ResponseEntity<>(GlobalResponse.globalResponse(HttpStatus.BAD_REQUEST, request.getRequestURI(),
                    null, errorDetails), HttpStatus.BAD_REQUEST);
        }
        return usuarioService.editarUsuario(userName, usuario, request);
    }
}
