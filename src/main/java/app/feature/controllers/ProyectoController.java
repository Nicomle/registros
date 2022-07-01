package app.feature.controllers;

import app.feature.entities.Proyecto;
import app.feature.services.ProyectoService;
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
@RequestMapping("/proyectos")
public class ProyectoController {

    @Autowired
    ProyectoService proyectoService;

    @Autowired
    private Validacion validacion;

    @GetMapping("/obtener")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> obtenerProyecto(@RequestParam String id, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        return proyectoService.obtenerProyecto(Long.parseLong(id), request);
    }

    @GetMapping("/obtener/lista")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<GlobalResponse> obtenerListaProyectos(HttpServletRequest request) {
        return proyectoService.obtenerListaProyectos(request);
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> guardarProyecto(@Valid @RequestBody Proyecto proyecto, BindingResult bindingResult, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarBindingResult(bindingResult, request);
        if (response != null) {
            return response;
        }
        return proyectoService.guardarProyecto(proyecto, request);
    }

    @DeleteMapping("/eliminar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> eliminarProyecto(@RequestParam String id, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        return proyectoService.eliminarProyecto(Long.parseLong(id), request);
    }

    @PutMapping("/editar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> editarProyecto(@RequestParam String id, @Valid @RequestBody Proyecto proyecto, BindingResult bindingResult, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        response = validacion.validarBindingResult(bindingResult, request);
        if (response != null) {
            return response;
        }
        return proyectoService.editarProyecto(Long.parseLong(id), proyecto, request);
    }

    @PostMapping("/cambiar/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GlobalResponse> cambiarStatus(@RequestParam String id, HttpServletRequest request) {
        ResponseEntity<GlobalResponse> response = validacion.validarId(id, request);
        if (response != null) {
            return response;
        }
        return proyectoService.cambiarStatus(Long.parseLong(id), request);
    }
}
