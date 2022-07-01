package app.feature.controllers;

import app.feature.entities.Usuario;
import app.feature.services.UsuarioService;
import app.core.response.GlobalResponse;
import app.feature.services.Validacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    private Validacion validacion;

    @GetMapping("/obtener")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> obtenerUsuario(@RequestParam String userName, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarString(userName, "Nombre usuario", request);
        if (response != null) {
            return response;
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
        ResponseEntity<GlobalResponse> response = validacion.validarBindingResult(bindingResult, request);
        if (response != null) {
            return response;
        }
        return usuarioService.guardarUsuario(usuario, request);
    }

    @DeleteMapping("/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> eliminarUsuario(@RequestParam String userName, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarString(userName, "Nombre usuario", request);
        if (response != null) {
            return response;
        }
        return usuarioService.eliminarUsuario(userName, request);
    }

    @PutMapping("/editar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> editarUsuario(@RequestParam String userName, @Valid @RequestBody Usuario usuario, BindingResult bindingResult, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarString(userName, "Nombre usuario", request);
        if (response != null) {
            return response;
        }
        response = validacion.validarBindingResult(bindingResult, request);
        if (response != null) {
            return response;
        }
        return usuarioService.editarUsuario(userName, usuario, request);
    }

    @PostMapping("/cambiar/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> cambiarStatus(@RequestParam String id, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        return usuarioService.cambiarStatus(Long.parseLong(id), request);
    }

    @PutMapping("/cambiar/rol/agregar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> agregarRol(@RequestParam String idUsuario, @RequestParam String idRol, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(idUsuario, request);
        if (response != null) {
            return response;
        }
        response = validacion.validarId(idRol, request);
        if (response != null) {
            return response;
        }
        return usuarioService.agregarRol(Long.parseLong(idUsuario), Long.parseLong(idRol), request);
    }

    @PutMapping("/cambiar/rol/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> eliminarRol(@RequestParam String idUsuario, @RequestParam String idRol, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(idUsuario, request);
        if (response != null) {
            return response;
        }
        response = validacion.validarId(idRol, request);
        if (response != null) {
            return response;
        }
        return usuarioService.eliminarRol(Long.parseLong(idUsuario), Long.parseLong(idRol), request);
    }
}
