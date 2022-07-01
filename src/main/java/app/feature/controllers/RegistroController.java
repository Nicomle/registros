package app.feature.controllers;

import app.feature.entities.Registro;
import app.feature.services.RegistroService;
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
@RequestMapping("/registros")
public class RegistroController {

    @Autowired
    RegistroService registroService;

    @Autowired
    private Validacion validacion;

    @GetMapping("/obtener")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> obtenerRegistro(@RequestParam String id, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        return registroService.obtenerRegistro(Long.parseLong(id), request);
    }

    @GetMapping("/obtener/lista")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> obtenerListaRegistros(HttpServletRequest request) {
        return registroService.obtenerListaRegistros(request);
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> guardarRegistro(@Valid @RequestBody Registro registro, BindingResult bindingResult, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarBindingResult(bindingResult, request);
        if (response != null) {
            return response;
        }
        return registroService.guardarRegistro(registro, request);
    }

    @DeleteMapping("/eliminar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> eliminarRegistro(@RequestParam String id, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        return registroService.eliminarRegistro(Long.parseLong(id), request);
    }

    @PutMapping("/editar")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> editarRegistro(@RequestParam String id, @Valid @RequestBody Registro registro, BindingResult bindingResult, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        response = validacion.validarBindingResult(bindingResult, request);
        if (response != null) {
            return response;
        }
        return registroService.editarRegistro(Long.parseLong(id), registro, request);
    }
}
